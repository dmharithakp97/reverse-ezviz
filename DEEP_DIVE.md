# EZVIZ SDK — Deep-Dive Reverse Engineering & Enumeration

An ethical-hacker-style, end-to-end teardown of `ezviz-sdk 5.27.3`: methodology, native protocol
reconstruction, API enumeration with the auth model, a **proven** encrypted-media format (with a working
decryptor), and a structured threat model / attack tree.

Companion to [REPORT.md](REPORT.md) (architecture) and [SECURITY_FINDINGS.md](SECURITY_FINDINGS.md)
(severity-rated bugs). Everything here is **static analysis + local self-test** — no live device, account,
network, or third-party footage was touched. Scope is understanding and defense.

---

## 0. Methodology (the recon workflow)

| Phase | What | Tools here |
|---|---|---|
| Acquire | Pull vendor-published SDK (Maven Central + EZVIZ GitHub) — no APK piracy | `fetch.sh` |
| Unpack | AAR → `classes.jar`, `libs/*.jar`, `jni/*.so`, manifest, assets | `analyze.sh`, unzip |
| Decompile | `classes.jar`/sub-JARs → Java (plain bytecode, no dex2jar needed) | CFR 0.152 |
| Triage | grep endpoints, crypto calls, WebView/exec/loader sinks, secrets | ripgrep |
| Native | ELF triage, JNI export surface, symbol tables, string intel | readelf/nm/strings |
| Disassemble | AArch64 functions (objdump has no arm64 backend here) | capstone (`tools/scripts/`) |
| Prove | Reconstruct wire/file formats, self-test to confirm byte-accuracy | `ezviz_media.py --selftest` |
| Model | Attack tree / STRIDE, rank by exploitability | this doc |

Reproduce any of it with `./fetch.sh && ./analyze.sh`.

---

## 1. Native protocol reconstruction

Full detail in `analysis/protocol_reconstruction.txt`. Two libraries ship **unsymbolized code but keep
their symbol tables** (`libstunClient.so`, `libSystemTransform.so`), which is the break that makes the P2P
stack readable.

### 1.1 Custom STUN / NAT traversal (`ezviz_stunclient::CStunProtocol`)
This is **not RFC 5389 STUN** — EZVIZ rolled their own TLV protocol with CRC32 integrity. Disassembling
`CStunProtocol::ComposeMsgHeader` (`@0x6d310`) — which byte-swaps its four `uint` args with `rev`
(host→network order) around a fixed constant — yields a **32-byte header**:

```
off 0  magic    9E 53 35 E9
off 4  version  01 00 00 00
off 8  BE(arg3)
off 12 00 00 00 00
off 16 BE(arg1)   # message type
off 20 00 00 00 00
off 24 BE(arg2)
off 28 BE(arg4)   # body length
body   TLV attributes (u8 type | len | value) ... + CRC32
```
Supporting methods confirm the shape: `WriteAttribute(u8 type, string val, …)` / `ReadAttribute`,
`ComposeMsgBody`/`ParseMsgBody`, `CheckSum_CRC32`, `GenerateUuid` (transaction id), and the higher-level
`BuildNatReq(_NATREQ*)` / `PaserNatRsp(_NATRSP*)` driving `Stun_GetNATType[_New]`, `Stun_GetNATIP`,
`Stun_GetIpv6NatType`.

### 1.2 SADP LAN discovery & activation (`libsadp.so`) — the biggest LAN surface
SADP rides **UDP multicast** (`CIOMulticast`, Hikvision group `239.255.255.250:37020`, binary
`MulticastProtocol_V31`). Device announces carry `SADPVersion, IPv4, SerialNOEx, MAC, bySupport[..]`
capability bitmaps, `IPv6, dwCmdPort, wHttpPort`. The exported JNI surface shows SADP is not just
discovery — it is **device lifecycle control on the same LAN protocol**:

- `SADP_SendInquiry` / `SADP_InquirySpecificSubnetAllDevice` → **enumerate every camera on the segment**.
- `SADP_ActivateDevice` → **set the admin password on a factory-inactive device** (first-to-activate owns it).
- `SADP_ResetPasswd` / `_V40` / `_V50` / `SADP_ResetDefaultPasswd` → password-reset flows (salt/GUID/security-code).
- `SADP_ModifyDeviceNetParam` / `SADP_SetDeviceConfig` → rewrite a device's IP/network config.

**Implication:** anyone on the L2 segment can inventory cameras (serial, MAC, model caps, open ports) and,
if a unit is still in the factory "inactive" state, activate/own it — the well-known Hikvision SADP surface.
Defenders: segment/VLAN cameras, activate before deployment, block multicast `37020` across trust zones.

### 1.3 Stream session crypto (`libezstreamclient.so`)
`EZVIZECDHCrypter::ezviz_ecdh_generateMasterKey` / `…srvGenerateMasterKey` derive an ECDH master key + salt
that feeds **SRTP** (`srtp_stream_init_all_master_keys`) for A/V. Token requests negotiate it with
`&ecdh=1&devecdh=…&udpecdh=…`. Transport crypto is modern — the weak layer is media-at-rest (§3), not the
live session.

---

## 2. Cloud API enumeration & auth model

~90 endpoints recovered (`analysis/endpoints.txt`). The transport is **OkHttp** (`RestfulUtils`) with a
default client using system CAs — **no certificate pinning** in the Java layer.

**Auth is form-parameter-based, not header-based.** Every authenticated call (`BaseInfo` reflected into a
form body) carries:

