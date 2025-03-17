package org.xebia.spdmanager.model.system.fx.subtypes

import org.xebia.spdmanager.model.system.fx.common.*

data class Reverb(
    override val fxType: FXType = FXType.REVERB,
    val type: ReverbType,
    val reverbTime: Int,
    val preDelay: Int,
    val lowCut: LowCut,
    val highCut: HighCut,
    val density: Int,
    val directLevel: Int,
    val glblRevLvl: Int
): FxEffect() {
    companion object {
        fun fromValues(params: List<Int>): Reverb {
            return Reverb(
                type = ReverbType.fromValue(params[0]),
                reverbTime = params[1],
                preDelay = params[2],
                lowCut = LowCut.fromValue(params[3]),
                highCut = HighCut.fromValue(params[4]),
                density = params[5],
                directLevel = params[6],
                glblRevLvl = params[7]
            )
        }
    }
}



