package org.xebia.spdmanager.model.system.fx.subtypes

import org.xebia.spdmanager.model.system.fx.common.*

data class Filter(
    override val fxType: FXType = FXType.FILTER,
    val type: FilterType,
    val resonance: Int,
    val slope: FilterSlope,
    val rateSyncSW: SyncSwitch,
    val modRate: ModRate,
    val lfoWave: LfoWave
): FxEffect(){
    companion object {
        fun fromValues(params: List<Int>): Filter {
            val rateSyncSwEnum = SyncSwitch.fromValue(params[3])

            return Filter(
                type = FilterType.fromValue(params[0]),
                resonance = params[1],
                slope = FilterSlope.fromValue(params[2]),
                rateSyncSW = rateSyncSwEnum,
                modRate = ModRate.create(rateSyncSwEnum, params[3], params[4]),
                lfoWave = LfoWave.fromValue(params[5])
            )
        }
    }
}
