package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.subtypes.FiltDrive
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect
import org.xebia.spdmanager.ui.components.common.SliderWithLabel

@Composable
fun FiltDriveView(
    fx: FiltDrive,
    onFxChange: (FxEffect) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Filt Drive Settings", fontSize = 18.sp)

        SliderWithLabel(
            label = "Resonance",
            value = fx.resonance.toFloat(),
            onValueChange = { newResonance ->
                onFxChange(fx.copy(resonance = newResonance.toInt()))
            },
            valueRange = 0f..100f
        )

        SliderWithLabel(
            label = "Level",
            value = fx.level.toFloat(),
            onValueChange = { newLevel ->
                onFxChange(fx.copy(level = newLevel.toInt()))
            },
            valueRange = 0f..100f
        )
    }
}