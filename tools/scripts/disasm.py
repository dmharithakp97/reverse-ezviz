#!/usr/bin/env python3
"""Minimal AArch64/ARM function disassembler using pyelftools + capstone.
Usage: disasm.py <lib.so> <symbol-or-hexaddr> [num_bytes]
Resolves data referenced by ADRP/ADD pairs and dumps ASCII of referenced .rodata."""
import sys, struct
from elftools.elf.elffile import ELFFile
from capstone import Cs, CS_ARCH_ARM64, CS_ARCH_ARM, CS_MODE_ARM, CS_MODE_LITTLE_ENDIAN

path, target = sys.argv[1], sys.argv[2]
nbytes = int(sys.argv[3]) if len(sys.argv) > 3 else None
f = open(path,'rb'); elf = ELFFile(f)
is64 = elf.elfclass == 64
machine = elf.header.e_machine

# symbol table
addr=None; size=None
for secname in ('.symtab','.dynsym'):
    sec = elf.get_section_by_name(secname)
    if not sec: continue
    for sym in sec.iter_symbols():
        if sym.name == target:
            addr = sym['st_value']; size = sym['st_size']; break
    if addr is not None: break
if addr is None:
    addr = int(target,16)
if nbytes: size = nbytes
if not size: size = 256

# read bytes at vaddr
def read_vaddr(va, n):
    for seg in elf.iter_segments():
        if seg['p_type']!='PT_LOAD': continue
        if seg['p_vaddr'] <= va < seg['p_vaddr']+seg['p_filesz']:
            off = seg['p_offset'] + (va - seg['p_vaddr'])
            f.seek(off); return f.read(n)
    return b''

code = read_vaddr(addr, size)
if is64:
    md = Cs(CS_ARCH_ARM64, CS_MODE_LITTLE_ENDIAN)
else:
    md = Cs(CS_ARCH_ARM, CS_MODE_ARM)
md.detail = False
print(f"# {target} @ 0x{addr:x} size {size}")
adrp_reg={}
for insn in md.disasm(code, addr):
    line=f"0x{insn.address:x}:\t{insn.mnemonic}\t{insn.op_str}"
    # track adrp/add to resolve rodata pointers
    if insn.mnemonic=='adrp':
        try:
            reg,imm=insn.op_str.split(', ')
            adrp_reg[reg]=int(imm,0)
        except: pass
    if insn.mnemonic in ('add','ldr') and ',' in insn.op_str:
        parts=[p.strip() for p in insn.op_str.split(',')]
        base=parts[1] if len(parts)>1 else ''
        if base in adrp_reg and '#' in insn.op_str:
            try:
                off=int(insn.op_str.split('#')[-1].rstrip(']'),0)
                ea=adrp_reg[base]+off
                data=read_vaddr(ea,32)
                asc=''.join(chr(b) if 32<=b<127 else '.' for b in data)
                line+=f"    ; ptr=0x{ea:x} -> '{asc}'"
            except: pass
    print(line)
