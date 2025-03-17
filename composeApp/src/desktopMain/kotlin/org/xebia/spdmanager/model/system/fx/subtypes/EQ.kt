package org.xebia.spdmanager.model.system.fx.subtypes

import org.xebia.spdmanager.model.system.fx.common.*

data class EQ(
    override val fxType: FXType = FXType.EQ,
    val lowCut: LowCut,
    val lowGain: Float,
    val pkg1Freq: EqFreq,
    val pkg1Q: EqQ,
    val pkg1Gain: Float,
    val pkg2Freq: EqFreq,
    val pkg2Q: EqQ,
    val pkg2Gain: Float,
    val hiGain: Float,
    val hiCut: HighCut,
    val level: Float
): FxEffect() {
    companion object {
        fun fromValues(params: List<Int>):  EQ {
            return EQ(
                lowCut = LowCut.fromValue(params[0]),
                lowGain = mapEqDecibels(params[1]),
                pkg1Freq = EqFreq.fromIndex(params[2]),
                pkg1Q = EqQ.fromIndex(params[3]),
                pkg1Gain = mapEqDecibels(params[4]),
                pkg2Freq = EqFreq.fromIndex(params[5]),
                pkg2Q = EqQ.fromIndex(params[6]),
                pkg2Gain = mapEqDecibels(params[7]),
                hiGain = mapEqDecibels(params[8]),
                hiCut = HighCut.fromValue(params[9]),
                level = mapEqDecibels(params[10]),
            )
        }

        private fun mapEqDecibels(value: Int): Float {
            return (value - 20).toFloat()
        }
    }
}
