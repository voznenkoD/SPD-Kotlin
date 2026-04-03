package org.xebia.spdmanager.model.system.fx.subtypes

data class Thru(
    override val fxType: FXType = FXType.THRU
) : FxEffect() {
    override fun toParams(): List<Int> = padTo20(emptyList())

    companion object {
        fun fromValues(params: List<Int>): Thru {
            return Thru()
        }
    }
}



