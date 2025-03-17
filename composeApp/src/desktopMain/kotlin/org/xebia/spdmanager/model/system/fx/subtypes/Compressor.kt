package org.xebia.spdmanager.model.system.fx.subtypes

import org.xebia.spdmanager.model.system.fx.common.*

data class Compressor(
    override val fxType: FXType = FXType.COMPRESSOR,
    val threshold: Float,
    val attack: Int,
    val release: Int,
    val ratio: Ratio,
    val knee: Knee,
    val makeup: Int
): FxEffect(){
    companion object {
        fun fromValues(params: List<Int>): Compressor {
            return Compressor(
                threshold = mapThreshold(params[0]),
                attack = params[1],
                release = params[2],
                ratio = Ratio.fromValue(params[3]),
                knee = Knee.fromValue(params[4]),
                makeup = params[5]
            )
        }

        private fun mapThreshold(value: Int): Float {
            return (value - 48).toFloat()
        }
    }
}
