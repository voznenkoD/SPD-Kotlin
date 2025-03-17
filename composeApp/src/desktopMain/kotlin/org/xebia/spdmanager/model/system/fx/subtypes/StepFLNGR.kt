package org.xebia.spdmanager.model.system.fx.subtypes

import org.xebia.spdmanager.model.system.fx.common.*

data class StepFLNGR(
    override val fxType: FXType = FXType.STEP_FLNGR,
    val rateSync: SyncSwitch,
    val stepSync: SyncSwitch,
    val depth: Int,
    val manual: Int,
    val resonance: Int,
    val separation: Int,
    val lowCut: LowCut,
    val effectLevel: Int,
    val directLevel: Int
): FxEffect() {
    companion object {
        fun fromValues(params: List<Int>): StepFLNGR {
            return StepFLNGR(
                rateSync = SyncSwitch.fromValue(params[0]),
                stepSync = SyncSwitch.fromValue(params[1]),
                depth = params[2],
                manual = params[3],
                resonance = params[4],
                separation = params[5],
                lowCut = LowCut.fromValue(params[6]),
                effectLevel = params[7],
                directLevel = params[8]
            )
        }
    }
}
