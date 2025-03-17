package org.xebia.spdmanager.model.system.fx.subtypes

data class Vibrato(
    override val fxType: FXType = FXType.VIBRATO,
): FxEffect() {
    companion object {
        fun fromValues(params: List<Int>): Vibrato {
            return Vibrato()
        }
    }
}
