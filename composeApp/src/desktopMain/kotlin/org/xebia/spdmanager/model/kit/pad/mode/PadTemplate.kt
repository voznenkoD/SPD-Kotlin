package org.xebia.spdmanager.model.kit.pad.mode

enum class PadTemplate (val value: Int) {
    NONE(-1),
    SINGLE(0),
    PHRASE(1),
    LOOP(2);

    companion object {
        fun fromValue(value: Int): PadTemplate {
            return entries.find { it.value == value } ?: SINGLE
        }
    }
}
