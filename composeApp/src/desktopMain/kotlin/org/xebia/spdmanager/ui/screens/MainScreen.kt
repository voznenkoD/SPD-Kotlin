package org.xebia.spdmanager.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.LocalDeviceManager
import org.xebia.spdmanager.model.Wave
import org.xebia.spdmanager.model.kit.Kit
import org.xebia.spdmanager.model.kit.pad.Pad
import org.xebia.spdmanager.model.list.ListedWave
import org.xebia.spdmanager.model.list.WaveListsHolder
import org.xebia.spdmanager.ui.components.common.SelectFolderButton
import org.xebia.spdmanager.ui.components.kit.DetailsTabs
import org.xebia.spdmanager.ui.components.lists.ListsScreen
import org.xebia.spdmanager.ui.components.pad.PadScreen

@Composable
fun MainScreen() {
    val deviceManager = LocalDeviceManager.current
    val device by remember { derivedStateOf { deviceManager.device } }

    val kits = device?.kits.orEmpty()
    val waves = device?.waves.orEmpty()
    val waveListsHolder = device?.waveLists ?: WaveListsHolder(emptyList(), emptyMap(), emptyMap())

    var selectedKit by remember { mutableStateOf<Kit?>(null) }
    var selectedWave by remember { mutableStateOf<Wave?>(null) }
    var selectedPad by remember { mutableStateOf<Pad?>(null) }

    val onKitSelected: (Kit) -> Unit = { kit ->
        selectedKit = kit
        selectedPad = kit.pads.values.firstOrNull()
    }

    val onWaveSelected: (ListedWave) -> Unit = { listedWave ->
        selectedWave = waves.find { it.number == listedWave.number }
    }

    val onPadSelected: (Pad) -> Unit = { pad ->
        selectedPad = pad
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

    Row {
        Column(Modifier.weight(0.3f).fillMaxHeight()) {
            DetailsTabs(selectedKit, selectedPad)
        }

        Column(Modifier.weight(0.4f).fillMaxHeight().border(width = 2.dp, color = Color.DarkGray)) {
            PadScreen(
                onSelect = onPadSelected,
                kit = selectedKit,
            )
            WaveDetailsScreen(wave = selectedWave, device = device)
        }

        Column(Modifier.weight(0.3f).fillMaxHeight()) {
            ListsScreen(kits, waveListsHolder, onKitSelected, onWaveSelected)
        }
    }
}