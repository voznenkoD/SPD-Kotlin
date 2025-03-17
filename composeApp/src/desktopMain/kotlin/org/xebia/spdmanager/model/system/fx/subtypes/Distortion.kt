package org.xebia.spdmanager.model.system.fx.subtypes

import org.xebia.spdmanager.model.system.fx.common.DistortionType

data class Distortion(
    override val fxType: FXType = FXType.DISTORTION,
    val type: DistortionType,
    val drive: Int,
    val bottom: Float,
    val tone: Float,
    val effectLevel: Int
): FxEffect(){
    companion object {
        fun fromValues(params: List<Int>): Distortion {
            return Distortion(
                type = DistortionType.fromValue(params[0]),
                drive = params[1],
                bottom = mapTone(params[2]),
                tone = mapTone(params[3]),
                effectLevel = params[4]
            )
        }

        private fun mapTone(value: Int): Float {
            return (value - 50).toFloat()
        }
    }
}
