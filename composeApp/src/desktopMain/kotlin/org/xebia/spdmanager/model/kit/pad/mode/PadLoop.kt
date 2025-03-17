package org.xebia.spdmanager.model.kit.pad.mode

enum class PadLoop (val value: Int) {
    OFF(0),
    ON(1),
    X2(2),
    X4(3),
    X8(4);

    companion object {
        fun fromValue(value: Int): PadLoop {
            return entries.find { it.value == value } ?: OFF
        }
    }
}
