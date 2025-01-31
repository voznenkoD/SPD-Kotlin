package org.xebia.spdmanager.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.model.Kit
import org.xebia.spdmanager.model.Pad
import org.xebia.spdmanager.model.Sound

@Composable
fun HomeScreen() {
    var selectedItem by remember { mutableStateOf<String?>(null) }
    var selectedPad by remember { mutableStateOf<Pad?>(null) }

    val onItemSelected: (String) -> Unit = { item ->
        selectedItem = item
    }

    val onPadSelected: (Pad) -> Unit = { pad ->
        selectedPad = pad
    }

    Row {
        Column(Modifier.weight(0.3f).fillMaxHeight()) {
            Text("Hello")
        }

        Column(Modifier.weight(0.4f).fillMaxHeight()) {
            Text("Selected: $selectedItem")
            PadScreen(
                onSelect = onPadSelected,
                kit = Kit(
                    name = "$selectedItem",
                    subName = "subName",
                    tempo = 120.0,
                    volume = 20,
                    records = "one" to "two",
                    pads = mapOf(
                        "PAD1" to Pad(mainSound = Sound(1), subSound = Sound(2)),
                        "PAD2" to Pad(mainSound = Sound(3), subSound = Sound(4)),
                        "PAD3" to Pad(mainSound = Sound(5), subSound = Sound(6)),
                        "TRI1" to Pad(mainSound = Sound(7), subSound = Sound(8)),
                        "TRI2" to Pad(mainSound = Sound(9), subSound = Sound(10)),
                        "TRI3" to Pad(mainSound = Sound(11), subSound = Sound(12)),
                    )
                )
            )
        }

        Column(Modifier.weight(0.3f).fillMaxHeight()) {
            TabsScreen(onItemSelected)
        }
    }
}
