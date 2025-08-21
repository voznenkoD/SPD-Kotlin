package org.xebia.spdmanager.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.xebia.spdmanager.model.Wave
import org.xebia.spdmanager.model.kit.Kit
import org.xebia.spdmanager.model.kit.pad.Pad
import org.xebia.spdmanager.model.kit.pad.PadNumber
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

    fun selectKit(kit: Kit) {
        val index = deviceManager.device?.kits?.indexOf(kit)
        if (index != null && index >= 0) {
            _selectedKitIndex.value = index
            _selectedPad.value = null
            _selectedPadNumber.value = null
            _selectedWave.value = null
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