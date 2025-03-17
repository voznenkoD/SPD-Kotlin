package org.xebia.spdmanager.model.system.fx.subtypes

import org.xebia.spdmanager.model.system.fx.common.DelayType
import org.xebia.spdmanager.model.system.fx.common.HighCut
import org.xebia.spdmanager.model.system.fx.common.LowCut
import org.xebia.spdmanager.model.system.fx.common.SyncSwitch

data class StereoDelay(
    override val fxType: FXType = FXType.STEREO_DLY,
    val type: DelayType,
    val syncSW: SyncSwitch,
    val delayTime: Int,
    val tapTime: Int,
    val lowCut: LowCut,
    val highCut: HighCut,
    val directLevel: Int
): FxEffect() {
    companion object {
        fun fromValues(params: List<Int>): StereoDelay {
            return StereoDelay(
                type = DelayType.fromValue(params[0]),
                syncSW = SyncSwitch.fromValue(params[1]),
                delayTime = params[2],
                tapTime = params[3],
                lowCut = LowCut.fromValue(params[4]),
                highCut = HighCut.fromValue(params[5]),
                directLevel = params[6]
            )
        }
    }
}


