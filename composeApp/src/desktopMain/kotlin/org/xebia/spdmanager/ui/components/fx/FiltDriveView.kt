package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.subtypes.FiltDrive
import org.xebia.spdmanager.ui.components.common.SliderWithLabel

@Composable
fun FiltDriveView(fx: FiltDrive) {
    var resonance by remember { mutableStateOf(fx.resonance) }
    var level by remember { mutableStateOf(fx.level) }

    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Filt Drive Settings", fontSize = 18.sp)

        // Resonance (Slider)
        SliderWithLabel(
            label = "Resonance",
            value = resonance.toFloat(),
            onValueChange = { resonance = it.toInt() },
            valueRange = 0f..100f
        )

        // Level (Slider)
        SliderWithLabel(
            label = "Level",
            value = level.toFloat(),
            onValueChange = { level = it.toInt() },
            valueRange = 0f..100f
        )
    }
}