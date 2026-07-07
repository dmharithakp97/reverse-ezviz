# Deep database analysis — myRewards e-Bill

Focused reverse engineering of the **data layer**, derived from the production error page
(`evidence/error_page_debug_leak.png`) — both the `QueryException` and the underlying `PDOException`
were readable. Confirmed facts are marked **[C]**; inferences needing DB access to verify are **[V]**
(run `toolkit/db_audit.sql` to settle them).

---

## 1. DBMS + access-layer fingerprint  **[C]**

| Fact | Evidence |
|---|---|
| **MySQL or MariaDB** | Error `1054`, `SQLSTATE[42S22]`, and **backtick** identifier quoting (`` `CRM_Customer` ``) are all MySQL-family-specific. |
| **PDO + prepared statements** | 2nd exception trace: `Illuminate/Database/Connection.php -> prepare (line 368)`, reached via `Query/Builder.php -> select -> runSelect -> Connection::run`. The query is a **parameterized prepared statement**, not string-concatenated SQL. |
| **Eloquent ORM** | `CrmCustomer::where('merchant_id',…)->where('contact_no',…)->first()` compiles to `select * from … limit 1`. |
| **Bindings passed as integers** | The rendered SQL shows `` `merchant_id` = 200200 `` and `` `contact_no` = 772152986 `` **unquoted** → the app hands these to PDO as PHP `int`, not string. |

**Immediate consequence:** the confirmed query path is **not SQL-injectable** (parameterized). Do not
chase SQLi on *this* endpoint. The real DB risks are elsewhere — type handling, secrets-at-rest, DB
scope, and the debug-driven credential exposure. (SQLi can't be *ruled out* fleet-wide, though — any
`DB::raw()`/`whereRaw()`/`->orderByRaw()` with user input elsewhere in the codebase would reopen it;
`db_audit.sql` can't see code, so grep the source for those — see §7.)

---

## 2. Schema reconstructed from the leak  **[C for listed columns]**

Table **`CRM_Customer`** (MySQL). Columns proven to exist / be referenced:

| Column | Proven from | Notes |
|---|---|---|
| `contact_no` | WHERE clause | Compared as an **integer** (see §3). Holds the subscriber mobile number. |
| `verification_code` | Controller line 305 (`encrypt(...)`) | Stores a Laravel `encrypt()` blob — **reversible AES**, not a hash (see §4). |
| `has_subscribed` | Controller line 313 (`update([...])`) | Subscription-state flag, written on subscribe. |
| `merchant_id` | WHERE clause — **but MISSING** | Code references it; column does not exist on the table → the 1054 crash (see §5). |

The **`CRM_`** prefix + **PascalCase** name is *not* Laravel's default convention (which would be a
snake_case plural, `customers`). That means the model has an explicit `$table = 'CRM_Customer'` and,
almost certainly, the app is wired onto a **pre-existing / external CRM database** rather than a
schema Laravel migrations created (see §6). A real `CRM_Customer` row in a billing CRM will carry far
more than these four columns — name, NIC/ID, address, email, account/meter number — none of which we
can see, but all of which sit one `select *` away.

---

## 3. **DB-1 — Integer comparison on a phone-number column (type juggling)**  **High / [V]**

The rendered SQL compares `` `contact_no` = 772152986 `` with an **unquoted integer**. Sri Lankan
mobile numbers are 9 significant digits, usually written/stored with a leading zero
(`0772152986`) or country code (`+94772152986`). Comparing a phone column against an *integer*
triggers MySQL **implicit type conversion**, and the failure modes are real:

- **If `contact_no` is `VARCHAR`** and the app queries with an int, MySQL casts the column to a number
  per-row. Then:
  - `'0772152986' = 772152986` → **TRUE** (leading zero dropped in numeric cast).
  - `'772152986abc' = 772152986` → **TRUE** (MySQL parses the leading numeric run, warns, matches).
  - So the stored string and the queried int can match even when they are *not* the same string. Two
    customers whose numbers differ only by leading zeros / formatting / trailing junk can **collide**.
- **If `contact_no` is `INT`**, `0772152986` can't be stored faithfully (leading zero lost; and the
  full `94772152986` **overflows `INT`** max 2,147,483,647 → needs `BIGINT`). Numbers get truncated or
  rejected.

**Why it matters for security, not just correctness:** the OTP subscribe/verify flow *identifies the
customer by this comparison*. A fuzzy/lossy match means the verification step can bind to the **wrong
`CRM_Customer` row** — an attacker who can get their input to numerically collide with a victim's
stored value could have the OTP/`has_subscribed` action applied against the victim's record. At
minimum it's an integrity bug; at worst a cross-account takeover primitive.

**Verify:** `db_audit.sql` prints the column type of `contact_no`. If it's `VARCHAR`, the app must
compare with a **string** and normalize numbers to one canonical form before querying.

---

## 4. **DB-2 — Reversible secret at rest (`verification_code`)**  **Medium-High / [C]**

