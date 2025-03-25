package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.model.system.fx.subtypes.Distortion
import org.xebia.spdmanager.ui.components.common.DropdownSelector
import org.xebia.spdmanager.ui.components.common.SliderWithLabel

@Composable
fun DistortionView(distortion: Distortion) {
    var selectedType by remember { mutableStateOf(distortion.type) }
    var drive by remember { mutableStateOf(distortion.drive.toFloat()) }
    var bottom by remember { mutableStateOf(distortion.bottom) }
    var tone by remember { mutableStateOf(distortion.tone) }
    var effectLevel by remember { mutableStateOf(distortion.effectLevel.toFloat()) }

    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Distortion", fontSize = 18.sp)

        // Type DropdownSelector
        DropdownSelector(
            label = "Distortion Type",
            selectedItem = selectedType,
            items = DistortionType.entries,
            onItemSelected = { selectedType = it }
        )

        // Drive Slider
        SliderWithLabel(
            label = "Drive",
            value = drive,
            onValueChange = { drive = it },
            valueRange = 0f..100f
        )

        // Bottom Slider
        SliderWithLabel(
            label = "Bottom",
            value = bottom,
            onValueChange = { bottom = it },
            valueRange = 0f..100f
        )

        // Tone Slider
        SliderWithLabel(
            label = "Tone",
            value = tone,
            onValueChange = { tone = it },
            valueRange = 0f..100f
        )

        // Effect Level Slider
        SliderWithLabel(
            label = "Effect Level",
            value = effectLevel,
            onValueChange = { effectLevel = it },
            valueRange = 0f..100f
        )
    }
}
