package org.xebia.spdmanager.model.system.fx.common

enum class HighCut(val value: Int, val displayName: String) {
    HZ_700(0, "700"),
    KHZ_1_0(1, "1k"),
    KHZ_1_4(2, "1.4k"),
    KHZ_2_0(3, "2k"),
    KHZ_3_0(4, "3k"),
    KHZ_4_0(5, "4k"),
    KHZ_6_0(6, "6k"),
    KHZ_8_0(7, "8k"),
    KHZ_11_0(8, "11k"),
    FLAT(9, "Flat");

    override fun toString() = displayName

    companion object {
        fun fromValue(value: Int): HighCut {
            return entries.find { it.value == value } ?: KHZ_6_0
        }
    }
}
