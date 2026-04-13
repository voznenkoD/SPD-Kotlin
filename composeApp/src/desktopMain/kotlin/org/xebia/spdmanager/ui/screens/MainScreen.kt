package org.xebia.spdmanager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.LocalDeviceManager
import org.xebia.spdmanager.model.list.WaveListsHolder
import org.xebia.spdmanager.service.DeviceManager
import org.xebia.spdmanager.ui.components.common.SelectFolderButton
import org.xebia.spdmanager.ui.components.kit.DetailsTabs
import org.xebia.spdmanager.ui.components.lists.ListsScreen
import org.xebia.spdmanager.ui.components.pad.PadScreen
import org.xebia.spdmanager.viewmodel.MainViewModel

@Composable
fun MainScreen() {
    val deviceManager = LocalDeviceManager.current

    val mainViewModel = remember(deviceManager) {
        MainViewModel(deviceManager)
    }

    val device = deviceManager.device

    val selectedKitIndex by mainViewModel.selectedKitIndex.collectAsState()
    val selectedKit = selectedKitIndex?.let { device?.kits?.getOrNull(it) }
    val selectedWave by mainViewModel.selectedWave.collectAsState()
    val selectedPad by mainViewModel.selectedPad.collectAsState()
    val selectedPadNumber by mainViewModel.selectedPadNumber.collectAsState()
    val isMainSelected by mainViewModel.isMainSelected.collectAsState()
    val importError by mainViewModel.importError.collectAsState()
    val deleteConfirm by mainViewModel.deleteConfirm.collectAsState()
    val deleteBlocked by mainViewModel.deleteBlocked.collectAsState()
    val deleteError by mainViewModel.deleteError.collectAsState()
    val listsSelectedTab by mainViewModel.listsSelectedTab.collectAsState()
    val dragInfo by mainViewModel.dragInfo.collectAsState()
    val dragPosition by mainViewModel.dragPosition.collectAsState()

    val kits = device?.kits.orEmpty()
    val waves = device?.waves.orEmpty()
    val waveListsHolder = device?.waveLists ?: WaveListsHolder(emptyList(), emptyMap(), emptyMap())
    val waveUsageMap = remember(kits) { DeviceManager.buildWaveUsageMap(kits) }
    val waveNameLookup: (Int) -> String? = remember(waves) {
        val byNumber = waves.associateBy { it.number }
        val lookup: (Int) -> String? = { n -> byNumber[n]?.name }
        lookup
    }

    if (device == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            SelectFolderButton()
        }
        return
    }

    Box(modifier = Modifier.fillMaxSize()) {
    Row {
        Column(Modifier.weight(0.3f).fillMaxHeight()) {
            DetailsTabs(
                kitIndex = selectedKitIndex,
                kit = selectedKit,
                pad = selectedPad,
                padNumber = selectedPadNumber,
                deviceManager = deviceManager
            )
        }

        Column(
            Modifier
                .weight(0.5f)
                .fillMaxHeight()
                .border(width = 2.dp, color = Color.DarkGray)
        ) {
            val clipboardPad by mainViewModel.clipboardPad.collectAsState()
            PadScreen(
                onSelect = { padNumber, isMain ->
                    mainViewModel.selectPad(padNumber, selectedKit, isMain)
                },
                kit = selectedKit,
                selectedPadNumber = selectedPadNumber,
                isMainSelected = isMainSelected,
                onCopyPad = mainViewModel::copyPad,
                onPastePad = mainViewModel::pastePad,
                onRemoveWave = mainViewModel::removeWave,
                onRemoveSubWave = mainViewModel::removeSubWave,
                hasCopiedPad = clipboardPad != null,
                waveNameLookup = waveNameLookup,
                isDragActive = dragInfo != null,
                dragPosition = dragPosition,
                onRegisterPadBounds = mainViewModel::registerPadBounds,
                onUnregisterPadBounds = mainViewModel::unregisterPadBounds
            )

            WaveDetailsScreen(
                wave = selectedWave,
                device = device
            )
        }

        Column(Modifier.weight(0.2f).fillMaxHeight()) {
            val clipboardKit by mainViewModel.clipboardKit.collectAsState()
            ListsScreen(
                kits = kits,
                waveListsHolder = waveListsHolder,
                onKitSelected = mainViewModel::selectKit,
                onWaveSelected = { listedWave ->
                    mainViewModel.selectWave(waves, listedWave)
                },
                onCopyKit = mainViewModel::copyKit,
                onPasteKit = mainViewModel::pasteKit,
                hasCopiedKit = clipboardKit != null,
                onMoveKit = mainViewModel::moveKit,
                waveUsageMap = waveUsageMap,
                onSelectKitByName = mainViewModel::selectKitByName,
                onRenameCategory = mainViewModel::renameCategory,
                onImportWave = mainViewModel::importWave,
                importError = importError,
                onClearImportError = mainViewModel::clearImportError,
                onRequestDeleteWave = mainViewModel::requestDeleteWave,
                onConfirmDeleteWave = mainViewModel::confirmDeleteWave,
                deleteConfirm = deleteConfirm,
                deleteBlocked = deleteBlocked,
                deleteError = deleteError,
                onClearDeleteConfirm = mainViewModel::clearDeleteConfirm,
                onClearDeleteBlocked = mainViewModel::clearDeleteBlocked,
                onClearDeleteError = mainViewModel::clearDeleteError,
                selectedTab = listsSelectedTab,
                onSelectedTabChange = mainViewModel::selectListsTab,
                selectedWaveNumber = selectedWave?.number,
                onStartWaveDrag = mainViewModel::startWaveDrag,
                onUpdateDragPosition = mainViewModel::updateDragPosition,
                onEndWaveDrag = mainViewModel::endWaveDrag,
                onCancelWaveDrag = mainViewModel::cancelWaveDrag
            )
        }
    }

    val currentDragInfo = dragInfo
    val currentDragPos = dragPosition
    if (currentDragInfo != null && currentDragPos != null) {
        val density = LocalDensity.current
        Box(
            modifier = Modifier
                .offset { IntOffset(currentDragPos.x.toInt() + 16, currentDragPos.y.toInt() - 16) }
                .background(Color(0xDD333333), RoundedCornerShape(4.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "${currentDragInfo.waveNumber}. ${currentDragInfo.waveName}",
                color = Color.White,
                fontSize = 12.sp
            )
        }
    }
    }
}