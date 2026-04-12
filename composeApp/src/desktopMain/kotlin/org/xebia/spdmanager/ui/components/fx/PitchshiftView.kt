package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect
import org.xebia.spdmanager.model.system.fx.subtypes.Pitchshift
import org.xebia.spdmanager.ui.components.common.SliderWithLabel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PitchshiftView(
    pitchshift: Pitchshift,
    onFxChange: (FxEffect) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Pitch Shift", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SliderWithLabel(
                label = "Fine",
                value = pitchshift.fine,
                onValueChange = { newFine ->
                    onFxChange(pitchshift.copy(fine = newFine))
                },
                valueRange = -50f..50f, bipolar = true
            )

            SliderWithLabel(
                label = "Effect Level",
                value = pitchshift.effectLevel.toFloat(),
                onValueChange = { newEffectLevel ->
                    onFxChange(pitchshift.copy(effectLevel = newEffectLevel.toInt()))
                },
                valueRange = 0f..100f
            )

            SliderWithLabel(
                label = "Direct Level",
                value = pitchshift.directLevel.toFloat(),
                onValueChange = { newDirectLevel ->
                    onFxChange(pitchshift.copy(directLevel = newDirectLevel.toInt()))
                },
                valueRange = 0f..100f
            )
        }
    }
}