package org.xebia.spdmanager.model.system.fx.common

enum class Ratio(val value: Int, val description: String) {
    TWO(0, "2:1"),
    THREE(0, "3:1"),
    FOUR(0, "4:1"),
    EIGHT(0, "8:1"),
    HUNDRED(0, "100:1");

    companion object {
        fun fromValue(value: Int): Ratio {
            return entries.find { it.value == value } ?: TWO
        }
    }

}
