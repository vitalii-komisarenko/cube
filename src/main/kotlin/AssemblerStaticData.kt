package cube

public class AssemblerStaticData {
    companion object {
        class RegisterInfo(val size: Int, val index: Int)

        @JvmField public val registers = mapOf(
            "%rax" to RegisterInfo(8, 0),
            "%rcx" to RegisterInfo(8, 1),
            "%rdx" to RegisterInfo(8, 2),
            "%rbx" to RegisterInfo(8, 3),
            "%rsi" to RegisterInfo(8, 4),
            "%rdi" to RegisterInfo(8, 5),
            "%rbp" to RegisterInfo(8, 6),
            "%rsp" to RegisterInfo(8, 7),
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
            "%esi" to RegisterInfo(4, 4),
            "%edi" to RegisterInfo(4, 5),
            "%ebp" to RegisterInfo(4, 6),
            "%esp" to RegisterInfo(4, 7),
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
            "%si" to RegisterInfo(2, 4),
            "%di" to RegisterInfo(2, 5),
            "%bp" to RegisterInfo(2, 6),
            "%sp" to RegisterInfo(2, 7),
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
            "%sil" to RegisterInfo(1, 4),
            "%dil" to RegisterInfo(1, 5),
            "%bpl" to RegisterInfo(1, 6),
            "%spl" to RegisterInfo(1, 7),
            "%r8b" to RegisterInfo(1, 8),
            "%r9b" to RegisterInfo(1, 9),
            "%r10b" to RegisterInfo(1, 10),
            "%r11b" to RegisterInfo(1, 11),
            "%r12b" to RegisterInfo(1, 12),
            "%r13b" to RegisterInfo(1, 13),
            "%r14b" to RegisterInfo(1, 14),
            "%r15b" to RegisterInfo(1, 15)
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
    }
}