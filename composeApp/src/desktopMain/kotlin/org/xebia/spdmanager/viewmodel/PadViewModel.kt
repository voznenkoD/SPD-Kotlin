package org.xebia.spdmanager.viewmodel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import org.xebia.spdmanager.model.kit.pad.*
import org.xebia.spdmanager.model.kit.pad.mode.PadMode
import org.xebia.spdmanager.model.system.fx.common.SyncSwitch
import org.xebia.spdmanager.service.DeviceManager

class PadViewModel(
    private val kitIndex: Int,
    private val padNumber: PadNumber,
    private val deviceManager: DeviceManager
) : ViewModel() {

    val pad by derivedStateOf {
        deviceManager.device?.kits?.getOrNull(kitIndex)?.pads?.get(padNumber)
    }

    fun updateMainSound(sound: Sound) {
        deviceManager.updatePad(kitIndex, padNumber) {
            it.copy(main = sound)
        }
    }

    fun updateSubSound(sound: Sound) {
        deviceManager.updatePad(kitIndex, padNumber) {
            it.copy(sub = sound)
        }
    }

    fun updateMuteGroup(muteGroup: MuteGroup) {
        deviceManager.updatePad(kitIndex, padNumber) {
            it.copy(muteGroup = muteGroup)
        }
    }

    fun updateTempoSync(tempoSync: SyncSwitch) {
        deviceManager.updatePad(kitIndex, padNumber) {
            it.copy(tempoSync = tempoSync)
        }
    }

    fun updateOutput(output: PadOutput) {
        deviceManager.updatePad(kitIndex, padNumber) {
            it.copy(output = output)
        }
    }

    fun updatePadMode(padMode: PadMode) {
        deviceManager.updatePad(kitIndex, padNumber) {
            it.copy(padMode = padMode)
        }
    }

    fun updateMidiParams(midiParams: MidiParams) {
        deviceManager.updatePad(kitIndex, padNumber) {
            it.copy(midiParams = midiParams)
        }
    }
}