package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.ui.theme.*
import org.xebia.spdmanager.ui.theme.Typography
import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect
import org.xebia.spdmanager.model.system.fx.subtypes.StepFLNGR
import org.xebia.spdmanager.ui.components.common.DropdownSelector
import org.xebia.spdmanager.ui.components.common.SliderWithLabel
import org.xebia.spdmanager.ui.components.common.ToggleSwitchWithLabel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StepFlangerView(
    fx: StepFLNGR,
    onFxChange: (FxEffect) -> Unit
) {
    Column(modifier = Modifier.padding(Spacing.xxl).fillMaxWidth()) {
        Text("Step Flanger", fontSize = Typography.titleSize)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.xl)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                ToggleSwitchWithLabel(
                    label = "Step Sync",
                    selectedItem = fx.stepSync,
                    offItem = SyncSwitch.OFF,
                    onItem = SyncSwitch.ON,
                    onItemSelected = { newStepSync ->
                        onFxChange(fx.copy(stepSync = newStepSync))
                    }
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                ToggleSwitchWithLabel(
                    label = "Rate Sync",
                    selectedItem = fx.rateSync,
                    offItem = SyncSwitch.OFF,
                    onItem = SyncSwitch.ON,
                    onItemSelected = { newRateSync ->
                        onFxChange(fx.copy(rateSync = newRateSync))
                    }
                )
            }
        }

        FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xl)) {
            SliderWithLabel(
                label = "Depth",
                value = fx.depth.toFloat(),
                onValueChange = { newDepth ->
                    onFxChange(fx.copy(depth = newDepth.toInt()))
                },
                valueRange = 0f..100f
            )

            SliderWithLabel(
                label = "Manual",
                value = fx.manual.toFloat(),
                onValueChange = { newManual ->
                    onFxChange(fx.copy(manual = newManual.toInt()))
                },
                valueRange = 0f..100f
            )

            SliderWithLabel(
                label = "Resonance",
                value = fx.resonance.toFloat(),
                onValueChange = { newResonance ->
                    onFxChange(fx.copy(resonance = newResonance.toInt()))
                },
                valueRange = 0f..100f
            )
        }

        DropdownSelector(
            label = "Low Cut (Hz)",
            selectedItem = fx.lowCut,
            items = LowCut.entries.toList(),
            onItemSelected = { newLowCut ->
                onFxChange(fx.copy(lowCut = newLowCut))
            }
        )

        FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xl)) {
            SliderWithLabel(
                label = "Effect Level",
                value = fx.effectLevel.toFloat(),
                onValueChange = { newEffectLevel ->
                    onFxChange(fx.copy(effectLevel = newEffectLevel.toInt()))
                },
                valueRange = 0f..100f
            )

            SliderWithLabel(
                label = "Direct Level",
                value = fx.directLevel.toFloat(),
                onValueChange = { newDirectLevel ->
                    onFxChange(fx.copy(directLevel = newDirectLevel.toInt()))
                },
                valueRange = 0f..100f
            )

            SliderWithLabel(
                label = "Separation",
                value = fx.separation.toFloat(),
                onValueChange = { newSeparation ->
                    onFxChange(fx.copy(separation = newSeparation.toInt()))
                },
                valueRange = 0f..100f
            )
        }
    }
}