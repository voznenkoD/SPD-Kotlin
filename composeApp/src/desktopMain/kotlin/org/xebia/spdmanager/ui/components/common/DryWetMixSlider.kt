package org.xebia.spdmanager.ui.components.common

import androidx.compose.runtime.Composable
import org.xebia.spdmanager.model.system.fx.common.DryWetMix

@Composable
fun DryWetMixSlider(
    label: String,
    value: DryWetMix,
    onValueChange: (DryWetMix) -> Unit
) {
    val rawValue = (value.dry + value.wet * 2).coerceIn(0, 200)
    val knobValue = (rawValue - 100).toFloat()

    KnobControl(
        label = label,
        value = knobValue,
        onValueChange = { newKnobValue ->
            onValueChange(DryWetMix.fromInt((newKnobValue + 100).toInt()))
        },
        valueRange = -100f..100f,
        mode = KnobMode.Bipolar,
        valueDisplay = value.toString()
    )
}
