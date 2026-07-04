#!/usr/bin/env python3
"""
EZVIZ encrypted-media format parser / decryptor  —  interoperability & forensics tool.

Reconstructed by static reverse engineering of ezviz-sdk 5.27.3
(com.videogo.openapi.BaseAPI.decryptData, ~L385-440). Lets an owner decrypt
their OWN encrypted snapshots/clips with THEIR OWN device verification code, and
demonstrates the design weakness documented in SECURITY_FINDINGS.md (H-2).

Wire format (cryptType == 1, magic "hikpic"):
    [0:6]   "hikpic"
    [6:8]   uint16 version
    [8:9]   encrypt flag
    [9:13]  uint32 payload length (big-endian, custom getLength)
    [13:...] encrypted "true data", of which:
        trueData[16:48] = ASCII hex of MD5(MD5(verifyCode))   <- integrity + OFFLINE ORACLE
        trueData[48:]   = AES-256? no: AES/CBC/PKCS5 ciphertext
    key = verifyCode.getBytes() right-padded with 0x00 to 16 bytes   (AES-128)
    iv  = 30 31 32 33 34 35 36 37 00 00 00 00 00 00 00 00   ("01234567\\0..") HARDCODED
    "hiklittlepic": length at [14:18], data at [18:]

This file is self-contained: `--selftest` builds a valid blob in-memory and
round-trips it, so the format is proven without touching anyone's real footage.
"""
import sys, struct, hashlib, argparse
from Crypto.Cipher import AES

HARDCODED_IV = bytes([0x30,0x31,0x32,0x33,0x34,0x35,0x36,0x37,0,0,0,0,0,0,0,0])

def md5hex(b: bytes) -> str:
    return hashlib.md5(b).hexdigest()

def double_md5_tag(verifycode: str) -> str:
    """filePwd stored in the blob = MD5(MD5(verifyCode)) as lowercase hex (32 chars)."""
    inner = md5hex(verifycode.encode())            # MD5Util.getMD5String(verifyCode)
    return md5hex(inner.encode())                  # MD5Util.getMD5String(<hex string>)

def key_from_code(verifycode: str) -> bytes:
    """secretKey = Arrays.copyOf(verifyCode.getBytes(), 16) — pad/trunc to 16 bytes."""
    raw = verifycode.encode()
    return (raw + b"\x00" * 16)[:16]

def _getlen_be(b: bytes) -> int:
    return struct.unpack(">I", b)[0]

def parse_header(data: bytes):
    if data[:6] == b"hikpic":
        version = struct.unpack(">H", data[6:8])[0]
        enc     = data[8]
        plen    = _getlen_be(data[9:13])
        return {"magic":"hikpic","version":version,"enc":enc,
                "payload_len":plen,"true_data":data[13:13+plen]}
    if data[:12] == b"hiklittlepic":
        plen = _getlen_be(data[14:18])
        return {"magic":"hiklittlepic","payload_len":plen,"true_data":data[18:18+plen]}
    # cryptType != image container: whole buffer is the true data
    return {"magic":None,"true_data":data}

def decrypt(data: bytes, verifycode: str):
    hdr = parse_header(data)
    td = hdr["true_data"]
    if td is None or len(td) <= 48:
        raise ValueError("true data too short / not encrypted")
    stored_tag = td[16:48].decode(errors="replace")
    expect_tag = double_md5_tag(verifycode)
    tag_ok = (stored_tag.lower() == expect_tag.lower())
    cipher = AES.new(key_from_code(verifycode), AES.MODE_CBC, HARDCODED_IV)
    pt = cipher.decrypt(td[48:])
    pad = pt[-1]                                    # PKCS5 unpad
    if 1 <= pad <= 16:
        pt = pt[:-pad]
    return {"header":hdr,"tag_stored":stored_tag,"tag_expected":expect_tag,
            "verifycode_correct":tag_ok,"plaintext":pt}

def build_blob(plaintext: bytes, verifycode: str, version=1) -> bytes:
    """Inverse of decrypt(): produce a valid 'hikpic' blob (used by --selftest)."""
    tag = double_md5_tag(verifycode).encode()       # 32 ascii hex bytes
    prefix16 = b"\x00" * 16                          # trueData[0:16] (unused by decrypt)
    padlen = 16 - (len(plaintext) % 16)
    padded = plaintext + bytes([padlen]) * padlen
    ct = AES.new(key_from_code(verifycode), AES.MODE_CBC, HARDCODED_IV).encrypt(padded)
    true_data = prefix16 + tag + ct
    body = b"hikpic" + struct.pack(">H", version) + b"\x01" + struct.pack(">I", len(true_data)) + true_data
    return body

def entropy_report(charset_size=26, code_len=6):
    """Quantify H-2: the verify code is the whole key. The double-MD5 tag is a
    free offline oracle, so the search cost is ~2 MD5 ops per candidate."""
    space = charset_size ** code_len
    print("=== verify-code brute-force complexity (H-2) ===")
    print(f"assumed charset size : {charset_size} (e.g. A-Z)")
    print(f"assumed code length  : {code_len}")
    print(f"key search space     : {charset_size}^{code_len} = {space:,}  (~2^{space.bit_length()-1})")
    print(f"work per candidate    : 2x MD5 (oracle) then 1 AES block to confirm")
    print(f"note                  : effective key entropy is the code, NOT 128 bits;")
    print(f"                        the embedded MD5(MD5(code)) tag confirms hits offline.")

def main():
    ap = argparse.ArgumentParser(description="EZVIZ encrypted-media parser/decryptor")
    ap.add_argument("file", nargs="?", help="encrypted .bin/.jpg blob")
    ap.add_argument("-c","--code", help="YOUR device verification code")
    ap.add_argument("-o","--out", help="write decrypted plaintext here")
    ap.add_argument("--selftest", action="store_true", help="prove the format in-memory")
    ap.add_argument("--entropy", action="store_true", help="print H-2 complexity report")
    a = ap.parse_args()

    if a.entropy:
        entropy_report(); return
    if a.selftest:
        code, msg = "ABCDEF", b"\xff\xd8\xff\xe0 JPEG-ish secret frame " * 3
        blob = build_blob(msg, code)
        r = decrypt(blob, code)
        ok = r["verifycode_correct"] and r["plaintext"] == msg
        print("built blob :", blob[:13].hex(), "... len", len(blob))
        print("magic      :", r["header"]["magic"], "version", r["header"].get("version"))
        print("MD5^2 tag   :", r["tag_stored"], "(match:", r["verifycode_correct"], ")")
        print("round-trip  :", "PASS" if ok else "FAIL")
        # wrong-code demo: tag mismatch flags it instantly (the oracle)
        w = decrypt(blob, "ZZZZZZ")
        print("wrong code  : tag match =", w["verifycode_correct"], "(oracle rejects instantly)")
        sys.exit(0 if ok else 1)
    if not a.file or not a.code:
        ap.error("need FILE and --code (or use --selftest / --entropy)")
    data = open(a.file,"rb").read()
    r = decrypt(data, a.code)
    print("magic:", r["header"]["magic"], "| verifycode correct:", r["verifycode_correct"])
    if a.out:
        open(a.out,"wb").write(r["plaintext"]); print("wrote", a.out, len(r["plaintext"]), "bytes")

if __name__ == "__main__":
    main()
