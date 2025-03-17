package org.xebia.spdmanager.model.system.fx.subtypes

import org.xebia.spdmanager.model.system.fx.common.*

data class Flanger(
    override val fxType: FXType = FXType.FLANGER,
    val rateSync: SyncSwitch,
    val manual: Int,
    val resonance: Int,
    val separation: Int,
    val lowCut: LowCut,
    val effectLevel: Int,
    val directLevel: Int
): FxEffect() {
    companion object {
        fun fromValues(params: List<Int>):  Flanger {
            return Flanger(
                rateSync = SyncSwitch.fromValue(params[0]),
                manual = params[1],
                resonance = params[2],
                separation = params[3],
                lowCut = LowCut.fromValue(params[4]),
                effectLevel = params[5],
                directLevel = params[6]
            )
        }
    }
}
