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
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect
import org.xebia.spdmanager.model.system.fx.subtypes.Reverb
import org.xebia.spdmanager.ui.components.common.ButtonRow
import org.xebia.spdmanager.ui.components.common.SliderWithLabel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ReverbView(
    reverb: Reverb,
    onFxChange: (FxEffect) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Reverb", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        ButtonRow(
            label = "Reverb Type",
            items = ReverbType.entries.toTypedArray(),
            selectedItem = reverb.type,
            onItemSelected = { newType ->
                onFxChange(reverb.copy(type = newType))
            }
        )

        ButtonRow(
            label = "Low Cut",
            items = LowCut.entries.toTypedArray(),
            selectedItem = reverb.lowCut,
            onItemSelected = { newLowCut ->
                onFxChange(reverb.copy(lowCut = newLowCut))
            }
        )

        ButtonRow(
            label = "High Cut",
            items = HighCut.entries.toTypedArray(),
            selectedItem = reverb.highCut,
            onItemSelected = { newHighCut ->
                onFxChange(reverb.copy(highCut = newHighCut))
            }
        )

        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SliderWithLabel(
                label = "Reverb Time",
                value = reverb.reverbTime.toFloat(),
                onValueChange = { newReverbTime ->
                    onFxChange(reverb.copy(reverbTime = newReverbTime.toInt()))
                },
                valueRange = 0f..100f
            )

            SliderWithLabel(
                label = "Pre Delay",
                value = reverb.preDelay.toFloat(),
                onValueChange = { newPreDelay ->
                    onFxChange(reverb.copy(preDelay = newPreDelay.toInt()))
                },
                valueRange = 0f..500f
            )

            SliderWithLabel(
                label = "Density",
                value = reverb.density.toFloat(),
                onValueChange = { newDensity ->
                    onFxChange(reverb.copy(density = newDensity.toInt()))
                },
                valueRange = 0f..100f
            )

            SliderWithLabel(
                label = "Direct Level",
                value = reverb.directLevel.toFloat(),
                onValueChange = { newDirectLevel ->
                    onFxChange(reverb.copy(directLevel = newDirectLevel.toInt()))
                },
                valueRange = 0f..100f
            )

            SliderWithLabel(
                label = "Global Reverb Level",
                value = reverb.glblRevLvl.toFloat(),
                onValueChange = { newGlblRevLvl ->
                    onFxChange(reverb.copy(glblRevLvl = newGlblRevLvl.toInt()))
                },
                valueRange = 0f..100f
            )
        }
    }
}