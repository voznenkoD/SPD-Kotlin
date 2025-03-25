package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.model.system.fx.subtypes.Flanger
import org.xebia.spdmanager.ui.components.common.ButtonRow
import org.xebia.spdmanager.ui.components.common.SliderWithLabel

@Composable
fun FlangerView(fx: Flanger) {
    var selectedRateSync by remember { mutableStateOf(fx.rateSync) }
    var manual by remember { mutableStateOf(fx.manual.toFloat()) }
    var resonance by remember { mutableStateOf(fx.resonance.toFloat()) }
    var separation by remember { mutableStateOf(fx.separation.toFloat()) }
    var selectedLowCut by remember { mutableStateOf(fx.lowCut) }
    var effectLevel by remember { mutableStateOf(fx.effectLevel.toFloat()) }
    var directLevel by remember { mutableStateOf(fx.directLevel.toFloat()) }

    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Flanger", fontSize = 18.sp)

        // Rate Sync (Switchable Button)
        ButtonRow(
            label = "Rate Sync",
            selectedItem = selectedRateSync,
            items = SyncSwitch.entries.toTypedArray(),
            onItemSelected = { selectedRateSync = it }
        )

        // Manual (0..100)
        SliderWithLabel(
            label = "Manual",
            value = manual,
            onValueChange = { manual = it },
            valueRange = 0f..100f
        )

        // Resonance (0..100)
        SliderWithLabel(
            label = "Resonance",
            value = resonance,
            onValueChange = { resonance = it },
            valueRange = 0f..100f
        )

        // Separation (0..100)
        SliderWithLabel(
            label = "Separation",
            value = separation,
            onValueChange = { separation = it },
            valueRange = 0f..100f
        )

        // Low Cut (Dropdown of LowCut values)
        ButtonRow(
            label = "Low Cut",
            selectedItem = selectedLowCut,
            items = LowCut.entries.toTypedArray(),
            onItemSelected = { selectedLowCut = it }
        )

        // Effect Level (0..100)
        SliderWithLabel(
            label = "Effect Level",
            value = effectLevel,
            onValueChange = { effectLevel = it },
            valueRange = 0f..100f
        )

        // Direct Level (0..100)
        SliderWithLabel(
            label = "Direct Level",
            value = directLevel,
            onValueChange = { directLevel = it },
            valueRange = 0f..100f
        )
    }
}
