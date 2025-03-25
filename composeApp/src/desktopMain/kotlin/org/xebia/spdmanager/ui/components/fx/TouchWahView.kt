package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.model.system.fx.subtypes.TouchWah
import org.xebia.spdmanager.ui.components.ButtonRow
import org.xebia.spdmanager.ui.components.SliderWithLabel

@Composable
fun TouchWahView(touchWah: TouchWah) {
    var selectedMode by remember { mutableStateOf(touchWah.mode) }
    var selectedPolarity by remember { mutableStateOf(touchWah.polarity) }
    var peak by remember { mutableStateOf(touchWah.peak.toFloat()) }
    var effectLevel by remember { mutableStateOf(touchWah.effectLevel.toFloat()) }
    var directLevel by remember { mutableStateOf(touchWah.directLevel.toFloat()) }

    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Touch Wah", fontSize = 18.sp)


        // Mode Button Row
        ButtonRow(
            label = "Mode",
            selectedItem = selectedMode,
            items = WahMode.entries.toTypedArray(),
            onItemSelected = { selectedMode = it }
        )

        // Polarity Button Row
        ButtonRow(
            label = "Polarity",
            selectedItem = selectedPolarity,
            items = Polarity.entries.toTypedArray(),
            onItemSelected = { selectedPolarity = it }
        )

        // Peak Slider
        SliderWithLabel(
            label = "Peak",
            value = peak,
            onValueChange = { peak = it },
            valueRange = 0f..100f
        )

        // Effect Level Slider
        SliderWithLabel(
            label = "Effect Level",
            value = effectLevel,
            onValueChange = { effectLevel = it },
            valueRange = 0f..100f
        )

        // Direct Level Slider
        SliderWithLabel(
            label = "Direct Level",
            value = directLevel,
            onValueChange = { directLevel = it },
            valueRange = 0f..100f
        )
    }
}
