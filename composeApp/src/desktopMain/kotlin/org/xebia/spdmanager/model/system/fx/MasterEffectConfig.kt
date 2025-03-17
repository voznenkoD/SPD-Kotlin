package org.xebia.spdmanager.model.system.fx

import org.xebia.spdmanager.data.model.raw.system.MEfctPrm
import org.xebia.spdmanager.model.system.fx.mainTypes.DelayEffect
import org.xebia.spdmanager.model.system.fx.mainTypes.FilterEffect
import org.xebia.spdmanager.model.system.fx.mainTypes.SLoopEffect
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect

data class MasterEffectConfig(
    val filterEffect: FilterEffect,
    val delayEffect: DelayEffect,
    val sLoopEffect: SLoopEffect,
    val fxEffect: FxEffect
){
    companion object {
        fun fromValues(
           mEfctPrm: MEfctPrm
        ): MasterEffectConfig {
            val filterEffect = FilterEffect.fromValues(
                mEfctPrm.fltrPreset,
                mEfctPrm.fltrType,
                mEfctPrm.fltrPrm0,
                mEfctPrm.fltrPrm1,
                mEfctPrm.fltrPrm2,
                mEfctPrm.fltrPrm3,
                mEfctPrm.fltrPrm4,
                mEfctPrm.fltrPrm5
            )

            val delayEffect = DelayEffect.fromValues(
                mEfctPrm.dlyPreset,
                mEfctPrm.drType,
                mEfctPrm.drPrm0,
                mEfctPrm.drPrm1,
                mEfctPrm.drPrm2,
                mEfctPrm.drPrm3,
                mEfctPrm.drPrm4,
                mEfctPrm.drPrm5,
                mEfctPrm.drPrm6
            )

            val sLoopEffect = SLoopEffect.fromValues(
                mEfctPrm.sloopPreset,
                mEfctPrm.spType,
                mEfctPrm.spPrm0,
                mEfctPrm.spPrm1,
                mEfctPrm.spPrm2,
                mEfctPrm.spPrm3
            )

            val fxEffect = FxEffect.fromValues(
                mEfctPrm.fxType,
                listOf(
                    mEfctPrm.fxPrm0,
                    mEfctPrm.fxPrm1,
                    mEfctPrm.fxPrm2,
                    mEfctPrm.fxPrm3,
                    mEfctPrm.fxPrm4,
                    mEfctPrm.fxPrm5,
                    mEfctPrm.fxPrm6,
                    mEfctPrm.fxPrm7,
                    mEfctPrm.fxPrm8,
                    mEfctPrm.fxPrm9,
                    mEfctPrm.fxPrm10,
                    mEfctPrm.fxPrm11,
                    mEfctPrm.fxPrm12,
                    mEfctPrm.fxPrm13,
                    mEfctPrm.fxPrm14,
                    mEfctPrm.fxPrm15,
                    mEfctPrm.fxPrm16,
                    mEfctPrm.fxPrm17,
                    mEfctPrm.fxPrm18,
                    mEfctPrm.fxPrm19
                )
            )
            return MasterEffectConfig(filterEffect, delayEffect, sLoopEffect, fxEffect)
        }
    }
}



