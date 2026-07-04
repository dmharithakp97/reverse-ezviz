# EZVIZ Android SDK — Advanced Security Findings

Deep-dive follow-up to [REPORT.md](REPORT.md), focused on **dangerous network, process, and permission
behaviour**. Every item cites `file:line` evidence from the decompiled `ezviz-sdk 5.27.3` (`work/src/…`)
or a native string/disassembly. Severity reflects real-world exploitability, and I note the mitigating
conditions honestly — several of these need an on-path attacker or a user tap, not a one-click remote.

Static analysis only. Nothing was run against a live device, account, or service.

Legend: 🔴 High · 🟠 Medium · 🟡 Low · ⚪ Info · 🟢 Positive (defense that is present)

---

## 🔴 H-1 — Token-bearing WebView accepts MITM certificates on a user tap (CWE-295 + CWE-749)
**`com.videogo.main.EzvizWebViewActivity`** — SSL error handler (`~L340`), JS bridge (`L197`), URL load (`L203`).

The OAuth/cloud WebView does three risky things at once:

1. `onReceivedSslError()` pops a dialog *"SSL verification is abnormal, whether to continue"* with a
   **"continue" button that calls `handler.proceed()`** — i.e. it will load HTTPS content over an
   invalid / self-signed / attacker-substituted certificate if the user accepts.
2. It carries **live credentials into the page**: the access token, `sessionId`, `appKey`, and
   `featureCode` are POSTed into the loaded URL (`loadUrl()` cases 3/4, `L228–247`), and the OAuth token
   is parsed straight out of the redirect URL (`checkUrlLoading`, `L410–435`).
3. It exposes a JavaScript bridge: `addJavascriptInterface(this, "deviceOperate")` with
   `setJavaScriptEnabled(true)` (`L194–197`), and `shouldOverrideUrlLoading` applies **no host allow-list**
   (`L377`) — the WebView will navigate anywhere.

**Attack:** on-path attacker (rogue Wi-Fi / ARP spoof) serves a fake cert for the cloud host; if the user
taps "continue", the attacker's page loads **with the access token in the POST body** and can drive the
`deviceOperate` bridge → account/camera takeover.
**Mitigating factors (why H, not Critical):** `EzvizWebViewActivity` is **not exported** (no
`intent-filter`, `android:exported` absent → false), so a malicious app cannot launch it directly; the
attack needs network MITM **and** the victim to accept the SSL dialog. **Fix:** remove the `proceed()`
path (call `handler.cancel()` unconditionally) and pin the cloud cert.

## 🔴 H-2 — Encrypted media is offline-brute-forceable (CWE-326/CWE-330)
Cross-reference REPORT §4.1 (`BaseAPI` `~L410–440`). Saved "encrypted" images/clips use `AES/CBC` with
**key = the 6-char device verification code padded to 16 bytes**, a **hardcoded IV** (`01234567\0…`), and
the blob embeds `MD5(MD5(verifyCode))` as a plaintext **offline verification oracle**. Anyone holding an
encrypted file can brute-force the verify-code space with no server contact and decrypt. Listed here
because it is the single most impactful crypto weakness.

---

## 🟠 M-1 — Access token and device verify-code written to logcat (CWE-532)
- `BaseAPI.java:195` → `LogUtil.d(TAG, "Enter setAccessToken: " + accessToken)` — **full bearer token**.
- `BaseAPI.java:389` → `LogUtil.i(TAG, "… checkSum = " + verifyCode …)` — the **device verification code**,
  which *is* the media-encryption key (H-2).
- `EzvizWebViewActivity.java:434` logs the first 5 token chars (truncated — lower risk).

Gated by the SDK log flag (`Config.LOGGING` / `setDebug`), but when enabled these leak to logcat, bug
reports, and any log-reading component. Secrets should never be logged, even at debug level.

