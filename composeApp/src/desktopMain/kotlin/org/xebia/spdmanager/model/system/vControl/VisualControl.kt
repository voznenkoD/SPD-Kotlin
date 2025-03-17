package org.xebia.spdmanager.model.system.vControl

import org.xebia.spdmanager.model.system.fx.common.SyncSwitch

data class VisualControl(
    val visualControlSwitch: SyncSwitch,
    val vControlMode: VControlMode,
    val bank: Bank,
    val ch: Int,
    val ctrlKnob1CC: KnobCC,
    val ctrlKnob2CC: KnobCC
) {
    companion object {
        fun fromValues(visualControlSwitch: Int,
                       vControlMode: Int,
                       bank: Int,
                       ch: Int,
                       ctrlKnob1CC: Int,
                       ctrlKnob2CC: Int): VisualControl {
            return VisualControl(
                visualControlSwitch = SyncSwitch.fromValue(visualControlSwitch),
                vControlMode = VControlMode.fromValue(vControlMode),
                bank = Bank.fromValue(bank),
                ch = ch,
                ctrlKnob1CC = KnobCC.fromValue(ctrlKnob1CC),
                ctrlKnob2CC = KnobCC.fromValue(ctrlKnob2CC)
            )
        }
    }
}
