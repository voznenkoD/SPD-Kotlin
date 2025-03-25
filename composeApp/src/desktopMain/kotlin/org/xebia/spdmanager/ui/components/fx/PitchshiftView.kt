package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.subtypes.Pitchshift
import org.xebia.spdmanager.ui.components.SliderWithLabel

@Composable
fun PitchshiftView(pitchshift: Pitchshift) {
    var fine by remember { mutableStateOf(pitchshift.fine) }
    var effectLevel by remember { mutableStateOf(pitchshift.effectLevel.toFloat()) }
    var directLevel by remember { mutableStateOf(pitchshift.directLevel.toFloat()) }

    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Pitch Shift", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        // Fine Pitch Control (-50 to +50 cents)
        SliderWithLabel(
            label = "Fine",
            value = fine,
            onValueChange = { fine = it },
            valueRange = -50f..50f
        )

        // Effect Level (0 to 100)
        SliderWithLabel(
            label = "Effect Level",
            value = effectLevel,
            onValueChange = { effectLevel = it },
            valueRange = 0f..100f
        )

        // Direct Level (0 to 100)
        SliderWithLabel(
            label = "Direct Level",
            value = directLevel,
            onValueChange = { directLevel = it },
            valueRange = 0f..100f
        )
    }
}
