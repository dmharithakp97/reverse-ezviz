# Security Findings — myRewards e-Bill

Findings derived **only** from the leaked production error page (`evidence/error_page_debug_leak.png`).
No exploitation was performed and none should be without written authorization from the site owner.
Where an issue is a *possibility* that depends on facts we can't see from one screenshot, it is marked
**(to confirm)** with the exact check to run.

---

## F-1 — Production debug mode leaks source, schema, and PII  — **Critical**

**What:** The app runs with `APP_DEBUG=true` (or an equivalent verbose error handler) in production.
An ordinary GET/POST that triggers any unhandled exception returns a full **Laravel/Symfony exception
page** containing: framework + package versions, absolute filesystem paths, **application source code
snippets**, the **database table/column names**, the failing **SQL with bound values**, and the
**live request data** (a real customer mobile number, `772152986`, was rendered into the page).

**Impact:** This is a standing information-disclosure channel. An attacker can deliberately induce
errors to map the app's internals — models, tables, columns, file layout, versions — which is the
entire recon phase of an attack, plus incidental **PII leakage** (phone numbers) to anyone who hits a
500.

**Fix (do this first):**
- Set `APP_DEBUG=false` in the production `.env` and `php artisan config:cache`.
- Ensure `APP_ENV=production`.
- Add a real error handler so users see a generic page; log details server-side only.
- Rotate `APP_KEY` if it was ever exposed (it wasn't in this page, but audit other error pages).

---

## F-2 — Debug mode + Ignition ⇒ possible unauthenticated RCE (CVE-2021-3129)  — **Critical (to confirm)**

**What:** Laravel 8 ships **Facade\Ignition** as its debug error page. Ignition **< 2.5.2** with
`APP_DEBUG=true` is vulnerable to **CVE-2021-3129**, an unauthenticated remote code execution via the
`/​_ignition/execute-solution` endpoint (log-file poisoning through a crafted `viewFile`). Because F-1
proves debug mode is on and the stack is Laravel-8-era, this app is a candidate.

**To confirm (owner / authorized tester only):**
- Check `composer.lock` → `facade/ignition` version. `< 2.5.2` = vulnerable.
- Check whether `POST /_ignition/execute-solution` and `/_ignition/health-check` are reachable.
- The definitive remediation is F-1 (turn off debug) **and** `composer update facade/ignition`.

**Impact if confirmed:** full server compromise, unauthenticated. This is one of the most-exploited
Laravel CVEs in the wild; scanners hit `/_ignition/*` constantly. Treat as emergency until disproven.

---

## F-3 — Subscription endpoint authorization / IDOR on `contact_no`  — **High (to confirm)**

**What:** `EbillController@store` looks up `CRM_Customer` by `merchant_id` + `contact_no` and then
`->update(["has_subscribed" => ...])`. The `contact_no` is user-supplied (it's the number being
subscribed). If the endpoint does not verify that the requester actually controls that number
**before** mutating state — i.e. the OTP is generated here but ownership isn't proven until a separate
verify step — then:

- An attacker can **enumerate customers**: submitting arbitrary numbers reveals (via differing
  responses / timing / the very existence of a `CRM_Customer` row) which numbers are registered under
  a merchant.
- An attacker may be able to **toggle `has_subscribed`** or trigger OTP SMS to arbitrary numbers
  (**SMS bombing / cost-abuse**) since the OTP is created before verification.

**To confirm:** review whether `store` rate-limits, whether it requires an authenticated/session-bound
subscriber, and whether `has_subscribed` is set at `store` time or only after OTP verification.

**Fix:** rate-limit per IP and per `contact_no`; require OTP verification before any state change;
don't reveal whether a number exists ("if it's registered, you'll get a code"); add a CAPTCHA/throttle
on OTP generation.

---

## F-4 — OTP stored with reversible `encrypt()` instead of a hash  — **Medium**

**What:** `verification_code` is stored via Laravel `encrypt()` (line 305: `'verification_code' =>
encrypt(...)`). `encrypt()` is **reversible** (AES via `APP_KEY`), not a one-way hash. A verification
code is a short-lived secret that only ever needs equality checking — it should be **hashed**
(`Hash::make` / `hash_hmac`) or, better, kept only server-side/never persisted in a form the app must
decrypt. Storing it reversibly means anyone with DB read access (SQLi, backup leak, insider) recovers
live OTPs, and it couples OTP secrecy to `APP_KEY` secrecy.

**Fix:** store `hash_hmac('sha256', $code, config('app.key'))` or a bcrypt hash; compare with
`hash_equals`. Short TTL + single-use + attempt cap.

---

## F-5 — Schema/code drift causes a hard 500 on the happy path  — **Medium (availability)**

**What:** `Unknown column 'merchant_id' in 'where clause'` — line 310's query references a column that
doesn't exist on `CRM_Customer`. Every subscription attempt on this tenant currently fails with a 500.
This is a functional outage, and combined with F-1 it turns a routine bug into an information leak.

**Fix:** correct the lookup (the column was likely renamed — candidates seen in sibling errors include
`vec_chart_id`/`store_id`, or the merchant scoping belongs on a different table via a relation). Add a
test around `store`. After F-1, this becomes a quiet log entry instead of a public disclosure.

---

## F-6 — Multi-tenant model is enumerable  — **Low / informational**

**What:** Tenancy is a numeric `merchant_id` (`200200`) exposed per subdomain (`kandy.`). Sibling
stores are guessable (subdomain wordlist + sequential/patterned merchant ids). Not a vuln by itself,
but it widens the attack surface: every finding above applies to every tenant, and merchant ids appear
to be low-entropy.

**Fix:** ensure per-tenant authorization is enforced server-side (not just by subdomain), and avoid
exposing raw sequential merchant ids in requests/responses where a random public id would do.

---

## Priority order

1. **F-1** turn off debug mode in production (stops the leak immediately).
2. **F-2** confirm/patch Ignition (potential unauth RCE).
3. **F-3** lock down the subscribe endpoint (authz + rate limit).
4. **F-4 / F-5 / F-6** hardening.

## Responsible-disclosure note

If this property is not yours, do **not** probe it. Report F-1/F-2 to the operator
(myRewards / the site owner) privately — debug-mode-off and an Ignition bump are trivial fixes and the
RCE risk is severe. This report intentionally contains **no working exploit**.
