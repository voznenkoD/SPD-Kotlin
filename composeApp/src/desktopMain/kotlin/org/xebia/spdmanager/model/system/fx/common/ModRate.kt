package org.xebia.spdmanager.model.system.fx.common

sealed class ModRate {
    data class EnumRate(val modRateEnum: ModRateEnum) : ModRate()
    data class IntRate(val intRate: Int) : ModRate()
    companion object {
        fun create(rateSyncEnum: SyncSwitch, modRateValueNote: Int, modRateValueMS: Int): ModRate {
            val modRate = if (rateSyncEnum == SyncSwitch.ON) {
                EnumRate(ModRateEnum.fromIndex(modRateValueNote))
            } else {
                IntRate(modRateValueMS)
            }
            return modRate
        }
    }
}

enum class ModRateEnum(val index: Int, val value: String) {
    WHOLE(0, "1"),
    HALF(1, "1/2"),
    QUARTER_DOTTED(2, "1/4."),
    QUARTER(3, "1/4"),
    EIGHT_DOTTED(4, "1/8."),
    QUARTER_TRIPLET(4, "1/4^3"),
    EIGHT(5, "1/8"),
    EIGHTH_TRIPLET(5, "1/8^3"),
    SIXTEENTH(6, "1/16");

    companion object {
        fun fromIndex(index: Int): ModRateEnum {
            return entries.find { it.index == index } ?: WHOLE
        }
    }
}