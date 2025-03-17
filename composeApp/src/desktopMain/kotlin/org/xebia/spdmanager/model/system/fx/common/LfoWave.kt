package org.xebia.spdmanager.model.system.fx.common

enum class LfoWave(val value: Int) {
    TRIANGLE(0),
    SINE(1),
    SAW(2),
    SQUARE(3);

    companion object {
        fun fromValue(value: Int): LfoWave {
            return entries.find { it.value == value } ?: TRIANGLE
        }
    }
}