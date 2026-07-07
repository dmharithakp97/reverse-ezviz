#!/usr/bin/env bash
# sweep.sh — for each discovered branch subdomain, map exposure to the issues in
#            ../SECURITY_FINDINGS.md and extract its branch (merchant) code.
#
# AUTHORIZED USE ONLY — your own asset. Read-only and non-destructive: it does NOT run any exploit.
# It only (a) checks whether debug mode leaks (F-1), (b) checks the /_ignition/* surface (F-2),
# and (c) reads the subscribe page to extract the merchant_id / chart_id (the "branch code").
#
# Usage: ./sweep.sh out/all_subdomains.txt
set -euo pipefail
LIST="${1:?usage: sweep.sh <file-of-subdomains>}"
UA="Mozilla/5.0 recon"
OUT="out"; mkdir -p "$OUT"
CSV="$OUT/exposure_matrix.csv"
echo "host,https,debug_leak(F-1),ignition_surface(F-2),merchant_id,server" > "$CSV"

probe(){
  local host="$1" base="https://$1"
  local up debug ign mid server
  up=$(curl -sS -o /dev/null -w '%{http_code}' -A "$UA" --max-time 15 "$base/" 2>/dev/null || echo 000)
  [ "$up" = 000 ] && { echo "$host,down,-,-,-,-" >> "$CSV"; echo "  [--] $host unreachable"; return; }
  server=$(curl -sSI -A "$UA" --max-time 15 "$base/" 2>/dev/null | grep -i '^server:' | tr -d '\r' | cut -d' ' -f2- || true)

  # F-1: force an error / read the store endpoint; debug on => framework internals in the body
  local body; body=$(curl -sS -A "$UA" --max-time 20 "$base/e-bill/subscribe/store" 2>/dev/null || true)
  if printf '%s' "$body" | grep -qiE 'Symfony Exception|Illuminate\\|vendor/laravel|APP_DEBUG|Whoops|SQLSTATE'; then
    debug="YES-LEAK"; else debug="no/handled"; fi

  # F-2: existence of Ignition endpoints (NO exploit sent — just a HEAD/GET status)
  local ic; ic=$(curl -sS -o /dev/null -w '%{http_code}' -A "$UA" --max-time 15 "$base/_ignition/health-check" 2>/dev/null || echo 000)
  case "$ic" in 200|405|500) ign="present($ic)";; *) ign="none($ic)";; esac

  # branch code: the subscribe form usually carries the merchant_id/chart_id as a hidden field
  local page; page=$(curl -sS -A "$UA" --max-time 20 "$base/e-bill/subscribe" 2>/dev/null || true)
  mid=$(printf '%s\n%s' "$page" "$body" \
        | grep -oiE '(merchant_id|chart_id|store_id|vec_chart_id)["'\'' :=>]+[0-9]{3,}' \
        | grep -oE '[0-9]{3,}' | head -1)
  [ -z "$mid" ] && mid="?"

  echo "$host,$up,$debug,$ign,$mid,${server:-?}" >> "$CSV"
  printf '  [%s] %-35s debug=%-9s ignition=%-11s merchant_id=%s\n' \
     "$( [ "$debug" = YES-LEAK ] && echo '!!' || echo ok )" "$host" "$debug" "$ign" "$mid"
}

while read -r h; do [ -n "$h" ] && probe "$h"; done < "$LIST"
echo
echo "Matrix -> $CSV"
echo "Rows with debug=YES-LEAK are actively disclosing (F-1); with ignition=present + debug leak are RCE candidates (F-2)."
echo "Collect the merchant_id column -> that's your branch-code map; hand the CSV back to Claude."
