package org.xebia.spdmanager.model.system.fx.mainTypes

import org.xebia.spdmanager.model.system.fx.common.*

data class DelayEffect(
    val preset: DelayPreset,
    val type: DelayType,
    val syncSW: SyncSwitch,
    val delayTime: DelayTime,
    val tapTime: Int,
    val lowCut: LowCut,
    val highCut: HighCut,
    val directLevel: Int
) {
    companion object {
        fun fromValues(
            preset:Int, type: Int, syncSW: Int, delayTimeMS: Int, delayTimeNote: Int,
            tapTime: Int, lowCut: Int, highCut: Int, directLevel: Int
        ): DelayEffect {

            val delayTime = if (SyncSwitch.fromValue(syncSW) == SyncSwitch.ON) {
                DelayTime.EnumTime(DelayTimeEnum.fromIndex(delayTimeNote))
            } else {
                DelayTime.IntTime(delayTimeMS)
            }

            return DelayEffect(
                preset = DelayPreset.fromValue(preset),
                type = DelayType.fromValue(type),
                syncSW = SyncSwitch.fromValue(syncSW),
                delayTime = delayTime,
                tapTime = tapTime,
                lowCut = LowCut.fromValue(lowCut),
                highCut = HighCut.fromValue(highCut),
                directLevel = directLevel
            )
        }
    }
}

enum class DelayPreset(val value: Int) {
    STEREO_NORMAL(0),
    STEREO_SYNC(1),
    PAN_QUARTER(2),
    PAN_DOTTED_EIGHT(3),
    PAN_DOUBLE(4);

    companion object {
        fun fromValue(value: Int): DelayPreset {
            return entries.find { it.value == value } ?: STEREO_NORMAL
        }
    }
}
