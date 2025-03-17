package org.xebia.spdmanager.model.system.fx.subtypes

import org.xebia.spdmanager.model.system.fx.common.*

data class Chorus(
    override val fxType: FXType = FXType.CHORUS,
    val mode: ChorusMode,
    val rate: Int,
    val preDelay: Int,
    val lowCut: LowCut,
    val highCut: HighCut,
    val directLevel: Int
): FxEffect() {
    companion object {
        fun fromValues(params: List<Int>): Chorus {
            return Chorus(
                mode = ChorusMode.fromValue(params[0]),
                rate = params[1],
                preDelay = params[2],
                lowCut = LowCut.fromValue(params[3]),
                highCut = HighCut.fromValue(params[4]),
                directLevel = params[5]
            )
        }
    }
}



