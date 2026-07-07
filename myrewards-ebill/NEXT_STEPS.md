# Pick up here (laptop handoff)

This file exists so a **fresh Claude Code session on your laptop** starts with full context — it
won't have the chat where this was created. Point Claude at this file first.

## What this project is

Black-box reverse engineering + security assessment of **`kandy.ebill.myrewards.lk`** (myRewards
e-Bill, a Laravel app you administer). It was reconstructed entirely from **one production error page**
— the app runs with `APP_DEBUG=true`, so a 500 leaked its source, schema, versions, and a live
customer phone number.

Read in this order: `README.md` → `REPORT.md` → `SECURITY_FINDINGS.md` →
`reconstructed/EbillController.store.php` → `MULTI_TENANT_EXPOSURE.md` → `DATABASE_ANALYSIS.md`.
Source evidence: `evidence/error_page_debug_leak.png`.

**Database deep-dive:** `DATABASE_ANALYSIS.md` — MySQL/MariaDB fingerprint, reconstructed
`CRM_Customer` schema, and DB-layer findings: DB-1 int comparison on a phone column (type juggling),
DB-2 reversible OTP at rest, DB-3 schema/code drift, DB-4 shared-CRM blast radius, DB-5 debug leaks
DB creds on next connection error, DB-6 no row-level authz + `select *` over-fetch. To confirm the
[V] items, run **`toolkit/db_audit.sql`** (read-only) against the branch DB and paste the output back,
or hand over `SHOW CREATE TABLE CRM_Customer` + the model's `$table/$fillable/$casts`.

**Hard-mode / fleet question (other subdomains + branch codes):** see `MULTI_TENANT_EXPOSURE.md`.
Short version: the code-level issues (F-3/F-4/F-5) are shared by every branch because it's one
codebase; the debug/RCE exposure (F-1/F-2) must be confirmed per branch. Discovery + exposure mapping
can't run from the analysis sandbox (DNS/CT logs are egress-blocked) — run `toolkit/enum_subdomains.sh`
then `toolkit/sweep.sh` from your laptop and hand back `out/all_subdomains.txt` + `out/exposure_matrix.csv`.

## State as of this handoff

- All analysis is written, committed, and pushed to branch
  **`claude/reverse-engineer-rewards-site-amqj00`**.
- Findings are documented; three of them are marked **"to confirm"** because they can't be verified
  without either live access or the source. No live probing was done (the web sandbox that produced
  this can't reach the host, and neither could an Azure VM — see the chat rationale).

## Open questions blocking a *verified* (not inferred) assessment

| # | Question | How to answer it |
|---|---|---|
| **F-2** | Is `facade/ignition < 2.5.2`? (⇒ unauthenticated RCE, CVE-2021-3129) | Paste the `facade/ignition` line from **`composer.lock`**. This is the single most important item. |
| **F-3** | Does `EbillController@store` rate-limit / require auth before sending OTP & mutating `has_subscribed`? | Paste **`app/Http/Controllers/EbillController.php`** + **`routes/web.php`**. |
| **F-1** | Confirm `APP_DEBUG`/`APP_ENV` values in prod | Paste **`.env` with all secret VALUES redacted** — only the keys and the two booleans matter. |

## Do this when you sit down at the laptop

You are the owner/admin, so skip probing — just give Claude the files. From your project root:

```bash
git fetch origin
git checkout claude/reverse-engineer-rewards-site-amqj00
# then, from the server or a deploy checkout, copy these in (redact .env secrets first):
#   composer.lock
#   app/Http/Controllers/EbillController.php
#   routes/web.php
#   .env            (values redacted)
#   the CRM_Customer migration/schema
```

Then tell Claude: *"Read myrewards-ebill/NEXT_STEPS.md, then verify the 'to confirm' findings against
the source files I just added and update SECURITY_FINDINGS.md."*

### Alternative — live external probe (only if you want the attacker's-eye view)
Read-only, non-destructive, run from any machine that can reach the host (laptop, web server, or a
throwaway VM). Do **not** run it from a machine you're not authorized on (you own this one — fine):
```bash
bash myrewards-ebill/toolkit/recon.sh https://kandy.ebill.myrewards.lk
# then hand toolkit/out/ back to Claude
```

## Priority reminder

1. **F-2 first** — the potential unauthenticated RCE. `composer.lock` settles it in seconds.
2. **F-1** — `APP_DEBUG=false` in prod stops the whole disclosure surface (this is the eventual fix,
   not part of this analysis phase, but it's the one-line emergency mitigation).
3. Then F-3 / F-4 / F-5 / F-6.

_Fixing is the next phase — this phase is understanding issues + impact. See SECURITY_FINDINGS.md._
