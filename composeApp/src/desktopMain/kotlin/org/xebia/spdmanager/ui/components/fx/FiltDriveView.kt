package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.ui.theme.*
import org.xebia.spdmanager.ui.theme.Typography
import org.xebia.spdmanager.model.system.fx.subtypes.FiltDrive
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect
import org.xebia.spdmanager.ui.components.common.SliderWithLabel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FiltDriveView(
    fx: FiltDrive,
    onFxChange: (FxEffect) -> Unit
) {
    Column(modifier = Modifier.padding(Spacing.xxl).fillMaxWidth()) {
        Text("Filt Drive Settings", fontSize = Typography.titleSize)

        FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xl)) {
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
}