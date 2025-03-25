package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.model.system.fx.subtypes.Compressor
import org.xebia.spdmanager.ui.components.ButtonRow
import org.xebia.spdmanager.ui.components.SliderWithLabel

@Composable
fun CompressorView(fx: Compressor) {
    var threshold by remember { mutableStateOf(fx.threshold) }
    var attack by remember { mutableStateOf(fx.attack) }
    var release by remember { mutableStateOf(fx.release) }
    var selectedRatio by remember { mutableStateOf(fx.ratio) }
    var selectedKnee by remember { mutableStateOf(fx.knee) }
    var makeup by remember { mutableStateOf(fx.makeup) }

    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Compressor Settings", fontSize = 18.sp)

        // Threshold (-80 to 0 dB)
        SliderWithLabel(
            label = "Threshold",
            value = threshold,
            onValueChange = { threshold = it },
            valueRange = -80f..0f
        )

        // Attack (1 to 1000 ms)
        SliderWithLabel(
            label = "Attack",
            value = attack.toFloat(),
            onValueChange = { attack = it.toInt() },
            valueRange = 1f..1000f
        )

        // Release (1 to 1000 ms)
        SliderWithLabel(
            label = "Release",
            value = release.toFloat(),
            onValueChange = { release = it.toInt() },
            valueRange = 1f..1000f
        )

        // Ratio (Button Row)
        ButtonRow(
            label = "Ratio",
            selectedItem = selectedRatio,
            items = Ratio.entries.toTypedArray(),
            onItemSelected = { selectedRatio = it }
        )

        // Knee (Button Row)
        ButtonRow(
            label = "Knee",
            selectedItem = selectedKnee,
            items = Knee.entries.toTypedArray(),
            onItemSelected = { selectedKnee = it }
        )

        // Makeup Gain (0 to 30 dB)
        SliderWithLabel(
            label = "Makeup Gain",
            value = makeup.toFloat(),
            onValueChange = { makeup = it.toInt() },
            valueRange = 0f..30f
        )
    }
}

