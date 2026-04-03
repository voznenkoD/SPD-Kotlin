package org.xebia.spdmanager.model.system

import org.xebia.spdmanager.data.decodeName
import org.xebia.spdmanager.data.encodeNamePadded
import org.xebia.spdmanager.data.model.raw.system.KitChainPrm
import org.xebia.spdmanager.data.model.raw.system.MEfctPrm
import org.xebia.spdmanager.data.model.raw.system.SysPrm
import org.xebia.spdmanager.model.KitChain
import org.xebia.spdmanager.model.kit.pad.PadNumber
import org.xebia.spdmanager.model.system.fx.MasterEffectConfig
import org.xebia.spdmanager.model.system.vControl.VisualControl

data class SystemConfig(
    val masterEffectConfig: MasterEffectConfig,
    val clickConfig: ClickConfig,
    val systemAudioConfig: SystemAudioConfig,
    val kitChains: Map<Char, KitChain>,
    val padFsControl: Map<PadNumber, PadFsControl>,
    val visualControl: VisualControl
) {
    fun toRawSysPrm(): SysPrm {
        val padCtrls = PadNumber.entries.map { padFsControl[it]?.value ?: 0 }
        return SysPrm(
            clickSoundGroup = clickConfig.soundGroup.value,
            clickSound = clickConfig.sound.value,
            clickWave = clickConfig.wave,
            clickInterval = clickConfig.interval.value,
            clickPan = clickConfig.clickPan.toRaw(),
            clickAssign = clickConfig.output.value,
            clickLevel = clickConfig.level,
            audioInputLevel = systemAudioConfig.audioInVolume,
            audioInputAssign = systemAudioConfig.audioInOutput.value,
            fx2Assign = systemAudioConfig.fx2Output.value,
            systemGain = systemAudioConfig.systemGain.value,
            subOutLevel = systemAudioConfig.subOutVolume,
            usbDAudioInputLevel = systemAudioConfig.usbInVolume,
            kitChainSwitch = 0,
            kitChainBank = 0,
            padCtrlPad1 = padCtrls[0], padCtrlPad2 = padCtrls[1], padCtrlPad3 = padCtrls[2],
            padCtrlPad4 = padCtrls[3], padCtrlPad5 = padCtrls[4], padCtrlPad6 = padCtrls[5],
            padCtrlPad7 = padCtrls[6], padCtrlPad8 = padCtrls[7], padCtrlPad9 = padCtrls[8],
            padCtrlExt1 = padCtrls[9], padCtrlExt2 = padCtrls[10],
            padCtrlExt3 = padCtrls[11], padCtrlExt4 = padCtrls[12],
            padCtrlFS1 = padCtrls[13], padCtrlFS2 = padCtrls[14],
            vLinkMode = visualControl.vControlMode.value,
            vLinkBank = visualControl.bank.toValue(),
            vLinkChannel = visualControl.ch,
            vLinkKnob1CC = visualControl.ctrlKnob1CC.toValue(),
            vLinkKnob2CC = visualControl.ctrlKnob2CC.toValue(),
            vLinkControlOnly = visualControl.visualControlSwitch.value
        )
    }

    fun toRawKitChainPrm(): KitChainPrm {
        val chains = ('A'..'H').map { key ->
            val chain = kitChains[key] ?: KitChain("", List(20) { 0 })
            val nm = encodeNamePadded(chain.name, 10)
            val kits = chain.kitRefs + List(20 - chain.kitRefs.size) { 0 }
            org.xebia.spdmanager.data.model.raw.system.KitChain(
                nm0 = nm[0], nm1 = nm[1], nm2 = nm[2], nm3 = nm[3], nm4 = nm[4],
                nm5 = nm[5], nm6 = nm[6], nm7 = nm[7], nm8 = nm[8], nm9 = nm[9],
                stp0 = kits[0], stp1 = kits[1], stp2 = kits[2], stp3 = kits[3],
                stp4 = kits[4], stp5 = kits[5], stp6 = kits[6], stp7 = kits[7],
                stp8 = kits[8], stp9 = kits[9], stp10 = kits[10], stp11 = kits[11],
                stp12 = kits[12], stp13 = kits[13], stp14 = kits[14], stp15 = kits[15],
                stp16 = kits[16], stp17 = kits[17], stp18 = kits[18], stp19 = kits[19]
            )
        }.toTypedArray()
        return KitChainPrm(kitChains = chains)
    }

    fun toRawMEfctPrm(): MEfctPrm {
        val eq = systemAudioConfig.systemEq
        val fltrParams = masterEffectConfig.filterEffect.toRawParams()
        val drParams = masterEffectConfig.delayEffect.toRawParams()
        val spParams = masterEffectConfig.sLoopEffect.toRawParams()
        val fxParams = masterEffectConfig.toRawFxParams()
        return MEfctPrm(
            meqLoGain = eq.toRawLowGain(), meqMidFreq = eq.toRawMidFreq(),
            meqMidGain = eq.toRawMidGain(), meqHiGain = eq.toRawHighGain(),
            fltrPreset = masterEffectConfig.filterEffect.toRawPreset(),
            dlyPreset = masterEffectConfig.delayEffect.toRawPreset(),
            sloopPreset = masterEffectConfig.sLoopEffect.toRawPreset(),
            fltrType = masterEffectConfig.filterEffect.toRawType(),
            fltrPrm0 = fltrParams[0], fltrPrm1 = fltrParams[1], fltrPrm2 = fltrParams[2],
            fltrPrm3 = fltrParams[3], fltrPrm4 = fltrParams[4], fltrPrm5 = fltrParams[5],
            fltrPrm6 = fltrParams.getOrElse(6) { 0 }, fltrPrm7 = fltrParams.getOrElse(7) { 0 },
            fltrPrm8 = fltrParams.getOrElse(8) { 0 }, fltrPrm9 = fltrParams.getOrElse(9) { 0 },
            fltrPrm10 = fltrParams.getOrElse(10) { 0 }, fltrPrm11 = fltrParams.getOrElse(11) { 0 },
            fltrPrm12 = fltrParams.getOrElse(12) { 0 }, fltrPrm13 = fltrParams.getOrElse(13) { 0 },
            fltrPrm14 = fltrParams.getOrElse(14) { 0 }, fltrPrm15 = fltrParams.getOrElse(15) { 0 },
            fltrPrm16 = fltrParams.getOrElse(16) { 0 }, fltrPrm17 = fltrParams.getOrElse(17) { 0 },
            fltrPrm18 = fltrParams.getOrElse(18) { 0 }, fltrPrm19 = fltrParams.getOrElse(19) { 0 },
            drType = masterEffectConfig.delayEffect.toRawType(),
            drPrm0 = drParams[0], drPrm1 = drParams[1], drPrm2 = drParams[2],
            drPrm3 = drParams[3], drPrm4 = drParams[4], drPrm5 = drParams[5],
            drPrm6 = drParams[6], drPrm7 = drParams.getOrElse(7) { 0 },
            drPrm8 = drParams.getOrElse(8) { 0 }, drPrm9 = drParams.getOrElse(9) { 0 },
            drPrm10 = drParams.getOrElse(10) { 0 }, drPrm11 = drParams.getOrElse(11) { 0 },
            drPrm12 = drParams.getOrElse(12) { 0 }, drPrm13 = drParams.getOrElse(13) { 0 },
            drPrm14 = drParams.getOrElse(14) { 0 }, drPrm15 = drParams.getOrElse(15) { 0 },
            drPrm16 = drParams.getOrElse(16) { 0 }, drPrm17 = drParams.getOrElse(17) { 0 },
            drPrm18 = drParams.getOrElse(18) { 0 }, drPrm19 = drParams.getOrElse(19) { 0 },
            spType = masterEffectConfig.sLoopEffect.toRawType(),
            spPrm0 = spParams[0], spPrm1 = spParams[1], spPrm2 = spParams[2],
            spPrm3 = spParams[3], spPrm4 = spParams.getOrElse(4) { 0 },
            spPrm5 = spParams.getOrElse(5) { 0 }, spPrm6 = spParams.getOrElse(6) { 0 },
            spPrm7 = spParams.getOrElse(7) { 0 }, spPrm8 = spParams.getOrElse(8) { 0 },
            spPrm9 = spParams.getOrElse(9) { 0 }, spPrm10 = spParams.getOrElse(10) { 0 },
            spPrm11 = spParams.getOrElse(11) { 0 }, spPrm12 = spParams.getOrElse(12) { 0 },
            spPrm13 = spParams.getOrElse(13) { 0 }, spPrm14 = spParams.getOrElse(14) { 0 },
            spPrm15 = spParams.getOrElse(15) { 0 }, spPrm16 = spParams.getOrElse(16) { 0 },
            spPrm17 = spParams.getOrElse(17) { 0 }, spPrm18 = spParams.getOrElse(18) { 0 },
            spPrm19 = spParams.getOrElse(19) { 0 },
            fxType = masterEffectConfig.fxEffect.fxType.value,
            fxPrm0 = fxParams[0], fxPrm1 = fxParams[1], fxPrm2 = fxParams[2],
            fxPrm3 = fxParams[3], fxPrm4 = fxParams[4], fxPrm5 = fxParams[5],
            fxPrm6 = fxParams[6], fxPrm7 = fxParams[7], fxPrm8 = fxParams[8],
            fxPrm9 = fxParams[9], fxPrm10 = fxParams[10], fxPrm11 = fxParams[11],
            fxPrm12 = fxParams[12], fxPrm13 = fxParams[13], fxPrm14 = fxParams[14],
            fxPrm15 = fxParams[15], fxPrm16 = fxParams[16], fxPrm17 = fxParams[17],
            fxPrm18 = fxParams[18], fxPrm19 = fxParams[19]
        )
    }

    companion object {
        fun fromValue(rawSys: SysPrm, rawKitChain: KitChainPrm, mEfctPrm: MEfctPrm): SystemConfig {
            val clickConfig = ClickConfig.fromValues(rawSys.clickSoundGroup, rawSys.clickSound, rawSys.clickWave, rawSys.clickInterval, rawSys.clickPan, rawSys.clickAssign, rawSys.clickLevel)
            val kitChains = toKitChains(rawKitChain)
            val masterEffectConfig = MasterEffectConfig.fromValues(mEfctPrm)
            val systemAudioConfig =  SystemAudioConfig.fromValues(
                rawSys.audioInputLevel,
                rawSys.usbDAudioInputLevel,
                rawSys.subOutLevel,
                rawSys.systemGain,
                rawSys.audioInputAssign,
                rawSys.fx2Assign,
                1,
                1,
                1,
                1,
            )
            val padFsControl = toPadFsControls(rawSys.padFsControls())
            val visualControl = VisualControl.fromValues(
                rawSys.vLinkControlOnly,
                rawSys.vLinkMode,
                rawSys.vLinkBank,
                rawSys.vLinkChannel,
                rawSys.vLinkKnob1CC,
                rawSys.vLinkKnob2CC
            )

            return SystemConfig (
                masterEffectConfig,
                clickConfig,
                systemAudioConfig,
                kitChains,
                padFsControl,
                visualControl
            )
        }

        private fun toPadFsControls(padFsControls: IntArray): Map<PadNumber, PadFsControl> {
            return padFsControls.mapIndexed { index, value ->
                PadNumber.fromValue(index) to PadFsControl.fromValue(value)
            }.toMap()
        }

        private fun toKitChains(kitChainPrm: KitChainPrm): Map<Char, KitChain> {
            return kitChainPrm.kitChains.mapIndexed { index, rawKitChain ->
                val key = ('A' + index).takeIf { it <= 'H' } ?: error("Index out of range for mapping A-H")
                key to KitChain(decodeName(rawKitChain.kitChainName()), rawKitChain.getKits())
            }.toMap()
        }
    }
}
