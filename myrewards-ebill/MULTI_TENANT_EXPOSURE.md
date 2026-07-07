# Multi-tenant exposure — do the issues span every branch?

Answers the hard-mode question: *find the other subdomains + branch codes, and is it possible to
probe all of them with the issues we identified?*

Two parts: (1) **can these issues affect every branch** — an architecture answer I can give now;
(2) **the concrete list of subdomains + branch codes** — which requires running the toolkit from a
machine with internet (this analysis sandbox is egress-blocked and can't enumerate DNS/CT logs).

---

## 1. What we actually know about the tenancy model

From the leaked error page:

- The document root is **`/var/www/kandy.myrewards.lk/`** — a **per-subdomain vhost with its own
  directory**. This is the single most important structural clue.
- Tenancy is a numeric **`merchant_id`** (`200200` for the `kandy` branch), used to scope
  `CRM_Customer` lookups.
- The URL host `kandy.ebill.myrewards.lk` differs from the docroot host `kandy.myrewards.lk` — a
  proxy/alias layer sits in front.

The docroot naming implies **one deployed copy of the app per branch**, not necessarily a single
shared multi-tenant instance. That distinction decides how the vulnerabilities propagate:

| Deployment model | Evidence for it | How the issues propagate |
|---|---|---|
| **A. Per-branch copies** (each subdomain = its own `/var/www/<branch>.myrewards.lk/` checkout + own `.env`) | The `/var/www/kandy.myrewards.lk/` docroot pattern | **Same code, independent config.** Every branch runs the *same* `EbillController` and the *same* `composer.lock` versions (deployed from one repo), so the *code-level* issues (F-3 IDOR, F-4 OTP, F-5 crash) exist everywhere. But `APP_DEBUG` and patch level live in each branch's own `.env`/vendor — so F-1/F-2 exposure can **vary per branch** depending on how consistently they were deployed. |
| **B. Single multi-tenant app** (one instance, subdomain routing) | The proxy/alias in front | One `.env`, one vendor tree → **if kandy is vulnerable, all are, identically.** |

**Either way, the *code-level* findings are shared** because it's one codebase. The only thing that
can differ branch-to-branch is F-1 (debug flag) and F-2 (Ignition version) — and only under model A.

### So: "is it possible to probe all of these by the issues identified?"

**Yes, architecturally — with one caveat you must verify, not assume.**

- **F-3 (subscribe IDOR / OTP+SMS abuse), F-4 (reversible OTP), F-5 (the crash):** shared code ⇒
  present on **every** branch running this app. If it's exploitable on kandy, it's exploitable on all.
- **F-1 (debug leak) & F-2 (Ignition RCE):** present on every branch **that was deployed with
  `APP_DEBUG=true` and the vulnerable `facade/ignition`**. If your branches are provisioned from one
  script/image (very likely), they almost certainly **share the same misconfiguration** — i.e. debug
  is on everywhere. But because model A gives each its own `.env`, you must **confirm per branch**
  rather than infer. That's exactly what `toolkit/sweep.sh` does (it flags `debug=YES-LEAK` per host).

The realistic conclusion: **treat every branch as vulnerable until the sweep proves otherwise.** A
one-branch fix (turning off debug on kandy) does **not** protect the others under model A — that's the
key risk of this architecture. Inconsistent rollout means some branches stay exposed indefinitely.

---

## 2. The branch codes (merchant_id scheme)

We have exactly one data point: **kandy → 200200**. That's not enough to reverse the numbering with
confidence. Hypotheses to test once the sweep returns a few pairs:

- `200200` looks like a **structured code**, not a sequence counter (the repeated `200` hints at a
  `region`/`type` prefix + branch suffix, e.g. `2002xx`). If galle comes back `200300` or `200201`,
  the scheme falls out immediately.
- `sweep.sh` extracts each branch's `merchant_id` from its subscribe form's hidden field, so after one
  run you'll have the full `subdomain → merchant_id` map. Send me that CSV and I'll reverse the scheme
  and tell you whether ids are **guessable/enumerable** (which would let someone target branches
  without even knowing the subdomain — see F-6).

---

## 3. Why I can't just hand you the subdomain list from here

Subdomain discovery needs one of: **Certificate Transparency logs** (crt.sh), **DNS queries**, passive
DNS, or a scanner — all of which are external hosts this analysis sandbox blocks (verified: even
`crt.sh`/`example.com` return 403). Web search doesn't index the myRewards portal (it's private/low
traffic), so that channel returned nothing. There is no way for me to enumerate your real subdomains
from inside this environment. The toolkit does it properly from your side.

---

## 4. Run this from your laptop (authorized — your own domain)

```bash
cd myrewards-ebill/toolkit

# 1) Discover subdomains: Certificate Transparency (authoritative) + DNS brute of the SL-branch wordlist
./enum_subdomains.sh myrewards.lk
#    -> out/all_subdomains.txt

# 2) Map exposure + extract branch codes across every discovered host (read-only, no exploits)
./sweep.sh out/all_subdomains.txt
#    -> out/exposure_matrix.csv   columns: host, debug_leak(F-1), ignition_surface(F-2), merchant_id
```

Then hand `out/all_subdomains.txt` and `out/exposure_matrix.csv` back to me. With those I'll:

- confirm which branches are actually leaking / are RCE candidates (turning the F-1/F-2 "per-branch
  varies" caveat into a definitive list),
- reverse the `merchant_id` scheme and rate its guessability,
- update `SECURITY_FINDINGS.md` with the fleet-wide blast radius.

> The definitive, no-scanning answer is still the owner route: your `composer.lock` +
> deploy/provisioning scripts tell me instantly whether all branches share one vendor tree and one
> `APP_DEBUG` — which settles the whole question without touching a single subdomain.
