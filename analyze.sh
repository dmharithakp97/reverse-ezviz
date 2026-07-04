#!/usr/bin/env bash
# Unpack the SDK AAR, decompile the Java layer with CFR, and dump native metadata.
# Outputs regenerable working data into work/ and curated artifacts into analysis/.
set -euo pipefail
cd "$(dirname "$0")"
VER=5.27.3
ABI=arm64-v8a
mkdir -p work/aar work/src analysis

echo "[*] Extract AAR"
( cd work/aar && unzip -oq ../../sdk/ezviz-sdk-$VER.aar )
NA=work/aar/jni/$ABI

echo "[*] Decompile classes.jar + sub-SDK jars with CFR"
java -jar tools/cfr.jar work/aar/classes.jar --outputdir work/src/main --silent true 2>/dev/null || true
for j in sadp KeyProtect EZStreamClient configwifi HCNetSDK ERTC PlayerSDK; do
  java -jar tools/cfr.jar "work/aar/libs/$j.jar" --outputdir "work/src/$j" --silent true 2>/dev/null || true
done

echo "[*] Native inventory + JNI export surface"
{ printf "%-26s %10s  %s\n" LIB SIZE STRIP
  for so in "$NA"/*.so; do
    printf "%-26s %10d  %s\n" "$(basename "$so")" "$(stat -c%s "$so")" "$(file -b "$so" | grep -o 'not stripped\|stripped')"
  done; } > analysis/native_inventory.txt
{ for so in "$NA"/*.so; do
    ex=$(readelf -sW "$so" 2>/dev/null | grep -oE 'Java_[A-Za-z0-9_]+' | sort -u)
    [ -n "$ex" ] && { echo "## $(basename "$so")"; echo "$ex"; echo; }
  done; } > analysis/jni_exports.txt

echo "[*] Endpoints from decompiled sources"
grep -rhoE 'https?://[a-zA-Z0-9._/-]+|/(api|oauth)/[a-zA-Z0-9._/-]+' work/src/main 2>/dev/null | sort -u > analysis/endpoints.txt

echo "[*] KeyProtect white-box disassembly"
python3 tools/scripts/disasm.py "$NA/libencryptprotect.so" ENCRYPT_GetKey > analysis/encryptprotect_disasm.txt 2>&1 || true

echo "[+] Done. See analysis/ and REPORT.md."
