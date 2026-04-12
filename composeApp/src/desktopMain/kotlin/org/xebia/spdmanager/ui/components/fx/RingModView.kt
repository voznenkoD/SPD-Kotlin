package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect
import org.xebia.spdmanager.model.system.fx.subtypes.RingMod
import org.xebia.spdmanager.ui.components.common.ButtonRow
import org.xebia.spdmanager.ui.components.common.DryWetMixSlider
import org.xebia.spdmanager.ui.components.common.SliderWithLabel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RingModView(
    ringMod: RingMod,
    onFxChange: (FxEffect) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Ring Modulator", fontSize = 18.sp)

        ButtonRow(
            label = "Polarity",
            selectedItem = ringMod.polarity,
            items = Polarity.entries.toTypedArray(),
            onItemSelected = { newPolarity ->
                onFxChange(ringMod.copy(polarity = newPolarity))
            }
        )

        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SliderWithLabel(
                label = "Low Gain",
                value = ringMod.lowGain,
                onValueChange = { newLowGain ->
                    onFxChange(ringMod.copy(lowGain = newLowGain))
                },
                valueRange = -15f..15f, bipolar = true
            )

            SliderWithLabel(
                label = "High Gain",
                value = ringMod.hiGain,
                onValueChange = { newHiGain ->
                    onFxChange(ringMod.copy(hiGain = newHiGain))
                },
                valueRange = -15f..15f, bipolar = true
            )
        }

        DryWetMixSlider(
            label = "Balance (Dry/Wet)",
            value = ringMod.balance,
            onValueChange = { newBalance ->
                onFxChange(ringMod.copy(balance = newBalance))
            }
        )

        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SliderWithLabel(
                label = "Level",
                value = ringMod.level.toFloat(),
                onValueChange = { newLevel ->
                    onFxChange(ringMod.copy(level = newLevel.toInt()))
                },
                valueRange = 0f..100f
            )
        }
    }
}