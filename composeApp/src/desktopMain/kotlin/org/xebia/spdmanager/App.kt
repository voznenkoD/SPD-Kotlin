package org.xebia.spdmanager

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.xebia.spdmanager.model.Kit
import org.xebia.spdmanager.model.Pad
import org.xebia.spdmanager.model.Sound
import org.xebia.spdmanager.screens.HomeScreen

@Composable
@Preview
fun App() {
    val kits = List(20) { index ->
        Kit(
            name = "Kit ${index + 1}",
            subName = "SubKit ${index + 1}",
            tempo = (60 + index * 5).toDouble(),
            volume = (50 + index * 2),
            records = Pair("Record ${index + 1}", "Record ${index + 1} Sub"),
            pads = mapOf(
                "PAD1" to Pad(mainSound = Sound(1), subSound = Sound(2)),
                "PAD2" to Pad(mainSound = Sound(3), subSound = Sound(4)),
                "PAD3" to Pad(mainSound = Sound(5), subSound = Sound(6)),
                "TRI1" to Pad(mainSound = Sound(7), subSound = Sound(8)),
                "TRI2" to Pad(mainSound = Sound(9), subSound = Sound(10)),
                "TRI3" to Pad(mainSound = Sound(11), subSound = Sound(12))
            )
        )
    }

    MaterialTheme {
        HomeScreen(kits)
    }
}