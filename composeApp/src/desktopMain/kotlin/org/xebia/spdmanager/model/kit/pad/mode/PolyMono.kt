package org.xebia.spdmanager.model.kit.pad.mode

enum class PolyMono (val value: Int) {
    MONO(0),
    POLY(1);

    companion object {
        fun fromValue(value: Int): PolyMono {
            return entries.find { it.value == value } ?: MONO
        }
    }
}
