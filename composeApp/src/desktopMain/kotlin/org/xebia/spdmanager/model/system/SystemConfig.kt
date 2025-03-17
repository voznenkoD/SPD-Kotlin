package org.xebia.spdmanager.model.system

import org.xebia.spdmanager.data.decodeName
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
