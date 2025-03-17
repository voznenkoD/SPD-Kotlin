package org.xebia.spdmanager.model.kit.pad.midi

enum class ExternalControl (val value: Int) {
    OFF(0),
    ON(1);

    companion object {
        fun fromValue(value: Int): ExternalControl {
            return entries.find { it.value == value } ?: OFF
        }
    }
}
