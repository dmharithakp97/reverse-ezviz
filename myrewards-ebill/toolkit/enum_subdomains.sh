#!/usr/bin/env bash
# enum_subdomains.sh — discover subdomains of a domain you OWN/ADMINISTER.
#
# AUTHORIZED USE ONLY. You are the IT admin of myrewards.lk, so this is passive recon of your
# own asset. Do not point it at domains you don't control.
#
# Two independent methods (neither needs access to the target servers):
#   1. Certificate Transparency logs (crt.sh) — every TLS cert your subdomains ever got is public.
#      This is the single best passive source and usually finds everything.
#   2. DNS resolution of a wordlist — catches hosts that never got their own public cert
#      (e.g. wildcard-covered or internal names that still resolve).
#
# This sandbox can't run it (egress blocked). Run from your laptop / a machine with internet + DNS.
#
# Usage: ./enum_subdomains.sh myrewards.lk [wordlists/sl_branches.txt]
set -euo pipefail
DOMAIN="${1:?usage: enum_subdomains.sh <domain> [wordlist]}"
WL="${2:-$(dirname "$0")/wordlists/sl_branches.txt}"
OUT="out"; mkdir -p "$OUT"
RESOLVED="$OUT/subdomains_resolved.txt"; : > "$RESOLVED"

echo "== [1] Certificate Transparency (crt.sh) — passive, authoritative =="
# Pull every name CT logs have seen for the domain and its sub-labels.
if command -v jq >/dev/null 2>&1; then
  curl -sS "https://crt.sh/?q=%25.${DOMAIN}&output=json" \
    | jq -r '.[].name_value' 2>/dev/null \
    | tr '[:upper:]' '[:lower:]' | tr ' ' '\n' | sed 's/^\*\.//' \
    | grep -E "\.${DOMAIN//./\\.}$" | sort -u | tee "$OUT/ct_names.txt"
else
  echo "  (install jq for clean parsing) raw dump -> $OUT/ct_raw.txt"
  curl -sS "https://crt.sh/?q=%25.${DOMAIN}&output=json" > "$OUT/ct_raw.txt"
  grep -oE "[a-zA-Z0-9_.-]+\.${DOMAIN//./\\.}" "$OUT/ct_raw.txt" | tr 'A-Z' 'a-z' | sort -u | tee "$OUT/ct_names.txt"
fi

echo
echo "== [2] DNS brute of wordlist against ${DOMAIN} and ebill.${DOMAIN} =="
resolve(){ # prints host if it resolves
  local h="$1"
  if command -v dig >/dev/null 2>&1; then
    [ -n "$(dig +short "$h" A "$h" CNAME 2>/dev/null)" ] && echo "$h"
  else
    getent hosts "$h" >/dev/null 2>&1 && echo "$h"
  fi
}
while read -r label; do
  [ -z "$label" ] && continue
  for base in "$DOMAIN" "ebill.$DOMAIN"; do
    h="${label}.${base}"
    r="$(resolve "$h" || true)"; [ -n "$r" ] && { echo "  [+] $r"; echo "$r" >> "$RESOLVED"; }
  done
done < "$WL"

echo
echo "== Merge =="
cat "$OUT/ct_names.txt" "$RESOLVED" 2>/dev/null | sort -u > "$OUT/all_subdomains.txt"
echo "  -> $OUT/all_subdomains.txt  ($(wc -l < "$OUT/all_subdomains.txt") hosts)"
echo "Next: ./sweep.sh $OUT/all_subdomains.txt   (maps which branches leak / are RCE-candidates)"
