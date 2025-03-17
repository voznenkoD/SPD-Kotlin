package org.xebia.spdmanager.model.system.fx.subtypes

data class Isolator(
    override val fxType: FXType = FXType.ISOLATOR,
    val mid: Float,
    val level: Int
): FxEffect(){
    companion object {
        fun fromValues(params: List<Int>): Isolator {
            return Isolator(
                mid = mapMidDecibels(params[0]),
                level = params[1]
            )
        }

        private fun mapMidDecibels(value: Int): Float {
            return (value - 60).toFloat()
        }
    }
}
