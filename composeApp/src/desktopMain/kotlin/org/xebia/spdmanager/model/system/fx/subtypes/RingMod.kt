package org.xebia.spdmanager.model.system.fx.subtypes

import org.xebia.spdmanager.model.system.fx.common.DryWetMix
import org.xebia.spdmanager.model.system.fx.common.Polarity

data class RingMod(
    override val fxType: FXType = FXType.RINGMOD,
    val polarity: Polarity,
    val lowGain: Float,
    val hiGain: Float,
    val balance: DryWetMix,
    val level: Int
): FxEffect() {
    companion object {
        fun fromValues(params: List<Int>): RingMod {
            return RingMod(
                polarity = Polarity.fromValue(params[0]),
                lowGain = mapGain(params[1]),
                hiGain = mapGain(params[2]),
                balance = DryWetMix.fromInt(params[3]),
                level = params[4]
            )
        }

        private fun mapGain(value: Int): Float {
            return (value - 15).toFloat()
        }
    }
}
