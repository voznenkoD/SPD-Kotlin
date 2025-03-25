package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.common.HighCut
import org.xebia.spdmanager.model.system.fx.common.LowCut
import org.xebia.spdmanager.model.system.fx.common.ReverbType
import org.xebia.spdmanager.model.system.fx.subtypes.Reverb
import org.xebia.spdmanager.ui.components.ButtonRow
import org.xebia.spdmanager.ui.components.SliderWithLabel

@Composable
fun ReverbView(reverb: Reverb) {
    var selectedType by remember { mutableStateOf(reverb.type) }
    var selectedLowCut by remember { mutableStateOf(reverb.lowCut) }
    var selectedHighCut by remember { mutableStateOf(reverb.highCut) }
    var reverbTime by remember { mutableStateOf(reverb.reverbTime.toFloat()) }
    var preDelay by remember { mutableStateOf(reverb.preDelay.toFloat()) }
    var density by remember { mutableStateOf(reverb.density.toFloat()) }
    var directLevel by remember { mutableStateOf(reverb.directLevel.toFloat()) }
    var globalReverbLevel by remember { mutableStateOf(reverb.glblRevLvl.toFloat()) }

    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Reverb", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        // Reverb Type Selection
        ButtonRow(
            label = "Reverb Type",
            items = ReverbType.entries.toTypedArray(),
            selectedItem = selectedType,
            onItemSelected = { selectedType = it }
        )

        // Low Cut Selection
        ButtonRow(
            label = "Low Cut",
            items = LowCut.entries.toTypedArray(),
            selectedItem = selectedLowCut,
            onItemSelected = { selectedLowCut = it }
        )

        // High Cut Selection
        ButtonRow(
            label = "High Cut",
            items = HighCut.entries.toTypedArray(),
            selectedItem = selectedHighCut,
            onItemSelected = { selectedHighCut = it }
        )

        // Sliders
        SliderWithLabel(
            label = "Reverb Time",
            value = reverbTime,
            onValueChange = { reverbTime = it },
            valueRange = 0f..100f
        )

        SliderWithLabel(
            label = "Pre Delay",
            value = preDelay,
            onValueChange = { preDelay = it },
            valueRange = 0f..500f
        )

        SliderWithLabel(
            label = "Density",
            value = density,
            onValueChange = { density = it },
            valueRange = 0f..100f
        )

        SliderWithLabel(
            label = "Direct Level",
            value = directLevel,
            onValueChange = { directLevel = it },
            valueRange = 0f..100f
        )

        SliderWithLabel(
            label = "Global Reverb Level",
            value = globalReverbLevel,
            onValueChange = { globalReverbLevel = it },
            valueRange = 0f..100f
        )
    }
}
