package org.xebia.spdmanager.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.model.Kit
import org.xebia.spdmanager.model.Pad

@Composable
fun HomeScreen(kits: List<Kit>) {
    var selectedKit by remember { mutableStateOf<Kit?>(null) }
    var selectedPad by remember { mutableStateOf<Pad?>(null) }
    var selectedFolderPath by remember { mutableStateOf<String?>(null) }

    val onItemSelected: (Kit) -> Unit = { kit ->
        selectedKit = kit
        selectedPad = kit.pads.values.firstOrNull()
    }

    val onOpenClicked: (String) -> Unit = { folderPath ->
        selectedFolderPath = folderPath
    }

    val onPadSelected: (Pad) -> Unit = { pad ->
        selectedPad = pad
    }

    Row {
        Column(Modifier.weight(0.3f).fillMaxHeight()) {
            KitScreen(selectedKit)
        }

        Column(Modifier.weight(0.4f).fillMaxHeight()) {
            Text("File path: $selectedFolderPath")
            PadScreen(
                onSelect = onPadSelected,
                kit = selectedKit,
            )
        }

        Column(Modifier.weight(0.3f).fillMaxHeight()) {
            TabsScreen(kits, onItemSelected, onOpenClicked)
        }
    }
}
