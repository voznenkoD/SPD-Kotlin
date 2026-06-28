package org.xebia.spdmanager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.ui.theme.*
import org.xebia.spdmanager.LocalDeviceManager
import org.xebia.spdmanager.model.list.WaveListsHolder
import org.xebia.spdmanager.service.DeviceManager
import org.xebia.spdmanager.ui.components.common.SelectFolderButton
import org.xebia.spdmanager.ui.components.kit.DetailsTabs
import org.xebia.spdmanager.ui.components.lists.ListsScreen
import org.xebia.spdmanager.ui.components.pad.PadScreen
import org.xebia.spdmanager.ui.panels.DetachablePanelWindow
import org.xebia.spdmanager.ui.panels.PanelLayoutState
import org.xebia.spdmanager.ui.panels.PanelRegion
import org.xebia.spdmanager.ui.panels.ScreenId
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

    val clipboardPad by mainViewModel.clipboardPad.collectAsState()
    val clipboardKit by mainViewModel.clipboardKit.collectAsState()

    val leftContent: @Composable () -> Unit = {
        DetailsTabs(
            kitIndex = selectedKitIndex,
            pad = selectedPad,
            padNumber = selectedPadNumber,
            deviceManager = deviceManager
        )
    }

    val bottomContent: @Composable () -> Unit = {
        WaveDetailsScreen(
            wave = selectedWave,
            device = device,
            onWaveChange = { updated -> mainViewModel.updateSelectedWave { updated } }
        )
    }

    val rightContent: @Composable () -> Unit = {
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
            selectedKitIndex = selectedKitIndex,
            onStartWaveDrag = mainViewModel::startWaveDrag,
            onUpdateDragPosition = mainViewModel::updateDragPosition,
            onEndWaveDrag = mainViewModel::endWaveDrag,
            onCancelWaveDrag = mainViewModel::cancelWaveDrag
        )
    }

    val leftEntry = PanelLayoutState.entry(ScreenId.Main, PanelRegion.Left)
    val rightEntry = PanelLayoutState.entry(ScreenId.Main, PanelRegion.Right)
    val bottomEntry = PanelLayoutState.entry(ScreenId.Main, PanelRegion.Bottom)

    Box(modifier = Modifier.fillMaxSize()) {
    Row {
        if (leftEntry.docked) {
            Column(Modifier.weight(0.25f).fillMaxHeight()) {
                leftContent()
            }
        }

        Column(
            Modifier
                .weight(0.6f)
                .fillMaxHeight()
                .border(width = 2.dp, color = ColorDivider)
        ) {
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
                onUnregisterPadBounds = mainViewModel::unregisterPadBounds,
                onExternalWaveDrop = mainViewModel::dropExternalWaveFileOnPad
            )

            if (bottomEntry.docked) {
                bottomContent()
            }
        }

        if (rightEntry.docked) {
            Column(Modifier.weight(0.15f).fillMaxHeight()) {
                rightContent()
            }
        }
    }

    val currentDragInfo = dragInfo
    val currentDragPos = dragPosition
    if (currentDragInfo != null && currentDragPos != null) {
        Box(
            modifier = Modifier
                .offset { IntOffset(currentDragPos.x.toInt() + 16, currentDragPos.y.toInt() - 16) }
                .background(ColorSurfaceSelected, ShapeCard)
                .padding(horizontal = Spacing.xl, vertical = Spacing.m)
        ) {
            Text(
                text = "${currentDragInfo.waveNumber}. ${currentDragInfo.waveName}",
                color = ColorTextOnAccent,
                fontSize = Typography.captionSize
            )
        }
    }
    }

    DetachablePanelWindow(leftEntry, PanelRegion.Left, leftContent)
    DetachablePanelWindow(rightEntry, PanelRegion.Right, rightContent)
    DetachablePanelWindow(bottomEntry, PanelRegion.Bottom, bottomContent)
}