## 🟠 M-2 — `System.load()` of a caller-supplied absolute path (CWE-114 / CWE-427)
`EZStreamClient/com/ez/stream/NativeApi.java:41` → `System.load(soPath)`, where `soPath` flows from the
public API `EZOpenSDK.initLib(app, appKey, loadLibraryAbsPath)` (`EZOpenSDK.java:99`). An integrating app
that passes a path on shared/world-writable storage lets an attacker plant a `.so` and gain **native code
execution** in the app process. It is an integrator footgun rather than a default flaw (the default branch
uses `loadLibrary("ezstreamclient")` from the app's private lib dir), but the API invites misuse and should
reject non-private paths.

## 🟠 M-3 — "KeyProtect" is obfuscation, not cryptography
Cross-reference REPORT §4.3. `libencryptprotect.so`'s `ENCRYPT_GetKey` builds a hardcoded constant on the
stack (recovered: `486a68a3…5c`, printable bytes spell **"Hangzhou"**) and Base64-mixes it — no cipher,
no device-bound secret. Any "protected" key is recoverable offline, so it must not be relied on as a trust
boundary.

## 🟠 M-4 — JavaScript injection via unsanitized URL groups
`EzvizWebViewActivity.getRequestParam()` (`L331`) builds
`"javascript:window.deviceOperate.evaluate('" + operate_str + "', …)"` where `operate_str`/`param_id` come
straight from a URL regex (`operate/(.*)\?paramId=(.*)&`, `L388`) with no escaping. A crafted in-page link
can break out of the quotes and inject JS into the bridge call. Confined to the page origin, but combined
with H-1 (hostile page) it widens the bridge attack surface.

---

## 🟡 L-1 — `Runtime.exec("ping … " + ip)` — argument injection (not shell)
`configwifi/…/mixedconfig/NetUtil.java:175` and `EZStreamClient/…/statistics/PingImp.java:220` concatenate
a host/IP into a `ping` command line. `Runtime.exec(String)` tokenises on whitespace with **no shell**, so
classic `;`/`|` command injection does not apply; a value containing spaces could only inject extra `ping`
arguments. Low impact (input is a discovered device IP), but should use the `exec(String[])` array form.

## 🟡 L-2 — Legacy cleartext HTTP endpoints
Plain-HTTP calls remain in code: `http://snap.ys7.com:99`, `http://ip.taobao.com/service/getIpInfo.php`,
`http://city.ip138.com/ip2city.asp` (client IP sent to third parties in the clear). The demo's
`network_security_config.xml` only whitelists cleartext for `192.168.4.1` (the camera SoftAP during
provisioning — a reasonable scoped exception), so on API 28+ the other HTTP calls would be **blocked by
default** unless routed through native code — i.e. likely dead/legacy paths, but worth removing.

## ⚪ I-1 — Persistent random-UUID device identifier
`LocalInfo.getHardwareCodeFromware()` (`L432–505`) generates `UUID.randomUUID()` once and caches it to a
`HardwareCode` file so it survives app data changes; `terminalId = md5(md5(hardwareCode))`. Notably the SDK
does **not** read IMEI/`getDeviceId()` (the `getDeviceId()` hits are serial-number bean getters), so it is
privacy-friendlier than older builds — but the cached UUID is still a stable cross-session tracking ID.

## ⚪ I-2 — Debug/test activities shipped in the demo app
The demo manifest declares `EZTestActivity`, `EZHubDebugActivity`, `EZP2pTestActivity`, `EZTKTokenActivity`,
`EZInterfaceTestActivity`, `EZStreamTypeSettingActivity`. None are exported, and they are demo-only (not in
the SDK), but such surfaces should not ship in a production build.

## ⚪ I-3 — Broadcast Wi-Fi provisioning exposes the home PSK
Cross-reference REPORT §7: SmartConfig/SoundWave transmit SSID + Wi-Fi password over multicast/UDP/audio
during onboarding — observable by anyone in radio/audio range. Inherent to broadcast provisioning.

---

## Dangerous-permission map (demo app)

| Permission | Risk | Why the SDK uses it |
|---|---|---|
| `ACCESS_FINE_LOCATION` / `COARSE` | 🟠 | Wi-Fi scan/BSSID during SoftAP & SmartConfig provisioning |
| `RECORD_AUDIO` | 🟠 | Two-way talk / SoundWave provisioning |
| `CAMERA` | 🟠 | QR-code device pairing |
| `READ_PHONE_STATE` | 🟠 | Network-type/RTC (ERTC module `TelephonyManager`); core SDK does **not** read IMEI |
| `GET_ACCOUNTS` | 🟠 | GCM/push registration |
| `WRITE_EXTERNAL_STORAGE` / `READ_MEDIA_VIDEO` | 🟡 | Saving snapshots/clips |
| `BLUETOOTH_SCAN` / `_CONNECT` | 🟡 | BLE provisioning (`eziot-ble-release.aar`) |
| `CHANGE_WIFI_STATE` / `CHANGE_WIFI_MULTICAST_STATE` | 🟡 | Join camera SoftAP; multicast for SmartConfig/SADP |
| `RECEIVE_BOOT_COMPLETED` / `FOREGROUND_SERVICE` | 🟡 | Persistent push/alarm service |

No `SYSTEM_ALERT_WINDOW`, no `REQUEST_INSTALL_PACKAGES`, no `WRITE_SETTINGS`, no accessibility, no SMS/call
permissions — the set is consistent with a camera app (provisioning + media + push), not over-scoped.

---

## 🟢 What's actually done right
- **No trust-all `TrustManager` / no `ALLOW_ALL_HOSTNAME`** in the Java REST layer — standard TLS (the only
  TLS gap is the WebView dialog, H-1).
- **Live streams use ECDH-derived master keys + SRTP** (`ECDHCryption_GenerateMasterKey`,
  `EZVIZECDHCrypter::ezviz_ecdh_generateMasterKey`, `srtp_*` in `libezstreamclient.so`) — modern transport
  crypto, distinct from the weak media-at-rest scheme.
- **No hardcoded native backdoor**: no `/bin/sh`, telnet, or embedded admin/root credentials found in
  `libhcnetsdk.so` / `libezstreamclient.so`.
- Sensitive components (`EzvizWebViewActivity`, receivers, services) are **not exported**; only the launcher
  `MainActivity` is exported.
- Modern crypto stack (OpenSSL 3.0.17 / mbedTLS) and a scoped network-security config.

## Priority fixes
1. **H-1:** delete the `handler.proceed()` branch and pin the cloud certificate.
2. **M-1:** strip token/verify-code from all log statements.
3. **H-2/M-3:** move media encryption to a random-key + random-IV scheme with the key wrapped server-side;
   stop treating the verify code and KeyProtect output as secrets.
4. **M-2:** have `initLib(...soPath)` reject paths outside the app's private storage.
