package org.xebia.spdmanager.model.kit.fx

import org.xebia.spdmanager.model.system.fx.common.SyncSwitch
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect

data class KitFX(val sw: SyncSwitch, val fx:FxEffect) {
    companion object {
        fun fromValues(sw:Int, fxType: Int, fxParams: List<Int>): KitFX {
            val fxEffects = FxEffect.fromValues(fxType, fxParams)
            return KitFX(SyncSwitch.fromValue(sw), fxEffects)
        }
    }
}
