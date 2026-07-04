# EZVIZ Android SDK — Reverse-Engineering Report

**Target:** `io.github.ezviz-open:ezviz-sdk:5.27.3` (versionName `v5.27.3`, versionCode `5273`)
**Vendor:** EZVIZ / Hangzhou Ezviz Network (Hikvision affiliate), `open-team@ezvizlife.com`
**Artifact source:** Maven Central + EZVIZ's own GitHub (`Ezviz-Open/EzvizSDK-Android`) — all vendor-published, Apache-2.0 packaging.
**Scope:** Static analysis only. Java decompilation (CFR), native ELF/JNI/symbol/string analysis, and AArch64 disassembly of the key-protection white-box. No devices, accounts, or live services were touched.

> This documents how the shipping SDK is *built*, so an integrator or defender can understand the protocol, crypto, and attack surface. It is not an exploit and contains no stolen credentials.

---

## 1. How the target was obtained (and why it's legitimate)

The full consumer app (`com.ezviz`) is only distributed through stores whose hosts were unreachable from this environment. However, EZVIZ **publishes the entire SDK itself**:

- The core SDK is a Maven Central artifact: `io.github.ezviz-open:ezviz-sdk:5.27.3` (48 MB AAR).
- EZVIZ's public demo repo `Ezviz-Open/EzvizSDK-Android` ships the BLE provisioning AAR and the demo app source.

This AAR contains the *same* native libraries, Java classes, protocol logic, and crypto that ship inside the consumer app — it is the app's engine, minus the UI. So the analysis below reflects the real product, obtained from first-party channels. `./fetch.sh` reproduces the exact download.

---

## 2. Architecture at a glance

```
 App / Demo (com.videogo.*, ezviz.ezopensdk)
   │
   ├── Cloud REST layer ....... com.videogo.openapi.BaseAPI + RestfulUtils
   │        open.ys7.com (CN) / open.ezvizlife.com (global), OAuth openauth.*
   │
   ├── Streaming / P2P ........ com.ez.stream.NativeApi  ──JNI──▶ libezstreamclient.so
   │        LAN-direct → STUN/CAS P2P (v2/v21/v3) → VTM relay      libstunClient / libNPClient / libsrt
   │
   ├── Device discovery ....... com.hikvision.sadp.Sadp  ──JNI──▶ libsadp.so  (SADP LAN protocol)
   │
   ├── Device control ......... com.hikvision.netsdk.HCNetSDK ─JNI▶ libhcnetsdk.so + libHC*.so
   │
   ├── WiFi/BLE provisioning .. com.ezviz.sdk.configwifi.*  (AP / SmartConfig / SoundWave / BLE)
   │
   ├── Key protection ......... com.hikvision.keyprotect.KeyProtect ─JNI▶ libencryptprotect.so
   │
   ├── Media decode / AI ...... libPlayCtrl, libSystemTransform, libMNN (Alibaba MNN inference)
   │
   └── Crypto ................. OpenSSL 3.0.17 (libcrypto/libssl) + mbedTLS (libmbed*)
```

654 classes were recovered from `classes.jar` alone; another ~1,100 across the sub-SDK jars (`HCNetSDK`, `EZStreamClient`, `ERTC`, `configwifi`, …). 35 native `.so` files ship per ABI (`armeabi-v7a`, `arm64-v8a`). See `analysis/native_inventory.txt` and `analysis/jni_exports.txt` for the full surface (864 exported `Java_*` entry points catalogued).

---

## 3. Cloud API

- **Base hosts:** default `https://open.ys7.com` (China) and `https://open.ezvizlife.com` (global); OAuth at `https://openauth.ys7.com` / `https://openauth.ezvizlife.com`; snapshots on `http://snap.ys7.com:99` (**plain HTTP**); telemetry to `log.ys7.com/multistatistics.do` and `test12dclog.ys7.com/statistics.do`. Region is chosen at runtime via `LocalInfo.isPublicServAddr()` matching the host substring.
- **Auth model:** `initLib(app, appKey)` seeds a developer **appKey**; `setAccessToken()` supplies the OAuth bearer token; refresh via `/oauth/token/refreshToken`. `terminalId = md5(md5(hardwareCode))` identifies the install.
- **Endpoint map:** ~90 paths recovered (`analysis/endpoints.txt`), including:
  - Device lifecycle: `/api/device/addDevice`, `/deleteDevice`, `/detail`, `/upgrade`, `/encrypt/set`, `/searchDeviceInfo`.
  - Streaming brokerage: `/api/service/media/streaming/relay/server`, `/relay/ticket`, `/api/sdk/p2p/dev/info/get`, `/api/sdk/p2p/user/info/get`, `/api/server/info` (returns STUN/VTM addresses & ports).
  - Cloud storage, DDNS (`/api/lapp/ddns/*`), messaging, RTC calling.
