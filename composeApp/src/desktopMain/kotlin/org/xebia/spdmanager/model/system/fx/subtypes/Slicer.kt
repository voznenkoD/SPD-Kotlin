package org.xebia.spdmanager.model.system.fx.subtypes

import org.xebia.spdmanager.model.system.fx.common.*

data class Slicer(
    override val fxType: FXType = FXType.SLICER,
    val pattern: Int,
    val rateSync: SyncSwitch,
    val attack: Int
): FxEffect() {
    companion object {
        fun fromValues(params: List<Int>): Slicer {
            return Slicer(
                pattern = params[0],
                rateSync = SyncSwitch.fromValue(params[1]),
                attack = params[2]
            )
        }
    }
}
