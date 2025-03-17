package org.xebia.spdmanager.model.system.fx.subtypes

import org.xebia.spdmanager.model.system.fx.common.*

data class SyncDelay(
    override val fxType: FXType = FXType.SYNC_DELAY,
    val type: DelayType,
    val delayTime: DelayTime,
    val tapTime: Int,
    val lowCut: LowCut,
    val highCut: HighCut,
    val directLevel: Int
): FxEffect(){
    companion object {
        fun fromValues(params: List<Int>): SyncDelay {
            val delayTime = DelayTime.EnumTime(DelayTimeEnum.fromIndex(params[1]))

            return SyncDelay(
                type = DelayType.fromValue(params[0]),
                delayTime = delayTime,
                tapTime = params[2],
                lowCut = LowCut.fromValue(params[3]),
                highCut = HighCut.fromValue(params[4]),
                directLevel = params[5]
            )
        }
    }
}


