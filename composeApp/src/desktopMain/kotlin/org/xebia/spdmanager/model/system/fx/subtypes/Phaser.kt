package org.xebia.spdmanager.model.system.fx.subtypes

import org.xebia.spdmanager.model.system.fx.common.*

data class Phaser(
    override val fxType: FXType = FXType.PHASER,
    val type: PhaserType,
    val rateSyncSW: SyncSwitch,
    val manual: Int,
    val resonance: Int,
    val separation: Int,
    val effectLevel: Int,
    val directLevel: Int
): FxEffect(){
    companion object {
        fun fromValues(params: List<Int>): Phaser {
            return Phaser(
                type = PhaserType.fromValue(params[0]),
                rateSyncSW = SyncSwitch.fromValue(params[1]),
                manual = params[2],
                resonance = params[3],
                separation = params[4],
                effectLevel = params[5],
                directLevel = params[6]
            )
        }
    }
}



