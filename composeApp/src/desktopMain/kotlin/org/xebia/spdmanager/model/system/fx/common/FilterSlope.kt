package org.xebia.spdmanager.model.system.fx.common

enum class FilterSlope(val value: Int) {
    MINUS_12(0),
    MINUS_24(1),
    MINUS_36(2);

    companion object {
        fun fromValue(value: Int): FilterSlope {
            return entries.find { it.value == value } ?: MINUS_36
        }
    }
}