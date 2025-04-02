package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.common.ChorusMode
import org.xebia.spdmanager.model.system.fx.common.HighCut
import org.xebia.spdmanager.model.system.fx.common.LowCut
import org.xebia.spdmanager.model.system.fx.subtypes.Chorus
import org.xebia.spdmanager.ui.components.common.ButtonRow
import org.xebia.spdmanager.ui.components.common.IntStepSliderWithLabel
import org.xebia.spdmanager.ui.components.common.SliderWithLabel

@Composable
fun ChorusView(chorus: Chorus) {
    var selectedMode by remember { mutableStateOf(chorus.mode) }
    var rate by remember { mutableStateOf(chorus.rate) }
    var preDelay by remember { mutableStateOf(chorus.preDelay) }
    var selectedLowCut by remember { mutableStateOf(chorus.lowCut) }
    var selectedHighCut by remember { mutableStateOf(chorus.highCut) }
    var directLevel by remember { mutableStateOf(chorus.directLevel) }

    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Chorus", fontSize = 18.sp)

        // Chorus Mode Selector (Row of Buttons)
        ButtonRow(
            label = "Mode",
            selectedItem = selectedMode,
            items = ChorusMode.entries.toTypedArray(),
            onItemSelected = { selectedMode = it }
        )

        // Rate (0..100)
        IntStepSliderWithLabel(
            label = "Rate",
            value = rate,
            onValueChange = { rate = it },
            range = 0..100
        )

        // Pre-Delay (0..100)
        IntStepSliderWithLabel(
            label = "Pre-Delay",
            value = preDelay,
            onValueChange = { preDelay = it },
            range = 0..100
        )

        // Low Cut Selector
        ButtonRow(
            label = "Low Cut",
            selectedItem = selectedLowCut,
            items = LowCut.entries.toTypedArray(),
            onItemSelected = { selectedLowCut = it }
        )

        // High Cut Selector
        ButtonRow(
            label = "High Cut",
            selectedItem = selectedHighCut,
            items = HighCut.entries.toTypedArray(),
            onItemSelected = { selectedHighCut = it }
        )

        IntStepSliderWithLabel(
            label = "Direct Level",
            value = directLevel,
            onValueChange = { directLevel = it },
            range = 0..100
        )
    }
}
