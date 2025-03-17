package org.xebia.spdmanager.model.kit.pad.mode

import org.xebia.spdmanager.model.system.fx.common.SyncSwitch

data class PadMode(
    val template: PadTemplate,
    val loop: PadLoop,
    val trigType: TrigType,
    val dynamics: SyncSwitch,
    val polyMono: PolyMono
) {
    companion object {
        fun fromValues(
            template: Int,
            loop: Int,
            trigType: Int,
            dynamics: Int,
            polyMono: Int
        ): PadMode {
            return PadMode(
                PadTemplate.fromValue(template),
                PadLoop.fromValue(loop),
                TrigType.fromValue(trigType),
                SyncSwitch.fromValue(dynamics),
                PolyMono.fromValue(polyMono)
            )
        }
    }
}
