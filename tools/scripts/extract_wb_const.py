import re, sys
from capstone import Cs, CS_ARCH_ARM64, CS_MODE_LITTLE_ENDIAN
from elftools.elf.elffile import ELFFile
path='work/aar/jni/arm64-v8a/libencryptprotect.so'
f=open(path,'rb'); elf=ELFFile(f)
def read_vaddr(va,n):
    for seg in elf.iter_segments():
        if seg['p_type']!='PT_LOAD':continue
        if seg['p_vaddr']<=va<seg['p_vaddr']+seg['p_filesz']:
            f.seek(seg['p_offset']+(va-seg['p_vaddr']));return f.read(n)
    return b''
addr=0xb0c;code=read_vaddr(addr,616)
md=Cs(CS_ARCH_ARM64,CS_MODE_LITTLE_ENDIAN)
# simulate: track reg immediates from `mov wN,#imm`, apply `strb wN,[sp,#off]`
regs={}; stack={}
for insn in md.disasm(code,addr):
    m=insn.mnemonic; o=insn.op_str
    if m=='mov' and o.startswith('w'):
        parts=o.split(', ')
        if len(parts)==2 and parts[1].startswith('#'):
            v=int(parts[1][1:],0) & 0xff
            regs[parts[0]]=v
    elif m=='strb':
        parts=o.split(', ')
        reg=parts[0]
        mm=re.search(r'sp, #(0x[0-9a-f]+)',o)
        if reg in regs and mm:
            stack[int(mm.group(1),16)]=regs[reg]
    elif m=='str' and 'xzr' in o:
        mm=re.search(r'sp, #(0x[0-9a-f]+)',o)
        if mm:
            base=int(mm.group(1),16)
            for i in range(8): stack.setdefault(base+i,0)
# reconstruct contiguous buffer 0x60..0x77
lo,hi=0x60,0x78
buf=bytes(stack.get(i,0) for i in range(lo,hi))
print("embedded const buffer [sp+0x60..0x77]:")
print("  hex :", buf.hex())
print("  ascii:", ''.join(chr(b) if 32<=b<127 else '.' for b in buf))
# rodata at 0xf10 (from adrp x1,#0 ; add x1,x1,#0xf10)
print("\nrodata @0xf10:", read_vaddr(0xf10,48))
print("rodata @0xff8 (GOT-ish):")