`verification_code` holds `encrypt($code)` — Laravel `encrypt()` is **AES-256-CBC + HMAC, reversible**
with `APP_KEY`. A verification code only ever needs equality-checking, so it should be a **hash**
(`Hash::make` / `hash_hmac`), never a reversible ciphertext.

DB-level blast radius: anyone who can *read this column* — via a DB backup, an insider, the F-2
Ignition RCE, or a future SQLi — recovers **live OTPs** for every pending subscription, provided they
also have `APP_KEY`. And `APP_KEY` leaks through the exact same debug/RCE channels. So the OTP's
secrecy is only as strong as `APP_KEY` secrecy, which we've shown is weak here. Storing it hashed
breaks that chain: even full DB read + `APP_KEY` yields nothing usable.

---

## 5. **DB-3 — Schema/code drift → hard 500 on the happy path**  **Medium (availability + governance) / [C]**

`merchant_id` is in the code but not in the table. Two readings, both bad:

- **Migration not applied on this DB** (deployment drift): the branch's schema is behind the code.
  Implies migrations aren't reliably tracked/run per branch — a governance gap that will recur.
- **Column was renamed** and the model wasn't updated.

Either way, **every subscription on this branch currently 500s at the lookup**, and (because debug is
on) each failure re-leaks the schema + a live phone number. `db_audit.sql` checks whether `merchant_id`
exists anywhere in the schema and what the intended scoping column likely is.

---

## 6. **DB-4 — `CRM_` prefix implies a shared/back-office CRM database**  **High (blast radius) / [V]**

The naming strongly suggests the customer-facing e-bill web app connects to a **pre-existing CRM
database**, possibly shared with back-office systems, rather than an app-owned schema. If so:

- The web app's DB user may hold **broad privileges over the entire CRM** (full customer master,
  billing, maybe financial/meter data), when it only needs to read/write a subscription flag and an OTP.
- A compromise of the *public* web app (F-2 RCE, leaked `.env`) therefore reaches **the core CRM**, not
  just e-bill data. That is a segmentation failure: an internet-facing app should never share a
  high-privilege DB account with internal systems.

**Verify:** `db_audit.sql` runs `SHOW GRANTS` for the current user and counts `CRM_%` tables — if the
web user can `SELECT`/`UPDATE` across the whole CRM, that's the finding.

---

## 7. **DB-5 — Debug mode will leak DB *credentials* on the next connection error**  **Critical / [C+V]**

Right now debug leaked the *schema*. The moment any **connection-level** failure occurs (DB down, wrong
password, host unreachable, max-connections), Laravel's `PDOException` message — rendered to the browser
under `APP_DEBUG=true` — includes the **DSN**: `host`, `port`, and `dbname`, and depending on the driver
error, hints at the `username`. Combined with the F-2 RCE that reads `.env` outright, treat the DB
credentials (`DB_HOST/DB_PORT/DB_DATABASE/DB_USERNAME/DB_PASSWORD`) as **exposed-on-demand**. If the DB
port is reachable from anywhere the attacker can reach (or via the compromised web host), that's direct
DB access. Turning off debug closes the passive half; network-isolating the DB + least-privilege closes
the rest.

---

## 8. **DB-6 — No row-level authorization; `select *` over-fetch**  **Medium / [V]**

- The only scoping on the read/update is `(merchant_id, contact_no)`. `contact_no` is user-supplied, so
  there is **no ownership check at the DB layer** beyond a value the attacker provides — the classic
  IDOR shape, now seen at the query level. `->update(["has_subscribed" => …])` is a **direct write**
  gated only by that (fuzzy, per §3) match.
- `->first()` issues `select *`, pulling **every column** of a CRM customer (name, NIC, address, …) into
  app memory when the flow needs two fields. Any later serialization to a view/JSON/log turns that into
  a PII disclosure. Select explicit columns.

---

## 9. Priority (DB layer)

1. **DB-5 / F-1** — `APP_DEBUG=false` in prod: stops schema leaks now and prevents credential leakage on the next connection error.
2. **DB-4** — audit the web app's DB grants; give it a **least-privilege** user scoped to only the tables/columns it needs; network-isolate the DB from the internet.
3. **DB-1** — confirm `contact_no` type; compare as a normalized **string**, not an int.
4. **DB-2** — hash `verification_code` instead of `encrypt()`.
5. **DB-3** — reconcile the `merchant_id` schema/code drift; add migration discipline per branch.
6. **DB-6** — enforce ownership before mutating; select explicit columns.

## 10. To turn every **[V]** into **[C]**

Run `toolkit/db_audit.sql` (read-only, metadata + a couple of harmless equality probes) against the
branch DB and paste the output back — or just give me the `CRM_Customer` **migration / `SHOW CREATE
TABLE`** and the model's `$table`/`$fillable`/`$casts`. Either settles the column types, the PII
inventory, the index situation, the DB user's privilege scope, and the shared-CRM question outright.
