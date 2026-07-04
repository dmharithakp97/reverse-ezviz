# H-2 Exploitability: Verification-Code Recovery (offline crack analysis)

Proof that the EZVIZ encrypted-media weakness (H-2) is **practically**, not just theoretically, breakable —
and the numbers a defender needs to judge the risk. Everything here is offline computation on a file; the
PoC self-tests on synthetic data and touches no device, account, or network.

**Tools:** `tools/scripts/ezviz_verifycode_crack.py` (recovery PoC) + `ezviz_media.py` (format/decrypt).
**Legitimate use:** recover the code for footage *you own* when it's lost; validate the finding for a
disclosure report. Not for third-party data you don't own.

---

## 1. Why it cracks: the embedded oracle

Encrypted media derives its AES-128 key directly from the device **verification code** (the short code on
the camera label), and every blob stores `MD5(MD5(verifyCode))` at `trueData[16:48]`. That stored tag is a
**self-contained offline oracle**:

```
for each candidate code:
    if MD5( hex(MD5(candidate)) ) == stored_tag:   # 2 cheap MD5s, no AES, no network
        key = candidate.padTo16(); return candidate  # confirmed
```

The attacker needs **only the file**. No live device, no server round-trip, no rate limit, no lockout —
the check is two MD5 ops per guess, and AES runs exactly once at the end to emit plaintext. Effective key
entropy is the *code*, not 128 bits.

## 2. Keyspace

| Assumed code alphabet | Size | log2 |
|---|---|---|
| 6 × A–Z (typical label code) | 26⁶ = 308,915,776 | ~2²⁸ |
| 6 × A–Z0–9 | 36⁶ ≈ 2.2 × 10⁹ | ~2³¹ |
| 6 × A–Za–z0–9 | 62⁶ ≈ 5.7 × 10¹⁰ | ~2³⁶ |

Even the largest realistic alphabet is a rounding error for a GPU.

## 3. Measured vs extrapolated throughput

Run in this container (unoptimized **pure-Python** double-MD5, 4 vCPU):

```
single-core double-MD5 : ~1,046,000 H/s
est. 4-core CPU        : ~4,186,000 H/s
keyspace 26^6 (2^28)   : 308,915,776
worst-case, this CPU   : ~74 s        (pure Python — a naive floor)
worst-case, mid GPU    : ~39 ms       (8 GH/s)
worst-case, high GPU   : ~8 ms        (40 GH/s, hashcat -m 2600)
```

The end-to-end self-test recovered a 6-char code and decrypted the frame in **0.52 s** (the true code sat
~494k candidates in) — and that's slow Python. Optimized C/OpenCL is 3–4 orders of magnitude faster; the
honest takeaway is **the whole A–Z space falls in well under a second on any modern GPU**.

## 4. GPU offload — it's a stock hashcat mode

`md5(md5($pass))` is **hashcat mode 2600**, so no custom kernel is needed:

```bash
python3 tools/scripts/ezviz_verifycode_crack.py --hashcat clip.bin   # writes ezviz_h2.hash
hashcat -m 2600 -a 3 ezviz_h2.hash ?u?u?u?u?u?u                       # ?u = A-Z
# then:
python3 tools/scripts/ezviz_media.py clip.bin -c <RECOVERED> -o clip.jpg
```

Swap the mask for the code alphabet (`?a`, `?d`, …). On a single high-end GPU (tens of GH/s on MD5) the
2²⁸–2³⁶ spaces above complete in milliseconds to a few minutes.

## 5. Reproduce

```bash
python3 tools/scripts/ezviz_verifycode_crack.py --selftest   # recover synthetic code + decrypt (PASS)
python3 tools/scripts/ezviz_verifycode_crack.py --bench      # throughput + full-space time on your box
python3 tools/scripts/ezviz_verifycode_crack.py --hashcat <blob>   # emit GPU hashfile+mask
```

## 6. Impact & fix

**Impact:** anyone who obtains an encrypted blob (backup, cloud object, shared file, forensic image) can
recover the verify code offline and decrypt every clip/snapshot protected by that same code — the
"encryption" adds only the delay of a sub-second search. It also *discloses the verification code itself*,
which gates other device operations.

**Fix (vendor):**
1. Encrypt media with a **random per-file key**, wrapped by a server-held key or a proper KDF over a
   high-entropy device secret — never the 6-char code directly.
2. Use a **random IV per file** (current IV is hardcoded `01234567\0…`).
3. Remove the `MD5(MD5(code))` tag, or replace it with an HMAC that isn't a plaintext-recoverable oracle;
   if integrity is needed, derive the MAC key separately (encrypt-then-MAC).
4. Longer / higher-entropy verification codes as defense-in-depth (does not fix the design, only widens
   the search).

**Mitigation (user, today):** treat exported/backed-up encrypted footage as effectively unprotected;
don't rely on the on-camera "encryption" for confidentiality of shared or cloud-stored clips.

---
*Offline PoC for a disclosed cryptographic weakness, self-tested on synthetic data. No live systems,
accounts, or third-party footage were involved. See SECURITY_FINDINGS.md (H-2) and DEEP_DIVE.md §3.*
