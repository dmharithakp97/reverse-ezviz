#!/usr/bin/env bash
# recon.sh — passive/black-box recon of a Laravel e-bill tenant.
#
# RUN THIS ONLY AGAINST A HOST YOU ARE AUTHORIZED TO TEST.
# It is read-only and non-destructive: it fingerprints the framework and checks for the
# debug-mode / Ignition misconfiguration documented in ../SECURITY_FINDINGS.md.
#
# NOTE: this sandbox's egress policy blocks the target, so run this from a machine that can
# reach the host (or after reconfiguring the environment's network policy).
#
# Usage: ./recon.sh https://kandy.ebill.myrewards.lk
set -euo pipefail
BASE="${1:?usage: recon.sh https://host}"
UA="Mozilla/5.0 (X11; Linux x86_64) recon"
OUT="out"; mkdir -p "$OUT"
say(){ printf '\n== %s ==\n' "$*"; }

say "Headers (server / powered-by / cookies)"
curl -sS -D - -o /dev/null -A "$UA" "$BASE/" | tee "$OUT/headers.txt" \
  | grep -iE 'server:|x-powered|set-cookie:|x-|via:' || true
# Laravel gives itself away via the `XSRF-TOKEN` + `<app>_session` cookies and no framework header.

say "Framework fingerprint files (existence only, no download of secrets)"
for p in /robots.txt /favicon.ico /mix-manifest.json /build/manifest.json; do
  code=$(curl -sS -o /dev/null -w '%{http_code}' -A "$UA" "$BASE$p"); echo "$code  $p"
done

say "Debug-mode / info-disclosure check (F-1)"
# A 500 that returns an HTML 'Symfony Exception' / 'Illuminate\' body == debug is ON.
curl -sS -A "$UA" "$BASE/e-bill/subscribe/store" -X GET -o "$OUT/maybe_error.html" -w 'HTTP %{http_code}\n' || true
if grep -qiE 'Symfony Exception|Illuminate\\\\|Whoops|APP_DEBUG|vendor/laravel' "$OUT/maybe_error.html" 2>/dev/null; then
  echo "!! DEBUG MODE APPEARS ON — exception details are being served (Critical, F-1)"
fi

say "Ignition presence (F-2 — CVE-2021-3129 surface). Existence check only, NO exploit."
for p in /_ignition/health-check /_ignition/execute-solution; do
  code=$(curl -sS -o /dev/null -w '%{http_code}' -A "$UA" "$BASE$p"); echo "$code  $p"
done
# 200/405/500 on /_ignition/* while debug is on => confirm facade/ignition version < 2.5.2 and patch.

say "Done. Review $OUT/. Do not proceed to exploitation — report findings to the operator."
