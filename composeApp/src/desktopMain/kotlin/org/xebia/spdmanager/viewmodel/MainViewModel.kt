package org.xebia.spdmanager.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.xebia.spdmanager.model.Wave
import org.xebia.spdmanager.model.kit.Kit
import org.xebia.spdmanager.model.kit.pad.Pad
import org.xebia.spdmanager.model.kit.pad.PadNumber
import org.xebia.spdmanager.model.kit.pad.PadPan
import org.xebia.spdmanager.model.kit.pad.Sound
import org.xebia.spdmanager.model.list.ListedWave
import org.xebia.spdmanager.service.DeviceManager

class MainViewModel(
    val deviceManager: DeviceManager
) {
    private val _selectedKitIndex = MutableStateFlow<Int?>(null)
    val selectedKitIndex: StateFlow<Int?> = _selectedKitIndex.asStateFlow()

    private val _selectedWave = MutableStateFlow<Wave?>(null)
    val selectedWave: StateFlow<Wave?> = _selectedWave.asStateFlow()

    private val _selectedPad = MutableStateFlow<Pad?>(null)
    val selectedPad: StateFlow<Pad?> = _selectedPad.asStateFlow()

    private val _selectedPadNumber = MutableStateFlow<PadNumber?>(null)
    val selectedPadNumber: StateFlow<PadNumber?> = _selectedPadNumber.asStateFlow()

    private val _isMainSelected = MutableStateFlow(true)
    val isMainSelected: StateFlow<Boolean> = _isMainSelected.asStateFlow()

    private val _clipboardKit = MutableStateFlow<Kit?>(null)
    val clipboardKit: StateFlow<Kit?> = _clipboardKit.asStateFlow()

    private val _clipboardPad = MutableStateFlow<Pad?>(null)
    val clipboardPad: StateFlow<Pad?> = _clipboardPad.asStateFlow()

    fun selectKit(kit: Kit) {
        val index = deviceManager.device?.kits?.indexOf(kit)
        if (index != null && index >= 0) {
            _selectedKitIndex.value = index
            _selectedPad.value = null
            _selectedPadNumber.value = null
            _selectedWave.value = null
        }
    }

    fun selectKitByName(kitName: String) {
        val kits = deviceManager.device?.kits ?: return
        val index = kits.indexOfFirst { it.name == kitName }
        if (index >= 0) {
            selectKitByIndex(index)
        }
    }

    fun selectKitByIndex(index: Int) {
        _selectedKitIndex.value = index
        _selectedPad.value = null
        _selectedPadNumber.value = null
        _selectedWave.value = null
    }

    fun selectWave(waves: List<Wave>, listedWave: ListedWave) {
        _selectedWave.value = waves.find { it.number == listedWave.number }
        _selectedPad.value = null
        _selectedPadNumber.value = null
    }

    fun selectPad(padNumber: PadNumber, kit: Kit?, isMain: Boolean) {
        _selectedPadNumber.value = padNumber
        _isMainSelected.value = isMain

        val pad = kit?.pads?.get(padNumber)
        _selectedPad.value = pad

        val waveNumber = if (isMain) {
            pad?.main?.wave
        } else {
            pad?.sub?.wave
        }

        waveNumber?.let { number ->
            val waves = deviceManager.device?.waves ?: emptyList()
            _selectedWave.value = waves.find { it.number == number }
        }
    }

    fun moveKit(fromIndex: Int, toIndex: Int) {
        val currentSelected = _selectedKitIndex.value
        deviceManager.moveKit(fromIndex, toIndex)
        if (currentSelected != null) {
            when (currentSelected) {
                fromIndex -> _selectedKitIndex.value = toIndex
                in minOf(fromIndex, toIndex)..maxOf(fromIndex, toIndex) -> {
                    if (fromIndex < toIndex) {
                        _selectedKitIndex.value = currentSelected - 1
                    } else {
                        _selectedKitIndex.value = currentSelected + 1
                    }
                }
            }
        }
    }

    fun copyKit(kit: Kit) {
        _clipboardKit.value = kit
    }

    fun pasteKit(targetKit: Kit) {
        val copied = _clipboardKit.value ?: return
        val index = deviceManager.device?.kits?.indexOf(targetKit) ?: return
        if (index >= 0) {
            deviceManager.updateKit(index, copied.copy(name = copied.name))
        }
    }

    fun copyPad(padNumber: PadNumber) {
        val kitIndex = _selectedKitIndex.value ?: return
        val pad = deviceManager.device?.kits?.getOrNull(kitIndex)?.pads?.get(padNumber) ?: return
        _clipboardPad.value = pad
    }

    fun pastePad(padNumber: PadNumber) {
        val copied = _clipboardPad.value ?: return
        val kitIndex = _selectedKitIndex.value ?: return
        deviceManager.updatePad(kitIndex, padNumber, copied)
    }

    fun removeWave(padNumber: PadNumber) {
        val kitIndex = _selectedKitIndex.value ?: return
        val emptySound = Sound(wave = 0, volume = 100, pan = PadPan(0))
        deviceManager.updatePad(kitIndex, padNumber) { pad ->
            pad.copy(main = emptySound)
        }
    }

    fun removeSubWave(padNumber: PadNumber) {
        val kitIndex = _selectedKitIndex.value ?: return
        val emptySound = Sound(wave = 0, volume = 100, pan = PadPan(0))
        deviceManager.updatePad(kitIndex, padNumber) { pad ->
            pad.copy(sub = emptySound)
        }
    }

    /**
     * Toggle between main and sub wave for the current pad
     */
    fun toggleMainSub() {
        _selectedPad.value?.let { pad ->
            val newIsMain = !_isMainSelected.value
            _isMainSelected.value = newIsMain

            val waveNumber = if (newIsMain) {
                pad.main.wave
            } else {
                pad.sub.wave
            }

            waveNumber?.let { number ->
                val waves = deviceManager.device?.waves ?: emptyList()
                _selectedWave.value = waves.find { it.number == number }
            }
        }
    }
}