package org.xebia.spdmanager.model.system.fx.mainTypes

import org.xebia.spdmanager.model.system.fx.common.SyncSwitch

data class SLoopEffect(
    val preset: SLoopPreset,
    val mode: SLoopMode,
    val rateSync: SyncSwitch,
    val rate: SLoopRate,
    val timing: SLoopTiming
) {
    companion object {
        fun fromValues(
            preset: Int, mode: Int, rateSync: Int,
            rateValueMS: Int, rateValueNote: Int, timing: Int
        ): SLoopEffect {
            val sLoopPresetEnum = SLoopPreset.fromValue(preset)
            val sLoopModeEnum = SLoopMode.fromValue(mode)
            val rateSyncEnum = SyncSwitch.fromValue(rateSync)
            val timingEnum = SLoopTiming.fromValue(timing)

            val rate = if (rateSyncEnum == SyncSwitch.ON) {
                SLoopRate.EnumRate(SLoopRateEnum.fromIndex(rateValueNote))
            } else {
                SLoopRate.IntRate(rateValueMS)
            }

            return SLoopEffect(
                preset = sLoopPresetEnum,
                mode = sLoopModeEnum,
                rateSync = rateSyncEnum,
                rate = rate,
                timing = timingEnum
            )
        }
    }
}

sealed class SLoopRate {
    data class EnumRate(val rateEnum: SLoopRateEnum) : SLoopRate()
    data class IntRate(val intRate: Int) : SLoopRate()
}

enum class SLoopRateEnum(val index: Int, val value: String) {
    ONE(0, "1"),
    HALF(1, "1/2"),
    QUARTER_DOTTED(2, "1/4."),
    QUARTER(3, "1/4"),
    EIGHTH_DOTTED(4, "1/8."),
    QUARTER_TRIPLET(5, "1/4^3"),
    EIGHTH(6, "1/8"),
    EIGHTH_TRIPLET(7, "1/8^3"),
    SIXTEENTH(8, "1/16");

    companion object {
        fun fromIndex(index: Int): SLoopRateEnum {
            return entries.find { it.index == index } ?: QUARTER_TRIPLET
        }
    }
}

enum class SLoopPreset(val value: Int) {
    MANUAL(0),
    AUTO_QUARTER(1),
    AUTO_EIGHT(2),
    AUTO_SIXTEEN(3),
    AUTO_FREERUN(4);

    companion object {
        fun fromValue(value: Int): SLoopPreset {
            return entries.find { it.value == value } ?: MANUAL
        }
    }
}

enum class SLoopMode(val value: Int) {
    MANUAL(0),
    AUTO(1);

    companion object {
        fun fromValue(value: Int): SLoopMode {
            return entries.find { it.value == value } ?: AUTO
        }
    }
}

enum class SLoopTiming(val value: Int) {
    FIRST_HALF(0),
    SECOND_HALF(1);

    companion object {
        fun fromValue(value: Int): SLoopTiming {
            return entries.find { it.value == value } ?: SECOND_HALF
        }
    }
}
