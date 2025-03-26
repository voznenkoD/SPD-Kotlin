package org.xebia.spdmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.model.Device
import org.xebia.spdmanager.model.Wave
import org.xebia.spdmanager.model.kit.Kit
import org.xebia.spdmanager.model.kit.pad.Pad
import org.xebia.spdmanager.model.list.ListedWave
import org.xebia.spdmanager.model.list.WaveListsHolder

@Composable
fun HomeScreen() {
    var selectedKit by remember { mutableStateOf<Kit?>(null) }
    var selectedWave by remember { mutableStateOf<Wave?>(null) }
    var selectedPad by remember { mutableStateOf<Pad?>(null) }
    var selectedFolderPath by remember { mutableStateOf<String?>(null) }
    var kits by remember { mutableStateOf<List<Kit>>(emptyList()) }
    var waves by remember { mutableStateOf<List<Wave>>(emptyList()) }
    var waveListsHolder by remember { mutableStateOf(WaveListsHolder(emptyList(), emptyMap(), emptyMap())) }

    val onKitSelected: (Kit) -> Unit = { kit ->
        selectedKit = kit
        selectedPad = kit.pads.values.firstOrNull()
    }

    val onWaveSelected: (ListedWave) -> Unit = { listedWave ->
        selectedWave = waves.find { it.number == listedWave.number }
    }

    val onOpenClicked: (String, Device) -> Unit = { folderPath, device ->
        selectedFolderPath = folderPath
        kits = device.kits
        waves = device.waves
        waveListsHolder = device.waveLists
    }

    val onPadSelected: (Pad) -> Unit = { pad ->
        selectedPad = pad
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
            WaveDetailsScreen(wave = selectedWave)
        }
        Column(Modifier.weight(0.3f).fillMaxHeight()) {
            ListsScreen(kits, waveListsHolder, onKitSelected, onWaveSelected, onOpenClicked)
        }
    }
}
