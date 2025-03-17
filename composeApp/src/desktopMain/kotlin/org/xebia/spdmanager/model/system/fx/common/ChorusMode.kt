package org.xebia.spdmanager.model.system.fx.common

enum class ChorusMode(val value: Int) {
    MONO(0),
    STEREO_1(1),
    STEREO_2(2);


    companion object {
        fun fromValue(value: Int): ChorusMode {
            return entries.find { it.value == value } ?: MONO
        }
    }
}
