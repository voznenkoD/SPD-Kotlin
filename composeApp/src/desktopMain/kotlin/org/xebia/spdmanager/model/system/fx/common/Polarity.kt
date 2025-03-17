package org.xebia.spdmanager.model.system.fx.common

enum class Polarity(val value: Int) {
    DOWN(0),
    UP(1);


    companion object {
        fun fromValue(value: Int): Polarity {
            return entries.find { it.value == value } ?: DOWN
        }
    }
}
