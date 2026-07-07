# myRewards e-Bill — reverse engineering

Black-box reverse engineering of the **myRewards e-Bill** subscription portal, tenant
`kandy.ebill.myrewards.lk`. A **new project**, separate from the EZVIZ work in this repo's root.

## How this was obtained

No live access was needed. The endpoint `POST /e-bill/subscribe/store` crashed with a SQL error and,
because the app runs with **debug mode on in production**, it served a full Laravel/Symfony exception
page — leaking source code, file paths, DB schema, versions, and a live customer phone number. That
one page (`evidence/error_page_debug_leak.png`) is the whole recon phase handed over for free, and is
itself the top security finding.

This analysis environment's egress is locked down (it can't reach the host), so the write-up is built
from the screenshot you provided plus Laravel framework knowledge. Nothing was probed against
production.

## Contents

| File | What |
|---|---|
| **REPORT.md** | Full reverse-engineering write-up: what the endpoint does, reconstructed flow, framework/version, DB schema, and everything the leak exposed. |
| **SECURITY_FINDINGS.md** | Severity-rated findings + fixes: F-1 debug-mode leak (Critical), F-2 possible Ignition RCE / CVE-2021-3129 (Critical, to confirm), F-3 subscribe-endpoint IDOR/authz (High), F-4 reversible OTP storage, F-5 the schema/code-drift 500, F-6 enumerable tenancy. |
| **reconstructed/EbillController.store.php** | Annotated reconstruction of the leaked controller code (lines 305–314 were shown verbatim). |
| **toolkit/recon.sh** | Read-only, authorized-use-only fingerprint + misconfig checker for when the host is reachable. |
| **evidence/error_page_debug_leak.png** | The source artifact — the production debug stack trace. |

## Headline facts

- **Stack:** Laravel 8.x (PHP); `fruitcake/laravel-cors`, `fideloper/proxy`; docroot `/var/www/kandy.myrewards.lk/`.
- **Endpoint:** `EbillController@store` — OTP-based e-bill subscription; `encrypt()`s the OTP, updates `CRM_Customer.has_subscribed`.
- **DB:** table `CRM_Customer` (cols `contact_no`, `verification_code`, `has_subscribed`; stale `merchant_id` ref = the crash).
- **Tenancy:** numeric `merchant_id` (`200200` = kandy) per subdomain.

## First thing the operator should do

Set `APP_DEBUG=false` in production and `php artisan config:cache`. Then confirm/patch Ignition
(F-2). See `SECURITY_FINDINGS.md` for the ordered remediation list.

## If you want the deeper live analysis

This sandbox can't reach the site. Either (a) reconfigure the environment's network policy to allow
`*.myrewards.lk` (Claude Code on the web → environment network settings), or (b) run `toolkit/recon.sh`
from an authorized machine and drop the output here, and the report can be extended with the live
fingerprint, full route map, and JS/asset analysis.
