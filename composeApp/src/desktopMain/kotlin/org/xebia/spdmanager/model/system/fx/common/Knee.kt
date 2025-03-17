package org.xebia.spdmanager.model.system.fx.common

enum class Knee(val value: Int) {
    SOFT(0),
    HARD(0);

    companion object {
        fun fromValue(value: Int): Knee {
            return entries.find { it.value == value } ?: SOFT
        }
    }

}
