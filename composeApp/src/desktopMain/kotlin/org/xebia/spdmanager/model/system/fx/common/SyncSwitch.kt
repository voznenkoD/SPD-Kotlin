package org.xebia.spdmanager.model.system.fx.common

enum class SyncSwitch(val value: Int) {
    OFF(0),
    ON(1);

    companion object {
        fun fromValue(value: Int): SyncSwitch {
            return entries.find { it.value == value } ?: OFF
        }

        fun fromBoolean(value: Boolean): SyncSwitch {
            return if (value) ON else OFF
        }
    }
}