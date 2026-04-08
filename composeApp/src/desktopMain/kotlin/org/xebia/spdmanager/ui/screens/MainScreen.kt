package org.xebia.spdmanager.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.LocalDeviceManager
import org.xebia.spdmanager.model.list.WaveListsHolder
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

    val kits = device?.kits.orEmpty()
    val waves = device?.waves.orEmpty()
    val waveListsHolder = device?.waveLists ?: WaveListsHolder(emptyList(), emptyMap(), emptyMap())

    if (device == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            SelectFolderButton()
        }
        return
    }

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
                .weight(0.4f)
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
                hasCopiedPad = clipboardPad != null
            )

            WaveDetailsScreen(
                wave = selectedWave,
                device = device
            )
        }

        Column(Modifier.weight(0.3f).fillMaxHeight()) {
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
                onMoveKit = mainViewModel::moveKit
            )
        }
    }
}