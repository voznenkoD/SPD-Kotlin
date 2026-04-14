package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.ui.theme.*
import org.xebia.spdmanager.ui.theme.Typography
import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect
import org.xebia.spdmanager.model.system.fx.subtypes.RingMod
import org.xebia.spdmanager.ui.components.common.DryWetMixSlider
import org.xebia.spdmanager.ui.components.common.ToggleSwitchWithLabel
import org.xebia.spdmanager.ui.components.common.SliderWithLabel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RingModView(
    ringMod: RingMod,
    onFxChange: (FxEffect) -> Unit
) {
    Column(modifier = Modifier.padding(Spacing.m).fillMaxWidth()) {
        Text("Ring Modulator", fontSize = Typography.titleSize)

        ToggleSwitchWithLabel(
            label = "Polarity",
            selectedItem = ringMod.polarity,
            offItem = Polarity.DOWN,
            onItem = Polarity.UP,
            onItemSelected = { newPolarity ->
                onFxChange(ringMod.copy(polarity = newPolarity))
            },
            offLabel = "Down",
            onLabel = "Up"
        )

        FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xl)) {
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
            DryWetMixSlider(
                label = "Balance (Dry/Wet)",
                value = ringMod.balance,
                onValueChange = { newBalance ->
                    onFxChange(ringMod.copy(balance = newBalance))
                }
            )
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