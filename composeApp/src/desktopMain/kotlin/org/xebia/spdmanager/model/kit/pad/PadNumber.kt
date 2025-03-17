package org.xebia.spdmanager.model.kit.pad

enum class PadNumber (val value: Int) {
    PAD_1(0),
    PAD_2(1),
    PAD_3(2),
    PAD_4(3),
    PAD_5(4),
    PAD_6(5),
    PAD_7(6),
    PAD_8(7),
    PAD_9(8),
    TRIG_1(9),
    TRIG_2(10),
    TRIG_3(11),
    TRIG_4(12),
    FS_1(13),
    FS_2(14);

    companion object {
        fun fromValue(value: Int): PadNumber {
            return entries.find { it.value == value } ?: PAD_1
        }
    }
}