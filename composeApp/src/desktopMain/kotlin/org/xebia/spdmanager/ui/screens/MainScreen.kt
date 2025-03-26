package org.xebia.spdmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.LocalDeviceManager
import org.xebia.spdmanager.model.Wave
import org.xebia.spdmanager.model.kit.Kit
import org.xebia.spdmanager.model.kit.pad.Pad
import org.xebia.spdmanager.model.list.ListedWave
import org.xebia.spdmanager.model.list.WaveListsHolder
import org.xebia.spdmanager.ui.components.SelectFolderButton

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
    var selectedFolderPath by remember { mutableStateOf<String?>(device?.rootPath) }

    val onKitSelected: (Kit) -> Unit = { kit ->
        selectedKit = kit
        selectedPad = kit.pads.values.firstOrNull()
    }

    val onWaveSelected: (ListedWave) -> Unit = { listedWave ->
        selectedWave = waves.find { it.number == listedWave.number }
    }

    val onOpenClicked: (String) -> Unit = { folderPath ->
        selectedFolderPath = folderPath
        deviceManager.readDevice(folderPath)
    }

    val onPadSelected: (Pad) -> Unit = { pad ->
        selectedPad = pad
    }

    if (device == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            SelectFolderButton(onOpenClicked)
        }
        return
    }

    Row {
        Column(Modifier.weight(0.3f).fillMaxHeight()) {
            DetailsTabs(selectedKit, selectedPad)
        }

        Column(Modifier.weight(0.4f).fillMaxHeight()) {
            PadScreen(
                onSelect = onPadSelected,
                kit = selectedKit,
            )
            WaveDetailsScreen(wave = selectedWave, selectedFolderPath)
        }

        Column(Modifier.weight(0.3f).fillMaxHeight()) {
            ListsScreen(kits, waveListsHolder, onKitSelected, onWaveSelected, onOpenClicked)
            Text("Selected Folder: $selectedFolderPath", fontSize = 12.sp)
        }
    }
}