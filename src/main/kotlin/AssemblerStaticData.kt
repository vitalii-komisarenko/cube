package cube

public class AssemblerStaticData {
    companion object {
        @JvmField public val registers = mapOf(
            "%rax" to RegisterInfo(8, 0),
            "%rcx" to RegisterInfo(8, 1),
            "%rdx" to RegisterInfo(8, 2),
            "%rbx" to RegisterInfo(8, 3),
            "%rsp" to RegisterInfo(8, 4),
            "%rbp" to RegisterInfo(8, 5),
            "%rsi" to RegisterInfo(8, 6),
            "%rdi" to RegisterInfo(8, 7),
            "%r8" to RegisterInfo(8, 8),
            "%r9" to RegisterInfo(8, 9),
            "%r10" to RegisterInfo(8, 10),
            "%r11" to RegisterInfo(8, 11),
            "%r12" to RegisterInfo(8, 12),
            "%r13" to RegisterInfo(8, 13),
            "%r14" to RegisterInfo(8, 14),
            "%r15" to RegisterInfo(8, 15),
            "%eax" to RegisterInfo(4, 0),
            "%ecx" to RegisterInfo(4, 1),
            "%edx" to RegisterInfo(4, 2),
            "%ebx" to RegisterInfo(4, 3),
            "%esp" to RegisterInfo(4, 4),
            "%ebp" to RegisterInfo(4, 5),
            "%esi" to RegisterInfo(4, 6),
            "%edi" to RegisterInfo(4, 7),
            "%r8d" to RegisterInfo(4, 8),
            "%r9d" to RegisterInfo(4, 9),
            "%r10d" to RegisterInfo(4, 10),
            "%r11d" to RegisterInfo(4, 11),
            "%r12d" to RegisterInfo(4, 12),
            "%r13d" to RegisterInfo(4, 13),
            "%r14d" to RegisterInfo(4, 14),
            "%r15d" to RegisterInfo(4, 15),
            "%ax" to RegisterInfo(2, 0),
            "%cx" to RegisterInfo(2, 1),
            "%dx" to RegisterInfo(2, 2),
            "%bx" to RegisterInfo(2, 3),
            "%sp" to RegisterInfo(2, 4),
            "%bp" to RegisterInfo(2, 5),
            "%si" to RegisterInfo(2, 6),
            "%di" to RegisterInfo(2, 7),
            "%r8w" to RegisterInfo(2, 8),
            "%r9w" to RegisterInfo(2, 9),
            "%r10w" to RegisterInfo(2, 10),
            "%r11w" to RegisterInfo(2, 11),
            "%r12w" to RegisterInfo(2, 12),
            "%r13w" to RegisterInfo(2, 13),
            "%r14w" to RegisterInfo(2, 14),
            "%r15w" to RegisterInfo(2, 15),
            "%al" to RegisterInfo(1, 0),
            "%cl" to RegisterInfo(1, 1),
            "%dl" to RegisterInfo(1, 2),
            "%bl" to RegisterInfo(1, 3),
            "%spl" to RegisterInfo(1, 4),
            "%bpl" to RegisterInfo(1, 5),
            "%sil" to RegisterInfo(1, 6),
            "%dil" to RegisterInfo(1, 7),
            "%r8b" to RegisterInfo(1, 8),
            "%r9b" to RegisterInfo(1, 9),
            "%r10b" to RegisterInfo(1, 10),
            "%r11b" to RegisterInfo(1, 11),
            "%r12b" to RegisterInfo(1, 12),
            "%r13b" to RegisterInfo(1, 13),
            "%r14b" to RegisterInfo(1, 14),
            "%r15b" to RegisterInfo(1, 15),
            "%xmm0" to RegisterInfo(16, 0),
            "%xmm1" to RegisterInfo(16, 1),
            "%xmm2" to RegisterInfo(16, 2),
            "%xmm3" to RegisterInfo(16, 3),
            "%xmm4" to RegisterInfo(16, 4),
            "%xmm5" to RegisterInfo(16, 5),
            "%xmm6" to RegisterInfo(16, 6),
            "%xmm7" to RegisterInfo(16, 7),
            "%xmm8" to RegisterInfo(16, 8),
            "%xmm9" to RegisterInfo(16, 9),
            "%xmm10" to RegisterInfo(16, 10),
            "%xmm11" to RegisterInfo(16, 11),
            "%xmm12" to RegisterInfo(16, 12),
            "%xmm13" to RegisterInfo(16, 13),
            "%xmm14" to RegisterInfo(16, 14),
            "%xmm15" to RegisterInfo(16, 15),
        )

        // key: instruction mnemonic
        // value: list of two elements: opcodes for 8-bit and 32-bit relative offsets
        @JvmField public val instructionsWithRelativeAddressesOpcodes = hashMapOf(
            "call" to arrayListOf(
                arrayListOf(), // opcode does not exist
                arrayListOf(0xe8.toByte())
            ),
            "jmp" to arrayListOf(
                arrayListOf(0xeb.toByte()),
                arrayListOf(0xe9.toByte())
            ),
            "jo" to arrayListOf(
                arrayListOf(0x70.toByte()),
                arrayListOf(0x0f.toByte(), 0x80.toByte())
            ),
            "jno" to arrayListOf(
                arrayListOf(0x71.toByte()),
                arrayListOf(0x0f.toByte(), 0x81.toByte())
            ),
            "jb" to arrayListOf(
                arrayListOf(0x72.toByte()),
                arrayListOf(0x0f.toByte(), 0x82.toByte())
            ),
            "jnae" to arrayListOf(
                arrayListOf(0x72.toByte()),
                arrayListOf(0x0f.toByte(), 0x82.toByte())
            ),
            "jc" to arrayListOf(
                arrayListOf(0x72.toByte()),
                arrayListOf(0x0f.toByte(), 0x82.toByte())
            ),
            "jnb" to arrayListOf(
                arrayListOf(0x73.toByte()),
                arrayListOf(0x0f.toByte(), 0x83.toByte())
            ),
            "jae" to arrayListOf(
                arrayListOf(0x73.toByte()),
                arrayListOf(0x0f.toByte(), 0x83.toByte())
            ),
            "jnc" to arrayListOf(
                arrayListOf(0x73.toByte()),
                arrayListOf(0x0f.toByte(), 0x83.toByte())
            ),
            "jz" to arrayListOf(
                arrayListOf(0x74.toByte()),
                arrayListOf(0x0f.toByte(), 0x84.toByte())
            ),
            "je" to arrayListOf(
                arrayListOf(0x74.toByte()),
                arrayListOf(0x0f.toByte(), 0x84.toByte())
            ),
            "jnz" to arrayListOf(
                arrayListOf(0x75.toByte()),
                arrayListOf(0x0f.toByte(), 0x85.toByte())
            ),
            "jne" to arrayListOf(
                arrayListOf(0x75.toByte()),
                arrayListOf(0x0f.toByte(), 0x85.toByte())
            ),
            "jbe" to arrayListOf(
                arrayListOf(0x76.toByte()),
                arrayListOf(0x0f.toByte(), 0x86.toByte())
            ),
            "jna" to arrayListOf(
                arrayListOf(0x76.toByte()),
                arrayListOf(0x0f.toByte(), 0x86.toByte())
            ),
            "jnbe" to arrayListOf(
                arrayListOf(0x77.toByte()),
                arrayListOf(0x0f.toByte(), 0x87.toByte())
            ),
            "ja" to arrayListOf(
                arrayListOf(0x77.toByte()),
                arrayListOf(0x0f.toByte(), 0x87.toByte())
            ),
            "js" to arrayListOf(
                arrayListOf(0x78.toByte()),
                arrayListOf(0x0f.toByte(), 0x88.toByte())
            ),
            "jns" to arrayListOf(
                arrayListOf(0x79.toByte()),
                arrayListOf(0x0f.toByte(), 0x89.toByte())
            ),
            "jp" to arrayListOf(
                arrayListOf(0x7a.toByte()),
                arrayListOf(0x0f.toByte(), 0x8a.toByte())
            ),
            "jpe" to arrayListOf(
                arrayListOf(0x7a.toByte()),
                arrayListOf(0x0f.toByte(), 0x8a.toByte())
            ),
            "jnp" to arrayListOf(
                arrayListOf(0x7b.toByte()),
                arrayListOf(0x0f.toByte(), 0x8b.toByte())
            ),
            "jpo" to arrayListOf(
                arrayListOf(0x7b.toByte()),
                arrayListOf(0x0f.toByte(), 0x8b.toByte())
            ),
            "jl" to arrayListOf(
                arrayListOf(0x7c.toByte()),
                arrayListOf(0x0f.toByte(), 0x8c.toByte())
            ),
            "jnge" to arrayListOf(
                arrayListOf(0x7c.toByte()),
                arrayListOf(0x0f.toByte(), 0x8c.toByte())
            ),
            "jnl" to arrayListOf(
                arrayListOf(0x7d.toByte()),
                arrayListOf(0x0f.toByte(), 0x8d.toByte())
            ),
            "jge" to arrayListOf(
                arrayListOf(0x7d.toByte()),
                arrayListOf(0x0f.toByte(), 0x8d.toByte())
            ),
            "jle" to arrayListOf(
                arrayListOf(0x7e.toByte()),
                arrayListOf(0x0f.toByte(), 0x8e.toByte())
            ),
            "jng" to arrayListOf(
                arrayListOf(0x7e.toByte()),
                arrayListOf(0x0f.toByte(), 0x8e.toByte())
            ),
            "jnle" to arrayListOf(
                arrayListOf(0x7f.toByte()),
                arrayListOf(0x0f.toByte(), 0x8f.toByte())
            ),
            "jg" to arrayListOf(
                arrayListOf(0x7f.toByte()),
                arrayListOf(0x0f.toByte(), 0x8f.toByte())
            )
        )

        @JvmField public val setByteInstructions = hashMapOf(
            "seto" to 0x0f90,
            "setno" to 0x0f91,
            "setb" to 0x0f92,
            "setnae" to 0x0f92,
            "setc" to 0x0f92,
            "setnb" to 0x0f93,
            "setae" to 0x0f93,
            "setnc" to 0x0f93,
            "setz" to 0x0f94,
            "sete" to 0x0f94,
            "setnz" to 0x0f95,
            "setne" to 0x0f95,
            "setbe" to 0x0f96,
            "setna" to 0x0f96,
            "setnb" to 0x0f97,
            "setna" to 0x0f97,
            "sets" to 0x0f98,
            "setns" to 0x0f99,
            "setp" to 0x0f9a,
            "setpe" to 0x0f9a,
            "setnp" to 0x0f9b,
            "setpo" to 0x0f9b,
            "setl" to 0x0f9c,
            "setnge" to 0x0f9c,
            "setnl" to 0x0f9d,
            "setge" to 0x0f9d,
            "setle" to 0x0f9e,
            "setng" to 0x0f9e,
            "setnle" to 0x0f9f,
            "setg" to 0x0f9f,
        )

        @JvmField public val modrmBasedToRegistersNot8Bits = mapOf(
            "mov" to 0x8b,
            "movdqa" to 0x660f6f,
            "pcmpeqd" to 0x660f76,
            "addsubpd" to 0x660fd0,
            "addsubps" to 0xf20fd0,
            "psrlw" to 0x660fd1,
            "psrld" to 0x660fd2,
            "psrlq" to 0x660fd3,
            "paddq" to 0x660fd4,
            "pmullw" to 0x660fd5,
            "psubusb" to 0x660fd8,
            "psubusw" to 0x660fd9,
            "pminub" to 0x660fda,
            "pand" to 0x660fdb,
            "paddusb" to 0x660fdc,
            "paddusw" to 0x660fdd,
            "pmaxub" to 0x660fde,
            "pandn" to 0x660fdf,
            "pavgb" to 0x660fe0,
            "psraw" to 0x660fe1,
            "psrad" to 0x660fe2,
            "pavgw" to 0x660fe3,
            "pmulhuw" to 0x660fe4,
            "pmulhw" to 0x660fe5,
            "cvtpd2dq" to 0xf20fe6,
            "cvttpd2dq" to 0x660fe6,
            "cvtdq2pd" to 0xf30fe6,
            "psubsb" to 0x660fe8,
            "psubsw" to 0x660fe9,
            "pminsw" to 0x660fea,
            "por" to 0x660feb,
            "paddsb" to 0x660fec,
            "paddsw" to 0x660fed,
            "pmaxsw" to 0x660fee,
            "pxor" to 0x660fef,
            "psllw" to 0x660ff1,
            "pslld" to 0x660ff2,
            "psllq" to 0x660ff3,
            "pmuludq" to 0x660ff4,
            "pmaddwd" to 0x660ff5,
            "psadbw" to 0x660ff6,
            "psubb" to 0x660ff8,
            "psubw" to 0x660ff9,
            "psubd" to 0x660ffa,
            "psubq" to 0x660ffb,
            "paddb" to 0x660ffc,
            "paddw" to 0x660ffd,
            "paddd" to 0x660ffe,
        )

        @JvmField public val modrmBasedFromRegistersNot8Bits = mapOf(
            "movaps" to 0x0f29,
        )

        val segmentPrefixes = mapOf(
            "%fs:" to 0x64.toByte(),
            "%gs:" to 0x65.toByte(),
        )

        fun getSegmentOverrideInfo(params: List<String>): Pair<Int?, Byte?> {
            for ((index, param) in params.withIndex()) {
                for ((prefix, encodingByte) in segmentPrefixes) {
                    if (param.startsWith(prefix)) {
                        return Pair(index, encodingByte);
                    }
                }
            }
            return Pair(null, null);
        }

        @JvmStatic
        public fun getSegmentOverrideIndex(params: List<String>): Int? {
            return getSegmentOverrideInfo(params).first;
        }

        @JvmStatic
        public fun getSegmentOverrideEncodingByte(params: List<String>): Byte? {
            return getSegmentOverrideInfo(params).second;
        }
    }
}