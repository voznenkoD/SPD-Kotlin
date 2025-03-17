package org.xebia.spdmanager.model.system.fx.subtypes

data class Thru(
    override val fxType: FXType = FXType.THRU
) : FxEffect() {
    companion object {
        fun fromValues(params: List<Int>): Thru {
            return Thru()
        }
    }
}