- **Third-party IP geolocation:** the SDK calls `http://ip.taobao.com/service/getIpInfo.php` and `http://city.ip138.com/ip2city.asp` over plain HTTP — client IP is leaked to non-EZVIZ hosts unencrypted.

---

## 4. Crypto findings (the interesting part)

### 4.1 Encrypted media (images / clips): brute-forceable by design
`com.videogo.openapi.BaseAPI` decrypts EZVIZ "encrypted image" blobs (magic `hikpic` / `hiklittlepic`). The scheme (verified in decompiled source, `BaseAPI` ~L410–440):

- **Key** = the device **verification code** (the short code on the camera label, typically 6 uppercase letters) copied into a 16-byte buffer: `Arrays.copyOf(verifyCode.getBytes(), 16)` — the remaining 10 bytes are zero. No KDF, no salt.
- **IV** = **hardcoded** `{0x30,0x31,0x32,0x33,0x34,0x35,0x36,0x37, 0,0,0,0,0,0,0,0}` (`"01234567\0…"`).
- **Cipher** = `AES/CBC/PKCS5Padding`.
- Bytes `[16:48]` of the blob store `filePwd = MD5(MD5(verifyCode))` as a plaintext hex string — an **integrity tag that doubles as an offline verification oracle**.

**Impact:** anyone holding an encrypted blob can brute-force the 6-char verification code offline — the embedded double-MD5 tells you when you've hit the right code, no server round-trips — and then decrypt. Effective key entropy is the verification-code space, not 128 bits. The static IV also removes semantic security across images sharing a code.

### 4.2 App-layer storage ciphers: static IV / ECB
- `AESCBCCipher` (`com.videogo.util`): `AES/CBC/PKCS5Padding` with a **hardcoded class-constant IV** `"0123456789ABCDEF"`, key = raw `appKey.getBytes()`. Used to encrypt cached device/camera lists (`FileCacheDeviceInfoManager`) and shared-prefs values (`LocalInfo`). Deterministic — identical plaintext ⇒ identical ciphertext.
- `AESCipher`: `AES/ECB/PKCS7Padding` (and bare `"AES"` = ECB) to decrypt URL-safe base64 payloads with `key = appKey`. ECB leaks structure.

Because the "secret" is the developer's **appKey** (shipped in every copy of the app), this is obfuscation of on-device caches, not confidentiality against anyone who has the app.

### 4.3 KeyProtect white-box (`libencryptprotect.so`) — recovered statically
`KeyProtect.ENCRYPT_GetKey(byte[] in, int inLen, byte[] out, int outLen)` is the SDK's "key protection" primitive. The library is 9.5 KB and links only `libc`/`libstdc++` (memcpy/memset/sprintf/strlen/base64) — **no real cipher**. AArch64 disassembly (`analysis/encryptprotect_disasm.txt`) shows it:

1. materialises a hardcoded ~21-byte constant on the stack via inline `mov`/`strb` immediates (no `.rodata` reference, to dodge naive string scans),
2. mixes it with the caller's ≤20-byte input, formats via `%010d`, and Base64-encodes to a 128-byte output.

