package org.xebia.spdmanager.model.system.fx.subtypes

data class Pitchshift(
    override val fxType: FXType = FXType.PITCHSHIFT,
    val fine: Float,
    val effectLevel: Int,
    val directLevel: Int
): FxEffect(){
    override fun toParams(): List<Int> = padTo20(listOf((fine + 50).toInt(), effectLevel, directLevel))

    companion object {
        fun fromValues(params: List<Int>):  Pitchshift {
            return Pitchshift(
                fine = mapFine(params[0]),
                effectLevel = params[1],
                directLevel = params[2]
            )
        }

        private fun mapFine(value: Int): Float {
            return (value - 50).toFloat()
        }
    }
}
