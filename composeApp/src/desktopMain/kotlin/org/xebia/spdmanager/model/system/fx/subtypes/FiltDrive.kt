package org.xebia.spdmanager.model.system.fx.subtypes

data class FiltDrive(
    override val fxType: FXType = FXType.FILT_DRIVE,
    val resonance: Int,
    val level: Int
): FxEffect() {
    override fun toParams(): List<Int> = padTo20(listOf(resonance, level))

    companion object {
        fun fromValues(params: List<Int>):  FiltDrive {
            return FiltDrive(
                resonance = params[0],
                level = params[1]
            )
        }
    }
}
