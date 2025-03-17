package org.xebia.spdmanager.model.system.fx.common

enum class DelayType(val value: Int) {
    NORMAL(0),
    PAN(1);

    companion object {
        fun fromValue(value: Int): DelayType {
            return entries.find { it.value == value } ?: NORMAL
        }
    }
}
