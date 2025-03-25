package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.model.system.fx.subtypes.RingMod
import org.xebia.spdmanager.ui.components.ButtonRow
import org.xebia.spdmanager.ui.components.DryWetMixSlider
import org.xebia.spdmanager.ui.components.SliderWithLabel

@Composable
fun RingModView(ringMod: RingMod) {
    var selectedPolarity by remember { mutableStateOf(ringMod.polarity) }
    var lowGain by remember { mutableStateOf(ringMod.lowGain) }
    var hiGain by remember { mutableStateOf(ringMod.hiGain) }
    var balance by remember { mutableStateOf(ringMod.balance) }
    var level by remember { mutableStateOf(ringMod.level.toFloat()) }

    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Ring Modulator", fontSize = 18.sp)

        // Polarity ButtonRow
        ButtonRow(
            label = "Polarity",
            selectedItem = selectedPolarity,
            items = Polarity.entries.toTypedArray(),
            onItemSelected = { selectedPolarity = it }
        )

        // Low Gain Slider
        SliderWithLabel(
            label = "Low Gain",
            value = lowGain,
            onValueChange = { lowGain = it },
            valueRange = -15f..15f
        )

        // High Gain Slider
        SliderWithLabel(
            label = "High Gain",
            value = hiGain,
            onValueChange = { hiGain = it },
            valueRange = -15f..15f
        )

        // Balance (Dry/Wet Mix) Slider
        DryWetMixSlider(
            label = "Balance (Dry/Wet)",
            value = balance,
            onValueChange = { balance = it }
        )

        // Level Slider
        SliderWithLabel(
            label = "Level",
            value = level,
            onValueChange = { level = it },
            valueRange = 0f..100f
        )
    }
}
