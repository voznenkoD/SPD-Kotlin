package org.xebia.spdmanager.viewmodel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import org.xebia.spdmanager.model.kit.Kit
import org.xebia.spdmanager.model.kit.fx.KitFX
import org.xebia.spdmanager.model.kit.pad.PadNumber
import org.xebia.spdmanager.service.DeviceManager

class KitViewModel(
    private val kitIndex: Int,
    private val deviceManager: DeviceManager
) : ViewModel() {

    val kit by derivedStateOf {
        deviceManager.device?.kits?.getOrNull(kitIndex)
    }

    fun updateName(name: String) {
        deviceManager.updateKit(kitIndex) { it.copy(name = name) }
    }

    fun updateSubName(subName: String) {
        deviceManager.updateKit(kitIndex) { it.copy(subName = subName) }
    }

    fun updateTempo(tempo: Double) {
        deviceManager.updateKit(kitIndex) { it.copy(tempo = tempo) }
    }

    fun updateVolume(volume: Int) {
        deviceManager.updateKit(kitIndex) { it.copy(volume = volume) }
    }

    fun updatePadLink(pad1: PadNumber, pad2: PadNumber) {
        deviceManager.updateKit(kitIndex) { it.copy(padLink = pad1 to pad2) }
    }

    fun updateFx1(fx: KitFX) {
        deviceManager.updateKit(kitIndex) { it.copy(fx1 = fx) }
    }

    fun updateFx2(fx: KitFX) {
        deviceManager.updateKit(kitIndex) { it.copy(fx2 = fx) }
    }
}