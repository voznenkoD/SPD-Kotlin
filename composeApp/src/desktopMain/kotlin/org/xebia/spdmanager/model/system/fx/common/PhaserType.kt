package org.xebia.spdmanager.model.system.fx.common

enum class PhaserType (val value: Int) {
    FOUR_STAGE(0),
    EIGHT_STAGE(1),
    TWELVE_STAGE(2);

    companion object {
        fun fromValue(value: Int): PhaserType {
            return entries.find { it.value == value } ?: FOUR_STAGE
        }
    }
}