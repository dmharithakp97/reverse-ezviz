# reverse-ezviz

Static reverse-engineering of the **EZVIZ Android SDK** (`io.github.ezviz-open:ezviz-sdk:5.27.3`) — the
engine that powers the EZVIZ camera app: cloud API, P2P/streaming transport, device discovery, and crypto.

Everything here is derived from **vendor-published** artifacts (Maven Central + EZVIZ's own
`Ezviz-Open/EzvizSDK-Android` GitHub repo, Apache-2.0 packaging). No APK piracy, no live systems touched.

## Read this first
**[REPORT.md](REPORT.md)** — the full findings: architecture, ~90 cloud endpoints, the P2P/STUN/CAS/VTM
streaming model, SADP/HCNetSDK device control, the provisioning attack surface, and the crypto analysis
(brute-forceable encrypted-media scheme, static-IV/ECB local caches, and the reversed `libencryptprotect`
white-box).

**[SECURITY_FINDINGS.md](SECURITY_FINDINGS.md)** — severity-rated dangerous network/process/permission
findings with `file:line` evidence: the token-bearing WebView that accepts MITM certs on a user tap,
secrets logged to logcat, `System.load()` of a caller-supplied path, exec/permission surface, and the
defenses that *are* present (ECDH+SRTP streams, no trust-all TLS, no native backdoor).

**[DEEP_DIVE.md](DEEP_DIVE.md)** — ethical-hacker deep dive: reconstructed the custom STUN wire format
(magic `9E5335E9`, 32-byte header) and the SADP LAN activation/enumeration surface from unstripped native
symbols, the cloud API auth model, and a **proven, self-testing** encrypted-media decryptor
(`tools/scripts/ezviz_media.py`) plus a threat model / attack tree.

**[CRACK_ANALYSIS.md](CRACK_ANALYSIS.md)** — exploitability proof for H-2: an offline verify-code recovery
PoC (`tools/scripts/ezviz_verifycode_crack.py`) that recovers the code + decrypts on synthetic data,
measured CPU throughput, GPU/hashcat (mode 2600) scaling showing the 6-char space falls in milliseconds,
and vendor/user fixes. Offline-only, self-tested on synthetic data.

## Layout
```
REPORT.md              Findings write-up (start here)
fetch.sh               Download toolchain + official SDK artifacts (reproducible)
analyze.sh             Unpack AAR, decompile with CFR, dump native metadata
tools/scripts/         AArch64 helpers (capstone): disasm.py, extract_wb_const.py
analysis/              Curated artifacts:
  native_inventory.txt     35 .so per ABI, sizes, strip status
  jni_exports.txt          864 exported Java_* JNI entry points
  endpoints.txt            ~90 cloud REST paths + hosts
  native_strings_curated.txt   ECDH/P2P/VTM/CAS protocol tokens
  encryptprotect_disasm.txt    ENCRYPT_GetKey disassembly
  encryptprotect_const.txt     recovered white-box constant
src/java/              Decompiled EZVIZ-specific Java (main SDK + sadp + EZStreamClient + configwifi + KeyProtect)
```

## Reproduce
```bash
./fetch.sh && ./analyze.sh
```
Requires: JDK (for CFR/apktool), Python 3 + `capstone`/`pyelftools` (installed by `fetch.sh`), binutils.

Large/re-downloadable binaries (the AAR, `.so`, tool jars, and the raw `work/` tree) are git-ignored;
run the scripts to regenerate them.

## Tools
CFR 0.152 · apktool 2.9.3 · capstone 5 · pyelftools · readelf/nm/strings.
CFR decompiles the AAR's `classes.jar` directly (plain Java bytecode — no dex2jar needed).
