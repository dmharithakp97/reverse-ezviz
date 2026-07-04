#!/usr/bin/env bash
# Full reverse-engineering pipeline for a CONSUMER EZVIZ APK (com.ezviz).
#
# I cannot fetch the Play Store APK from this environment (Play + every APK mirror
# is blocked by the egress policy), so YOU supply the binary and this runs the
# complete analysis on it. Get it the clean way from your own device:
#     adb shell pm path com.ezviz         # -> package:/data/app/.../base.apk
#     adb pull <that path> apk/ezviz.apk
# then:  ./analyze_apk.sh apk/ezviz.apk
#
# Produces the same class of artifacts as the SDK analysis, under apk_work/ and apk_analysis/.
set -euo pipefail
cd "$(dirname "$0")"
APK="${1:-$(ls apk/*.apk 2>/dev/null | grep -iv demo | head -1)}"
[ -f "$APK" ] || { echo "No APK. Usage: ./analyze_apk.sh <path.apk>  (see header for adb pull)"; exit 1; }
echo "[*] Target: $APK  ($(stat -c%s "$APK") bytes, sha256 $(sha256sum "$APK" | cut -d' ' -f1))"

D2J="$(ls -d tools/dex2jar-*/ 2>/dev/null | head -1)"
mkdir -p apk_work apk_analysis

echo "[1/6] apktool: manifest, resources, smali"
java -jar tools/apktool.jar d -f -o apk_work/apktool "$APK" >/dev/null 2>&1 || echo "  (apktool warnings ignored)"
cp -f apk_work/apktool/AndroidManifest.xml apk_analysis/ 2>/dev/null || true

echo "[2/6] DEX -> jar (dex2jar) -> Java (CFR)"
( cd apk_work && unzip -oq "../$APK" -d unzip )
for dex in apk_work/unzip/classes*.dex; do
  [ -f "$dex" ] || continue
  b=$(basename "$dex" .dex)
  bash "${D2J}d2j-dex2jar.sh" -f -o "apk_work/$b.jar" "$dex" >/dev/null 2>&1 || true
done
mkdir -p apk_work/src
for jar in apk_work/classes*.jar; do
  [ -f "$jar" ] || continue
  java -jar tools/cfr.jar "$jar" --outputdir apk_work/src --silent true 2>/dev/null || true
done
echo "    decompiled $(find apk_work/src -name '*.java' 2>/dev/null | wc -l) .java files"

echo "[3/6] Permissions, exported components, cleartext, deeplinks"
{ echo "## permissions"; grep -oE 'android:name="[^"]*permission[^"]*"' apk_work/apktool/AndroidManifest.xml | sort -u
  echo; echo "## exported components"; grep -B2 'android:exported="true"' apk_work/apktool/AndroidManifest.xml | grep -oE 'android:name="[^"]*"' | sort -u
  echo; echo "## intent-filter schemes (deeplinks)"; grep -oE 'android:scheme="[^"]*"' apk_work/apktool/AndroidManifest.xml | sort -u
  echo; echo "## cleartext/networkSecurityConfig"; grep -oE 'usesCleartextTraffic="[^"]*"|networkSecurityConfig="[^"]*"' apk_work/apktool/AndroidManifest.xml | sort -u
} > apk_analysis/attack_surface.txt

echo "[4/6] Endpoints, secrets, dangerous sinks in decompiled code"
grep -rhoE 'https?://[a-zA-Z0-9._/-]+|/(api|oauth)/[a-zA-Z0-9._/-]+' apk_work/src 2>/dev/null | sort -u > apk_analysis/endpoints.txt || true
grep -rniE 'addJavascriptInterface|setJavaScriptEnabled|onReceivedSslError|handler\.proceed|Runtime\.getRuntime|System\.load\(|DexClassLoader|TrustManager|checkServerTrusted|ALLOW_ALL_HOSTNAME' apk_work/src 2>/dev/null > apk_analysis/dangerous_sinks.txt || true
# API-key style meta-data (Baidu/Amap/Google Maps/etc.)
grep -oE 'android:name="[^"]*(API_KEY|apikey|lbsapi)[^"]*" *android:value="[^"]*"' apk_work/apktool/AndroidManifest.xml 2>/dev/null > apk_analysis/manifest_api_keys.txt || true
grep -rniE 'api[_-]?key|appSecret|secretKey|AIza[0-9A-Za-z_-]{20,}|baidu|lbsapi|amap|com\.google\.android\.geo' apk_work/apktool/res/values/strings.xml 2>/dev/null > apk_analysis/resource_keys.txt || true

echo "[5/6] Native libraries"
{ printf "%-30s %10s %s\n" LIB SIZE STRIP
  for so in $(find apk_work/unzip/lib -name '*.so' 2>/dev/null | sort); do
    printf "%-30s %10d %s\n" "$(basename "$so")" "$(stat -c%s "$so")" "$(file -b "$so" | grep -o 'not stripped\|stripped')"
  done; } > apk_analysis/native_inventory.txt
for so in $(find apk_work/unzip/lib -name '*.so' 2>/dev/null); do
  readelf -sW "$so" 2>/dev/null | grep -oE 'Java_[A-Za-z0-9_]+'
done | sort -u > apk_analysis/jni_exports.txt || true

echo "[6/6] Third-party SDKs (package fingerprint)"
find apk_work/src -type d 2>/dev/null | sed 's#apk_work/src/##' | grep -E '^(com|io|org|net)/[a-z]+/[a-z]+$' | sort -u > apk_analysis/thirdparty_packages.txt || true

echo "[+] Done. Artifacts in apk_analysis/  (compare against the SDK findings in REPORT.md etc.)"
