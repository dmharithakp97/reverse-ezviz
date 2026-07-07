# myRewards e-Bill — Reverse Engineering Report

**Target:** `https://kandy.ebill.myrewards.lk/e-bill/subscribe/store`
**Date:** 2026-07-07
**Method:** Black-box, no live access. The application handed us its own internals by
returning a **Laravel debug-mode stack trace** (HTTP 500) in production. Everything below is
reconstructed from that single error page (`evidence/error_page_debug_leak.png`) plus framework
knowledge. No live systems were touched — this environment's egress policy blocks the host, so
nothing here was probed against production.

---

## 1. TL;DR

The e-bill subscription endpoint crashed with a SQL error and, because the app runs with
`APP_DEBUG=true` in production, it rendered a full **Symfony/Laravel exception page** to the
browser. That page leaks:

- The exact framework (**Laravel**, Illuminate 8.x-era) and third-party packages.
- The **absolute deploy path** (`/var/www/kandy.myrewards.lk/`).
- The **application source code** of the controller that handles subscriptions
  (`app/Http/Controllers/EbillController.php`, lines 305–314).
- The **database schema**: table `CRM_Customer`, columns `contact_no`, `verification_code`,
  `has_subscribed`, and a (now-broken) reference to `merchant_id`.
- The **live request data**: `merchant_id = 200200`, `contact_no = 772152986`.

The crash itself is a bug (`Unknown column 'merchant_id'` — the code references a column that no
longer exists in the table). But the **security problem is the leak**: debug mode should never be
on in production. See `SECURITY_FINDINGS.md` for severity-rated issues and fixes.

---

## 2. What the endpoint does

`POST /e-bill/subscribe/store` → `EbillController@store`. This is the "confirm subscription" step
of an **OTP-based e-bill sign-up flow** for a utility/merchant billing portal:

1. A customer enters their mobile number to subscribe to electronic bills for a given merchant
   (store). `merchant_id` identifies the store (here `200200` = the "kandy" tenant).
2. The app generates a **verification code (OTP)**, `encrypt()`s it, and stores it (line 305 shows
   `'verification_code' => enc…`, i.e. `encrypt(...)`).
3. It then looks up the matching CRM customer and flips a subscription flag:

```php
// app/Http/Controllers/EbillController.php  (reconstructed from the leaked trace, ~lines 300–314)
            // ... create/update a subscription/OTP row ...
            'verification_code' => encrypt($code),          // line 305
        ]);

        # if crm customer update table
        $crm_customer = CrmCustomer::where('merchant_id', $merchantId)   // line 310  ← CRASH
                          ->where('contact_no', $contactNo)
                          ->first();

        if ($crm_customer) {                                             // line 312
            $crm_customer->update(["has_subscribed" => /* ... */]);      // line 313
        }
```

The generated SQL (verbatim from the error banner):

```sql
select * from `CRM_Customer`
where (`merchant_id` = 200200 and `contact_no` = 772152986)
limit 1
```

`merchant_id` is not a column on `CRM_Customer` → `SQLSTATE[42S22] 1054 Unknown column`. So the
schema and the code have drifted (a column was renamed/removed, e.g. to `vec_chart_id`/`store_id`,
or the lookup should join through a different table). Every subscription attempt on this tenant is
currently 500-ing at line 310.

---

## 3. Architecture reconstructed from the trace

| Layer | Evidence | Conclusion |
|---|---|---|
| **Framework** | `vendor/laravel/framework/src/Illuminate/...`, `public/index.php`, Ignition/Symfony error renderer | **Laravel** (PHP). Middleware stack (VerifyCsrfToken, StartSession, ShareErrorsFromSession, SubstituteBindings) is the stock Laravel web pipeline. |
| **Version band** | Presence of `fruitcake/laravel-cors` (`vendor/fruitcake/laravel-cors/src/HandleCors.php`) and `fideloper/proxy` (`vendor/fideloper/proxy/src/TrustProxies.php`) | These two ship as defaults in **Laravel 7–8**; Laravel 9 replaced both with framework-native equivalents. So this is almost certainly **Laravel 8.x** (or late 7.x). |
| **Deploy path** | `/var/www/kandy.myrewards.lk/` | Classic LAMP/nginx docroot. Note the vhost root is `kandy.myrewards.lk`, distinct from the `kandy.ebill.myrewards.lk` URL host — a reverse proxy / host alias. |
| **Multi-tenancy** | `merchant_id = 200200`, subdomain `kandy.` | **Per-store subdomains** keyed by a numeric `merchant_id`. Other cities/stores almost certainly exist as sibling subdomains with their own merchant ids. |
| **Database** | `CRM_Customer` table (PascalCase, `CRM_` prefix) | A CRM-style customer store. The `CRM_` prefix suggests either a legacy schema or a shared CRM DB the Laravel app was bolted onto (models mapped onto pre-existing table names via `$table`). |
| **Data model** | Columns `contact_no`, `verification_code`, `has_subscribed`; model `CrmCustomer` | Customer keyed by phone; subscription state is a boolean-ish `has_subscribed`; OTP stored `encrypt()`ed. |
| **CSRF** | `VerifyCsrfToken` in the pipeline | The `store` POST expects a valid CSRF token — so the flow is driven from a server-rendered Blade form (session-based), not a token API. |

### Reconstructed route/flow

```
GET  /e-bill/subscribe            → Blade form: enter mobile number (+ merchant context from subdomain)
POST /e-bill/subscribe/store      → EbillController@store: create OTP row, encrypt() code,
                                     update CRM_Customer.has_subscribed  ← currently 500s
(then, by convention) 
     /e-bill/subscribe/verify     → enter OTP, decrypt()/compare verification_code, confirm
```

---

## 4. Concrete facts leaked (inventory)

- **Host / tenant:** `kandy.ebill.myrewards.lk`; merchant `200200`; docroot `/var/www/kandy.myrewards.lk/`.
- **Framework:** Laravel 8.x (PHP), stock web middleware, `fruitcake/laravel-cors`, `fideloper/proxy`.
- **Entry point:** `public/index.php:52`.
- **App class:** `App\Http\Controllers\EbillController@store`, source lines 300–314 visible.
- **Model:** `App\Models\CrmCustomer` (or `App\CrmCustomer`) → table **`CRM_Customer`**.
- **Columns:** `contact_no` (int-bound), `verification_code` (Laravel `encrypt()`ed blob),
  `has_subscribed`, and a stale `merchant_id` reference.
- **Live PII in the error:** a real mobile number `772152986` (Sri Lanka `+94 77 215 2986`) was
  echoed into the public error page — that is itself a data-exposure event.

---

## 5. Why this is the "reverse engineering" jackpot

You didn't need the JS bundle or an APK. A production stack trace with `APP_DEBUG=true` is the most
information-dense artifact a black-box tester can get: it is effectively a partial **source-code +
schema dump** on demand. The single 500 page told us the framework, version, file layout, the
controller's actual code, the table and column names, and a live request payload. That is the whole
first phase of reverse engineering handed over for free — and it's a serious misconfiguration.

See:
- **`SECURITY_FINDINGS.md`** — severity-rated findings (debug leak, possible Ignition RCE, IDOR/authz
  on the subscribe endpoint, PII exposure) with remediation.
- **`reconstructed/EbillController.store.php`** — the annotated reconstruction of the leaked code.
- **`toolkit/`** — a ready-to-run fetch+analyze pipeline for when the host is reachable (this sandbox
  can't reach it; run from a machine that can, or reconfigure the environment's network policy).
