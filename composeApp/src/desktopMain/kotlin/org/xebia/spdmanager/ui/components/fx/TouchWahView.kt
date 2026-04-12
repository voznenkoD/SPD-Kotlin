package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect
import org.xebia.spdmanager.model.system.fx.subtypes.TouchWah
import org.xebia.spdmanager.ui.components.common.ButtonRow
import org.xebia.spdmanager.ui.components.common.SliderWithLabel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TouchWahView(
    touchWah: TouchWah,
    onFxChange: (FxEffect) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Touch Wah", fontSize = 18.sp)

        ButtonRow(
            label = "Mode",
            selectedItem = touchWah.mode,
            items = WahMode.entries.toTypedArray(),
            onItemSelected = { newMode ->
                onFxChange(touchWah.copy(mode = newMode))
            }
        )

        ButtonRow(
            label = "Polarity",
            selectedItem = touchWah.polarity,
            items = Polarity.entries.toTypedArray(),
            onItemSelected = { newPolarity ->
                onFxChange(touchWah.copy(polarity = newPolarity))
            }
        )

        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SliderWithLabel(
                label = "Peak",
                value = touchWah.peak.toFloat(),
                onValueChange = { newPeak ->
                    onFxChange(touchWah.copy(peak = newPeak.toInt()))
                },
                valueRange = 0f..100f
            )

            SliderWithLabel(
                label = "Effect Level",
                value = touchWah.effectLevel.toFloat(),
                onValueChange = { newEffectLevel ->
                    onFxChange(touchWah.copy(effectLevel = newEffectLevel.toInt()))
                },
                valueRange = 0f..100f
            )

            SliderWithLabel(
                label = "Direct Level",
                value = touchWah.directLevel.toFloat(),
                onValueChange = { newDirectLevel ->
                    onFxChange(touchWah.copy(directLevel = newDirectLevel.toInt()))
                },
                valueRange = 0f..100f
            )
        }
    }
}