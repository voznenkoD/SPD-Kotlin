package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect
import org.xebia.spdmanager.model.system.fx.subtypes.StepFLNGR
import org.xebia.spdmanager.ui.components.common.ButtonRow
import org.xebia.spdmanager.ui.components.common.DropdownSelector
import org.xebia.spdmanager.ui.components.common.SliderWithLabel

@Composable
fun StepFlangerView(
    fx: StepFLNGR,
    onFxChange: (FxEffect) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Step Flanger", fontSize = 18.sp)

        ButtonRow(
            label = "Rate Sync",
            selectedItem = fx.rateSync,
            items = SyncSwitch.entries.toTypedArray(),
            onItemSelected = { newRateSync ->
                onFxChange(fx.copy(rateSync = newRateSync))
            }
        )

        ButtonRow(
            label = "Step Sync",
            selectedItem = fx.stepSync,
            items = SyncSwitch.entries.toTypedArray(),
            onItemSelected = { newStepSync ->
                onFxChange(fx.copy(stepSync = newStepSync))
            }
        )

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

        SliderWithLabel(
            label = "Separation",
            value = fx.separation.toFloat(),
            onValueChange = { newSeparation ->
                onFxChange(fx.copy(separation = newSeparation.toInt()))
            },
            valueRange = 0f..100f
        )

        DropdownSelector(
            label = "Low Cut",
            selectedItem = fx.lowCut,
            items = LowCut.entries.toList(),
            onItemSelected = { newLowCut ->
                onFxChange(fx.copy(lowCut = newLowCut))
            }
        )

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
    }
}