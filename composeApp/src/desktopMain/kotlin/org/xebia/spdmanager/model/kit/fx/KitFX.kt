package org.xebia.spdmanager.model.kit.fx

import org.xebia.spdmanager.model.system.fx.common.SyncSwitch
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect

data class KitFX(val sw: SyncSwitch, val fx:FxEffect) {
    fun toRawSw(): Int = sw.value
    fun toRawType(): Int = fx.fxType.value
    fun toRawParams(): List<Int> = fx.toParams()

    companion object {
        fun fromValues(sw:Int, fxType: Int, fxParams: List<Int>): KitFX {
            val fxEffects = FxEffect.fromValues(fxType, fxParams)
            return KitFX(SyncSwitch.fromValue(sw), fxEffects)
        }
    }
}
