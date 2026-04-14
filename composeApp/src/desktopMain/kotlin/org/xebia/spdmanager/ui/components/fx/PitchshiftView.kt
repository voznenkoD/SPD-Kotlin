package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.ui.theme.*
import org.xebia.spdmanager.ui.theme.Typography
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect
import org.xebia.spdmanager.model.system.fx.subtypes.Pitchshift
import org.xebia.spdmanager.ui.components.common.SliderWithLabel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PitchshiftView(
    pitchshift: Pitchshift,
    onFxChange: (FxEffect) -> Unit
) {
    Column(modifier = Modifier.padding(Spacing.m).fillMaxWidth()) {
        Text("Pitch Shift", fontSize = Typography.titleSize)

        FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xl)) {
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