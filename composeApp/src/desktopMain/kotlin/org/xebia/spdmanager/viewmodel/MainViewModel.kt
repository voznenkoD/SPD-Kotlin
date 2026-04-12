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
import java.io.File

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

    private val _importError = MutableStateFlow<String?>(null)
    val importError: StateFlow<String?> = _importError.asStateFlow()

    private val _listsSelectedTab = MutableStateFlow(0)
    val listsSelectedTab: StateFlow<Int> = _listsSelectedTab.asStateFlow()

    data class DeleteConfirmInfo(val waveNumber: Int, val waveName: String)
    data class DeleteBlockedInfo(val waveName: String, val kitNames: List<String>)

    private val _deleteConfirm = MutableStateFlow<DeleteConfirmInfo?>(null)
    val deleteConfirm: StateFlow<DeleteConfirmInfo?> = _deleteConfirm.asStateFlow()

    private val _deleteBlocked = MutableStateFlow<DeleteBlockedInfo?>(null)
    val deleteBlocked: StateFlow<DeleteBlockedInfo?> = _deleteBlocked.asStateFlow()

    private val _deleteError = MutableStateFlow<String?>(null)
    val deleteError: StateFlow<String?> = _deleteError.asStateFlow()

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
        _listsSelectedTab.value = 1

        val pad = kit?.pads?.get(padNumber)
        _selectedPad.value = pad

        val waveNumber = if (isMain) pad?.main?.wave else pad?.sub?.wave
        _selectedWave.value = if (waveNumber != null && waveNumber > 0) {
            deviceManager.device?.waves?.find { it.number == waveNumber }
        } else {
            null
        }
    }

    fun selectListsTab(index: Int) {
        _listsSelectedTab.value = index
    }

    fun renameCategory(oldName: String, newName: String) {
        deviceManager.renameCategory(oldName, newName)
    }

    fun importWave(sourceFile: File, categoryName: String) {
        when (val result = deviceManager.importWave(sourceFile, categoryName)) {
            is DeviceManager.ImportResult.Success -> Unit
            is DeviceManager.ImportResult.Error -> _importError.value = result.message
        }
    }

    fun clearImportError() {
        _importError.value = null
    }

    fun requestDeleteWave(waveNumber: Int) {
        val dev = deviceManager.device ?: return
        val wave = dev.waves.find { it.number == waveNumber } ?: return
        val usage = DeviceManager.buildWaveUsageMap(dev.kits)[waveNumber].orEmpty()
        if (usage.isNotEmpty()) {
            _deleteBlocked.value = DeleteBlockedInfo(wave.name, usage)
        } else {
            _deleteConfirm.value = DeleteConfirmInfo(wave.number, wave.name)
        }
    }

    fun confirmDeleteWave() {
        val confirming = _deleteConfirm.value ?: return
        _deleteConfirm.value = null
        when (val result = deviceManager.deleteWave(confirming.waveNumber)) {
            is DeviceManager.DeleteResult.Success -> {
                if (_selectedWave.value?.number == confirming.waveNumber) {
                    _selectedWave.value = null
                }
            }
            is DeviceManager.DeleteResult.Error -> {
                _deleteError.value = result.message
            }
            is DeviceManager.DeleteResult.InUse -> {
                _deleteBlocked.value = DeleteBlockedInfo(confirming.waveName, result.kitNames)
            }
        }
    }

    fun clearDeleteConfirm() {
        _deleteConfirm.value = null
    }

    fun clearDeleteBlocked() {
        _deleteBlocked.value = null
    }

    fun clearDeleteError() {
        _deleteError.value = null
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