| param | meaning |
|---|---|
| `accessToken` / `sessionId` | OAuth bearer (auto-refreshed on error `400902` via `/oauth/token/refreshToken`) |
| `appKey` | developer app id (shipped in the app) |
| `clientType` | `13` (Android SDK) |
| `featureCode` | the persistent random-UUID install id (`md5(md5(hardwareCode))` = `terminalId`) |
| `osVersion`, `clientVersion`, `sdkVersion`, `netType` | client fingerprint |

Endpoint groups worth enumerating (for interop / authorized testing with **your own** token):

- **Identity/OAuth:** `/oauth/authorize`, `/oauth/code`, `/oauth/token/refreshToken`, `/api/user/getUserInfo`.
- **Device lifecycle:** `/api/device/addDevice`, `/deleteDevice`, `/detail`, `/searchDeviceInfo`,
  `/encrypt/set`, `/upgrade`, `/updateDefence`, `/v3/.../clouds/device/active`.
- **Streaming brokerage:** `/api/server/info` (STUN/VTM addrs+ports), `/api/sdk/p2p/dev/info/get`,
  `/api/sdk/p2p/user/info/get`, `/api/service/media/streaming/relay/server`, `/relay/ticket`.
- **Cloud storage / DDNS / messaging / RTC:** `/api/cloud/*`, `/api/lapp/ddns/*`, `/api/message/*`,
  `/api/service/rtc/call/`.

The `/encrypt/set` + verification-code model (§3) is what gates media confidentiality — the cloud brokers
tokens, but the at-rest key never leaves the verify code.

---

## 3. Encrypted media format — reconstructed and **proven**

Reversed from `BaseAPI.decryptData` and implemented as a self-testing tool: `tools/scripts/ezviz_media.py`.

```
hikpic  | [0:6]"hikpic" [6:8]ver [8]encFlag [9:13]len(BE) [13:...]trueData
trueData| [16:48] = ASCII hex of MD5(MD5(verifyCode))   <-- integrity tag AND offline oracle
        | [48:  ] = AES/CBC/PKCS5 ciphertext
key     | verifyCode bytes right-padded with 0x00 to 16  (AES-128)
iv      | 30 31 32 33 34 35 36 37 00 00 00 00 00 00 00 00   (hardcoded "01234567\0…")
```

`--selftest` builds a blob and round-trips it (**PASS**), confirming the format is byte-accurate, and shows
the tag rejects a wrong code instantly. Legitimate uses: decrypt footage **you own** with **your** code;
forensic verification; regression-testing the finding.

```bash
python3 tools/scripts/ezviz_media.py --selftest      # proves the format
python3 tools/scripts/ezviz_media.py --entropy       # H-2 complexity
python3 tools/scripts/ezviz_media.py clip.bin -c ABCDEF -o clip.jpg   # your own footage
```

**Why it's weak (H-2):** the verify code *is* the whole key. With a typical 6-char A–Z code the key space
is `26^6 ≈ 3.1e8 ≈ 2^28`, and the embedded `MD5(MD5(code))` lets an attacker holding the file confirm
guesses **offline** at ~2 MD5/candidate — minutes on commodity hardware. A random per-file key wrapped
server-side would fix it.

---

## 4. Threat model / attack tree

Goal → path → prerequisite → mitigation (severity mirrors SECURITY_FINDINGS.md).

```
GOAL A: Steal a user's account/session token
├─ A1 WebView MITM cert-accept (H-1) ── on-path attacker + victim taps "continue" ── drop proceed(), pin cert
├─ A2 Read token from logcat (M-1) ──── local log access + SDK logging on ────────── never log secrets
└─ A3 Hostile page over bridge (H-1+M-4) ─ A1 first, then JS bridge/injection ─────── host allow-list, no proceed()

GOAL B: Decrypt a user's stored footage
└─ B1 Brute-force verify code (H-2) ──── possess encrypted blob ─────────────────── random key + server wrap
                                          (MD5² oracle makes it offline & fast)

GOAL C: Take over / recon cameras on the LAN
├─ C1 SADP enumerate (§1.2) ─────────── same L2 segment ──────────────────────────── VLAN/segment, block 37020
├─ C2 SADP activate inactive device ─── device in factory state ─────────────────── activate before deployment
└─ C3 SADP modify net params ────────── same L2 segment ──────────────────────────── segment + monitor multicast

GOAL D: Native code execution in the app
└─ D1 System.load(soPath) hijack (M-2) ─ integrator passes world-writable path ──── reject non-private paths

GOAL E: Intercept home Wi-Fi credentials
└─ E1 Sniff SmartConfig/SoundWave (I-3) ─ radio/audio range during onboarding ────── prefer BLE/SoftAP + TLS
```

STRIDE snapshot: **S**poofing → SADP activation (C2); **T**ampering → SADP net params (C3), MITM (A1);
**R**epudiation → n/a; **I**nfo disclosure → B1, A2, E1; **D**oS → out of scope; **E**oP → D1, C2.

---

## 5. Reproduce / verify

```bash
./fetch.sh && ./analyze.sh                       # rebuild everything
python3 tools/scripts/ezviz_media.py --selftest  # prove media format
python3 tools/scripts/disasm.py work/aar/jni/arm64-v8a/libstunClient.so 0x6d310 200   # STUN header
readelf -sW work/aar/jni/arm64-v8a/libsadp.so | grep -oE 'Java_[A-Za-z0-9_]+'         # SADP surface
```

Artifacts: `analysis/protocol_reconstruction.txt`, `analysis/jni_exports.txt`,
`analysis/endpoints.txt`, `analysis/native_inventory.txt`.

*Static reverse engineering of a first-party, Apache-2.0-packaged SDK for interoperability and defensive
understanding. No live systems accessed; the media tooling self-tests on synthetic data only.*
