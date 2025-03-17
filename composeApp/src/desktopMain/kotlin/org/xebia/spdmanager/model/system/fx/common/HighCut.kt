package org.xebia.spdmanager.model.system.fx.common

enum class HighCut(val value: Int) {
    HZ_700(0),
    KHZ_1_0(1),
    KHZ_1_4(2),
    KHZ_2_0(3),
    KHZ_3_0(4),
    KHZ_4_0(5),
    KHZ_6_0(6),
    KHZ_8_0(7),
    KHZ_11_0(8),
    FLAT(9);

    companion object {
        fun fromValue(value: Int): HighCut {
            return entries.find { it.value == value } ?: KHZ_6_0
        }
    }
}
