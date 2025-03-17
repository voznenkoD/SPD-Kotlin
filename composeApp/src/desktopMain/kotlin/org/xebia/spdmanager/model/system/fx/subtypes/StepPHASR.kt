package org.xebia.spdmanager.model.system.fx.subtypes

import org.xebia.spdmanager.model.system.fx.common.*

data class StepPHASR(
    override val fxType: FXType = FXType.STEP_PHASR,
    val type: PhaserType,
    val rateSyncSW: SyncSwitch,
    val stepSyncSW: SyncSwitch,
    val depth: Int,
    val manual: Int,
    val resonance: Int,
    val separation: Int,
    val effectLevel: Int,
    val directLevel: Int
): FxEffect() {
    companion object {
        fun fromValues(params: List<Int>): StepPHASR {
            return StepPHASR(
                type = PhaserType.fromValue(params[0]),
                rateSyncSW = SyncSwitch.fromValue(params[1]),
                stepSyncSW = SyncSwitch.fromValue(params[2]),
                depth = params[3],
                manual = params[4],
                resonance = params[5],
                separation = params[6],
                effectLevel = params[7],
                directLevel = params[8]
            )
        }
    }
}



