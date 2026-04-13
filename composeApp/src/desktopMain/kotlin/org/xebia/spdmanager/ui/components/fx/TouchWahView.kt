package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect
import org.xebia.spdmanager.model.system.fx.subtypes.TouchWah
import org.xebia.spdmanager.ui.components.common.SliderWithLabel
import org.xebia.spdmanager.ui.components.common.ToggleSwitchWithLabel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TouchWahView(
    touchWah: TouchWah,
    onFxChange: (FxEffect) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Touch Wah", fontSize = 18.sp)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                ToggleSwitchWithLabel(
                    label = "Polarity",
                    selectedItem = touchWah.polarity,
                    offItem = Polarity.DOWN,
                    onItem = Polarity.UP,
                    onItemSelected = { newPolarity ->
                        onFxChange(touchWah.copy(polarity = newPolarity))
                    },
                    offLabel = "Down",
                    onLabel = "Up"
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                ToggleSwitchWithLabel(
                    label = "Wah Mode",
                    selectedItem = touchWah.mode,
                    offItem = WahMode.LPF,
                    onItem = WahMode.BPF,
                    onItemSelected = { newMode ->
                        onFxChange(touchWah.copy(mode = newMode))
                    },
                    offLabel = "LPF",
                    onLabel = "BPF"
                )
            }
        }

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