The reconstructed embedded constant is:
```
486a68a361bf6eb567cd7afe68ca6fde75494b365c
```
whose printable subsequence spells **"Hangzhou"** (EZVIZ/Hikvision's home city). Because the transform is pure client-side obfuscation with a baked-in constant, the "protected" key is fully derivable offline — it raises the bar for a casual reverse engineer but provides no cryptographic secrecy. `tools/scripts/extract_wb_const.py` re-derives the constant from the binary.

### 4.4 Stream-session key exchange: ECDH (native)
Live-stream/talkback sessions negotiate a per-session key via **ECDH inside `libezstreamclient.so`**. The Java `EZEcdhKeyInfo`/`EcdhKeyInfo` carry only base64 public/private key material to/from native; the token request signals it with query flags `&ecdh=1`, `&devecdh=`, `&udpecdh=` (see `analysis/native_strings_curated.txt`). So the *transport* key exchange is modern EC crypto — the weak spots above are the **media-at-rest** and **local-cache** layers, not the live P2P handshake.

---

## 5. Streaming / P2P transport (`libezstreamclient.so`, 7.8 MB)

`InitParam` (in `EZStreamClient.jar`) is the whole connection model in one struct. Connection strategy, in order, with per-path inhibit flags (`EZ_STREAM_DISABLE_DIRECT_INNER/OUTER/P2P/…`):

1. **LAN direct** — `szDevIP` / `szDevLocalIP`, `iDevCmdPort` / `iDevStreamPort`.
2. **P2P hole-punch** — STUN (`szStunIP:iStunPort`, native `Stun_GetNATType[_New]`) + CAS access server (`szCasServerIP:iCasServerPort`). Multiple protocol generations coexist: `CP2PV2Client`, `CP2PV21Client`, `CP2PV3Client`, `CCasP2PClient` (protobuf-framed signaling).
3. **VTM relay fallback** — `szVtmIP:iVtmPort` (`CBavVtmHandle`), used when NAT traversal fails.

Session auth rides on `szStreamToken` (per-session, from `/relay/ticket`), `szPermanetkey` (device permanent stream key), `szClientSession`, and `szHardwareCode`. `libsrt.so` (Secure Reliable Transport) is bundled for low-latency relay. Bandwidth probing is native (`BWCheckManager`, `/api/service/media/bwc`).

---

## 6. Device discovery & control (Hikvision heritage)

- **SADP** (`libsadp.so`, `com.hikvision.sadp.Sadp`): the classic Hikvision LAN discovery/activation protocol. Exported JNI: `SADP_Start_V30/V40`, `SADP_SendInquiry`, `SADP_ActivateDevice`, `SADP_ModifyDeviceNetParam`, `SADP_SetDeviceConfig`. This is how the app finds and first-activates cameras on the local network.
- **HCNetSDK** (`libhcnetsdk.so` + `libHCCore/HCPreview/HCPlayBack/HCVoiceTalk/HCAlarm/…`): the full Hikvision Network SDK, ~349 JNA-bound classes, including EZVIZ-specific `NET_DVR_CreateEzvizUser` / `NET_DVR_ActivateDevice`. This is the on-device control plane (PTZ, playback, alarms, config).

The lineage is explicit: package names are `com.hikvision.*`, and the KeyProtect namespace is `com.hikvision.keyprotect`.

---

## 7. Provisioning attack surface (`com.ezviz.sdk.configwifi`)

Four onboarding channels ship: **AP mode** (phone joins the camera's SoftAP, HTTP to the device), **SmartConfig** (Wi-Fi SSID + password broadcast in multicast/UDP length-encoded frames), **SoundWave** (credentials modulated over audio), and **BLE** (`eziot-ble-release.aar`). SmartConfig and SoundWave transmit the **home Wi-Fi SSID and password** over the air during setup; anyone in radio/audio range during onboarding can observe the provisioning frames. This is a well-known trade-off of broadcast provisioning and applies to most Hikvision-derived cameras.

---

## 8. Hardening / obfuscation posture

- **Java:** the shipped `proguard.txt` (SDK consumer keep-rules) is effectively empty and class/method names in `classes.jar` are **not obfuscated** — full symbolic decompilation succeeded. The demo `build.gradle` enables R8 only for `release`.
- **Native:** all libs are **stripped** except `libSystemTransform.so` and `libstunClient.so`; no packing, standard ELF, so static analysis is straightforward. The only bespoke anti-analysis is KeyProtect's stack-built constant (§4.3), which is trivially defeated.
- **TLS:** OpenSSL 3.0.17 (2025-07) + mbedTLS are current; no evidence of certificate pinning in the Java REST layer (pinning, if any, would be in native). Some non-stream traffic (snapshots, IP geolocation) is plain HTTP.

---

## 9. Notable weaknesses (summary for a defender)

| # | Finding | Where | Consequence |
|---|---------|-------|-------------|
| 1 | Encrypted media key = 6-char verify code + hardcoded IV, with embedded MD5(MD5(code)) oracle | `BaseAPI` §4.1 | Offline brute-force → decrypt saved encrypted images/clips |
| 2 | Local caches encrypted with static-IV AES-CBC / ECB keyed by shipped appKey | `AESCBCCipher`, `AESCipher` §4.2 | On-device cache confidentiality is cosmetic |
| 3 | "KeyProtect" is client-side obfuscation with a baked-in constant, no cipher | `libencryptprotect.so` §4.3 | Protected key fully recoverable statically |
| 4 | Plain-HTTP snapshot + third-party IP-geolocation calls | §3 | Metadata/IP leakage, MITM of snapshots |
| 5 | Broadcast provisioning (SmartConfig/SoundWave) exposes Wi-Fi PSK | §7 | Home Wi-Fi creds observable during onboarding |
| 6 | No app-layer TLS pinning observed | §8 | Easier network interception for analysis/MITM |

Strengths worth noting: live-stream sessions use ECDH per-session keys; the TLS stack is modern; the device control plane is token-gated through the cloud broker.

---

## 10. Reproduce

```bash
./fetch.sh      # download CFR/apktool + the official ezviz-sdk AAR from Maven Central
./analyze.sh    # unpack, decompile with CFR, dump native JNI/inventory/endpoints
```
Artifacts land in `analysis/`; curated decompiled sources are committed under `src/java/`.
Tooling: **CFR 0.152** (Java decompiler, direct on AAR `classes.jar`), **apktool 2.9.3**, **capstone 5** + **pyelftools** (`tools/scripts/disasm.py`, `extract_wb_const.py`) for AArch64. `objdump` in the base image lacks an AArch64 backend, hence the capstone helper.

*Static analysis of a first-party, Apache-2.0-packaged SDK for interoperability and security understanding. No live systems were accessed.*
