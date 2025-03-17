package org.xebia.spdmanager.model.system.fx.mainTypes

import org.xebia.spdmanager.model.system.fx.common.*

data class FilterEffect(
    val preset: FilterPreset,
    val type: FilterType,
    val slope: FilterSlope,
    val rateSync: SyncSwitch,
    val modRate: ModRate,
    val modDepth: Int,
    val lfoWave: LfoWave
){
    companion object {
        fun fromValues(
            preset: Int, type: Int, slope: Int, rateSync: Int,
            modRateValueMS: Int, modRateValueNote: Int, lfoWave: Int, modDepth: Int,
        ): FilterEffect {
            val rateSyncEnum = SyncSwitch.fromValue(rateSync)

            return FilterEffect(
                preset = FilterPreset.fromValue(preset),
                type = FilterType.fromValue(type),
                slope = FilterSlope.fromValue(slope),
                rateSync = rateSyncEnum,
                modRate = ModRate.create(rateSyncEnum, modRateValueNote, modRateValueMS),
                modDepth = modDepth,
                lfoWave = LfoWave.fromValue(lfoWave)
            )
        }
    }
}

enum class FilterPreset(val value: Int) {
    SIMPLE_LFP(0),
    SIMPLE_BPF(1),
    SIMPLE_HPF(2),
    LPF_SYNC_MOD(3),
    BPF_SYNC_MOD(4),
    HPF_SYNC_MOD(5);

    companion object {
        fun fromValue(value: Int): FilterPreset {
            return entries.find { it.value == value } ?: SIMPLE_LFP
        }
    }
}
