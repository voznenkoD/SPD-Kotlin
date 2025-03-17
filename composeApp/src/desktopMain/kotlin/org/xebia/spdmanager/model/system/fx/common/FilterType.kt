package org.xebia.spdmanager.model.system.fx.common

enum class FilterType(val value: Int) {
    LOW_PASS(0),
    BAND_PASS(1),
    HIGH_PASS(2);

    companion object {
        fun fromValue(value: Int): FilterType {
            return entries.find { it.value == value } ?: LOW_PASS
        }
    }
}