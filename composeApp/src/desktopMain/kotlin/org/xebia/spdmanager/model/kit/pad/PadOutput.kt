package org.xebia.spdmanager.model.kit.pad

enum class PadOutput (val value: Int) {
    MASTER_OUT(0),
    KIT_FX1(1),
    KIT_FX2(2),
    SUB_OUT(3),
    PHONES_ONLY(4);

    companion object {
        fun fromValue(value: Int): PadOutput {
            return entries.find { it.value == value } ?: MASTER_OUT
        }
    }
}