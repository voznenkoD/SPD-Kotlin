package org.xebia.spdmanager.model.kit.pad.mode

enum class TrigType (val value: Int) {
    SHOT(0),
    ALT(1);

    companion object {
        fun fromValue(value: Int): TrigType {
            return entries.find { it.value == value } ?: SHOT
        }
    }
}
