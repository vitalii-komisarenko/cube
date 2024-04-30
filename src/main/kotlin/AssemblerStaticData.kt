package cube

public class AssemblerStaticData {
    // key: instruction mnemonic
    // value: list of two elements: opcodes for 8-bit and 32-bit relative offsets
    companion object {
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