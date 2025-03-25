package org.xebia.spdmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.model.Device
import org.xebia.spdmanager.model.kit.Kit
import org.xebia.spdmanager.model.kit.pad.Pad

@Composable
fun HomeScreen() {
    var selectedKit by remember { mutableStateOf<Kit?>(null) }
    var selectedPad by remember { mutableStateOf<Pad?>(null) }
    var selectedFolderPath by remember { mutableStateOf<String?>(null) }
    var kits by remember { mutableStateOf<List<Kit>>(emptyList()) }

    val onItemSelected: (Kit) -> Unit = { kit ->
        selectedKit = kit
        selectedPad = kit.pads.values.firstOrNull()
    }

    val onOpenClicked: (String, Device) -> Unit = { folderPath, device ->
        selectedFolderPath = folderPath
        kits = device.kits
    }

    val onPadSelected: (Pad) -> Unit = { pad ->
        selectedPad = pad
    }

    Row {
        Column(Modifier.weight(0.3f).fillMaxHeight()) {
            KitScreen(selectedKit)
        }

        Column(Modifier.weight(0.4f).fillMaxHeight()) {
            PadScreen(
                onSelect = onPadSelected,
                kit = selectedKit,
            )
        }
        Column(Modifier.weight(0.3f).fillMaxHeight()) {
            ListsScreen(kits, onItemSelected, onOpenClicked)
        }
    }
}
