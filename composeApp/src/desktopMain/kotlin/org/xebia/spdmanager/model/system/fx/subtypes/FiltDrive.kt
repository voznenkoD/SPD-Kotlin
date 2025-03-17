package org.xebia.spdmanager.model.system.fx.subtypes

data class FiltDrive(
    override val fxType: FXType = FXType.FILT_DRIVE,
    val resonance: Int,
    val level: Int
): FxEffect() {
    companion object {
        fun fromValues(params: List<Int>):  FiltDrive {
            return FiltDrive(
                resonance = params[0],
                level = params[1]
            )
        }
    }
}
