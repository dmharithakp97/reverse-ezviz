#!/usr/bin/env bash
# Fetch the reverse-engineering toolchain and the EZVIZ SDK artifacts.
# All sources are vendor-published (Maven Central / EZVIZ's own GitHub) — no APK piracy.
set -euo pipefail
cd "$(dirname "$0")"
mkdir -p tools sdk

echo "[*] Toolchain"
[ -f tools/cfr.jar ]     || curl -sSL -o tools/cfr.jar     "https://repo1.maven.org/maven2/org/benf/cfr/0.152/cfr-0.152.jar"
[ -f tools/apktool.jar ] || curl -sSL -o tools/apktool.jar "https://bitbucket.org/iBotPeaches/apktool/downloads/apktool_2.9.3.jar"
[ -d tools/dex2jar-2.0 ] || { curl -sSL -o tools/dex-tools.zip "https://sourceforge.net/projects/dex2jar/files/dex2jar-2.0.zip/download"; (cd tools && unzip -oq dex-tools.zip && rm -f dex-tools.zip && chmod +x dex2jar-2.0/*.sh); }

echo "[*] EZVIZ SDK (official, io.github.ezviz-open:ezviz-sdk)"
VER=5.27.3
BASE="https://repo1.maven.org/maven2/io/github/ezviz-open/ezviz-sdk/$VER"
[ -f sdk/ezviz-sdk-$VER.aar ] || curl -sSL -o sdk/ezviz-sdk-$VER.aar "$BASE/ezviz-sdk-$VER.aar"
[ -f sdk/ezviz-sdk-$VER.pom ] || curl -sSL -o sdk/ezviz-sdk-$VER.pom "$BASE/ezviz-sdk-$VER.pom"

echo "[*] BLE provisioning AAR (from EZVIZ GitHub demo repo)"
[ -f sdk/eziot-ble-release.aar ] || curl -sSL -o sdk/eziot-ble-release.aar \
  "https://raw.githubusercontent.com/Ezviz-Open/EzvizSDK-Android/master/app/libs/eziot-ble-release.aar"

echo "[*] Python helpers"
pip3 install --quiet capstone pyelftools >/dev/null 2>&1 || true

echo "[+] Done. Run ./analyze.sh next."
