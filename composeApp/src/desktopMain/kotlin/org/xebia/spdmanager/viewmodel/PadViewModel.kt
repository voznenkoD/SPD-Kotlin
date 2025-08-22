package org.xebia.spdmanager.viewmodel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import org.xebia.spdmanager.model.kit.pad.*
import org.xebia.spdmanager.model.kit.pad.mode.PadMode
import org.xebia.spdmanager.model.system.fx.common.SyncSwitch
import org.xebia.spdmanager.service.DeviceManager

class PadViewModel(
    private val kitIndex: Int,
    private val padNumber: PadNumber,
    private val deviceManager: DeviceManager
) {
    val pad by derivedStateOf {
        deviceManager.device?.kits?.getOrNull(kitIndex)?.pads?.get(padNumber)
    }

    fun updateMainSound(newSound: Sound) {
        pad?.let { currentPad ->
            deviceManager.updatePad(kitIndex, padNumber, currentPad.copy(main = newSound))
        }
    }

    fun updateSubSound(newSound: Sound) {
        pad?.let { currentPad ->
            deviceManager.updatePad(kitIndex, padNumber, currentPad.copy(sub = newSound))
        }
    }

    fun updateMuteGroup(muteGroup: MuteGroup) {
        pad?.let { currentPad ->
            deviceManager.updatePad(kitIndex, padNumber, currentPad.copy(muteGroup = muteGroup))
        }
    }

    fun updateTempoSync(tempoSync: SyncSwitch) {
        pad?.let { currentPad ->
            deviceManager.updatePad(kitIndex, padNumber, currentPad.copy(tempoSync = tempoSync))
        }
    }

    fun updateOutput(output: PadOutput) {
        pad?.let { currentPad ->
            deviceManager.updatePad(kitIndex, padNumber, currentPad.copy(output = output))
        }
    }

    fun updatePadMode(padMode: PadMode) {
        pad?.let { currentPad ->
            deviceManager.updatePad(kitIndex, padNumber, currentPad.copy(padMode = padMode))
        }
    }

    fun updateMidiParams(midiParams: MidiParams) {
        pad?.let { currentPad ->
            deviceManager.updatePad(kitIndex, padNumber, currentPad.copy(midiParams = midiParams))
        }
    }
}