package org.xebia.spdmanager.viewmodel

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
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
import java.util.concurrent.ConcurrentHashMap

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

    private val _kitLimitReached = MutableStateFlow(false)
    val kitLimitReached: StateFlow<Boolean> = _kitLimitReached.asStateFlow()

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

    private val _waveOpError = MutableStateFlow<String?>(null)
    val waveOpError: StateFlow<String?> = _waveOpError.asStateFlow()

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

    /**
     * Apply an edit to the currently selected wave: update it in the device (in memory) and refresh
     * the selection so the UI reflects the change immediately. Persistence to disk happens only when
     * the user explicitly saves (DeviceManager.saveDevice()) — this never writes to disk.
     */
    fun updateSelectedWave(transform: (Wave) -> Wave) {
        val current = _selectedWave.value ?: return
        deviceManager.updateWave(current.number, transform)
        _selectedWave.value = deviceManager.device?.waves?.find { it.number == current.number }
    }

    fun renameCategory(oldName: String, newName: String) {
        deviceManager.renameCategory(oldName, newName)
    }

    /** The category name currently holding [waveNumber], or null if unknown. */
    fun categoryOfWave(waveNumber: Int): String? {
        val holder = deviceManager.device?.waveLists ?: return null
        return holder.wavesByNamePerCategory.entries
            .firstOrNull { (_, waves) -> waves.any { it.number == waveNumber } }
            ?.key?.name
    }

    fun renameWave(waveNumber: Int, newName: String) {
        when (val result = deviceManager.renameWave(waveNumber, newName)) {
            is DeviceManager.WaveOpResult.Success -> resyncSelectedWave(waveNumber)
            is DeviceManager.WaveOpResult.Error -> _waveOpError.value = result.message
        }
    }

    fun moveWaveToCategory(waveNumber: Int, categoryName: String) {
        when (val result = deviceManager.moveWaveToCategory(waveNumber, categoryName)) {
            is DeviceManager.WaveOpResult.Success -> resyncSelectedWave(waveNumber)
            is DeviceManager.WaveOpResult.Error -> _waveOpError.value = result.message
        }
    }

    /** Refresh the selected-wave snapshot after an in-place edit so the detail view stays in sync. */
    private fun resyncSelectedWave(waveNumber: Int) {
        if (_selectedWave.value?.number == waveNumber) {
            _selectedWave.value = deviceManager.device?.waves?.find { it.number == waveNumber }
        }
    }

    fun clearWaveOpError() {
        _waveOpError.value = null
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

    /**
     * Copies the kit at [sourceIndex] and appends it to the end of the kit list (renamed via
     * [duplicateName]), then selects and scrolls to the new kit. If the device is already at the kit
     * limit, raises the [kitLimitReached] flag instead of adding a kit. Resolving the source by its
     * list index (not by value) is what keeps this correct when several kits are identical.
     */
    fun duplicateKit(sourceIndex: Int) {
        val newIndex = deviceManager.duplicateKit(sourceIndex, ::duplicateName)
        if (newIndex != null) {
            selectKitByIndex(newIndex)
        } else {
            _kitLimitReached.value = true
        }
    }

    fun clearKitLimitReached() {
        _kitLimitReached.value = false
    }

    /**
     * Duplicate-naming rule: append "2" to the source name, truncating the source first when needed
     * so the result stays within the kit-name limit. A "2" is always appended, even when the name
     * already ends in one; no uniqueness is enforced.
     */
    private fun duplicateName(source: String): String =
        source.take(Kit.NAME_MAX_LENGTH - 1) + "2"

    fun copyKit(kit: Kit) {
        _clipboardKit.value = kit
    }

    fun pasteKit(targetIndex: Int) {
        val copied = _clipboardKit.value ?: return
        deviceManager.updateKit(targetIndex, copied)
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
    // --- Wave drag-and-drop state ---

    data class DragInfo(val waveNumber: Int, val waveName: String)

    private val _dragInfo = MutableStateFlow<DragInfo?>(null)
    val dragInfo: StateFlow<DragInfo?> = _dragInfo.asStateFlow()

    private val _dragPosition = MutableStateFlow<Offset?>(null)
    val dragPosition: StateFlow<Offset?> = _dragPosition.asStateFlow()

    data class PadDropTarget(val padNumber: PadNumber, val mainBounds: Rect, val subBounds: Rect)
    private val padDropTargets = ConcurrentHashMap<PadNumber, PadDropTarget>()

    fun registerPadBounds(padNumber: PadNumber, mainBounds: Rect, subBounds: Rect) {
        padDropTargets[padNumber] = PadDropTarget(padNumber, mainBounds, subBounds)
    }

    fun unregisterPadBounds(padNumber: PadNumber) {
        padDropTargets.remove(padNumber)
    }

    fun startWaveDrag(waveNumber: Int, waveName: String) {
        _dragInfo.value = DragInfo(waveNumber, waveName)
    }

    fun updateDragPosition(position: Offset) {
        _dragPosition.value = position
    }

    fun endWaveDrag() {
        val info = _dragInfo.value
        val pos = _dragPosition.value
        _dragInfo.value = null
        _dragPosition.value = null

        if (info == null || pos == null) return
        val kitIndex = _selectedKitIndex.value ?: return
        val (padNumber, isMain) = hitTestPad(pos) ?: return
        assignWaveToPad(kitIndex, padNumber, isMain, info.waveNumber)
    }

    fun cancelWaveDrag() {
        _dragInfo.value = null
        _dragPosition.value = null
    }

    // --- External (OS filesystem) wave-file drop ---

    /**
     * Handle a .wav file dropped from the OS onto a specific pad zone: import it into the Default
     * category (last in the list, like importWave) and assign the new wave to that pad's main or sub
     * zone. Does nothing if no kit is selected (import is skipped entirely).
     */
    fun dropExternalWaveFileOnPad(padNumber: PadNumber, isMain: Boolean, file: File) {
        val kitIndex = _selectedKitIndex.value ?: return
        when (val result = deviceManager.importWave(file, "Default")) {
            is DeviceManager.ImportResult.Success ->
                assignWaveToPad(kitIndex, padNumber, isMain, result.wave.number)
            is DeviceManager.ImportResult.Error ->
                _importError.value = result.message
        }
    }

    /** Hit-test a window-coordinate position against registered pad zones; main is checked first. */
    private fun hitTestPad(pos: Offset): Pair<PadNumber, Boolean>? {
        for ((_, target) in padDropTargets) {
            if (target.mainBounds.contains(pos)) return target.padNumber to true
            if (target.subBounds.contains(pos)) return target.padNumber to false
        }
        return null
    }

    private fun assignWaveToPad(kitIndex: Int, padNumber: PadNumber, isMain: Boolean, waveNumber: Int) {
        deviceManager.updatePad(kitIndex, padNumber) { pad ->
            if (isMain) pad.copy(main = pad.main.copy(wave = waveNumber))
            else pad.copy(sub = pad.sub.copy(wave = waveNumber))
        }
    }
}