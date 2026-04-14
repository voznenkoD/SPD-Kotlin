package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.ui.theme.*
import org.xebia.spdmanager.ui.theme.Typography
import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.model.system.fx.subtypes.Distortion
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect
import org.xebia.spdmanager.ui.components.common.DropdownSelector
import org.xebia.spdmanager.ui.components.common.SliderWithLabel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DistortionView(
    distortion: Distortion,
    onFxChange: (FxEffect) -> Unit
) {
    Column(modifier = Modifier.padding(Spacing.xxl).fillMaxWidth()) {
        Text("Distortion", fontSize = Typography.titleSize)

        DropdownSelector(
            label = "Distortion Type",
            selectedItem = distortion.type,
            items = DistortionType.entries,
            onItemSelected = { newType ->
                onFxChange(distortion.copy(type = newType))
            }
        )

        FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xl)) {
            SliderWithLabel(
                label = "Drive",
                value = distortion.drive.toFloat(),
                onValueChange = { newDrive ->
                    onFxChange(distortion.copy(drive = newDrive.toInt()))
                },
                valueRange = 0f..100f
            )

            SliderWithLabel(
                label = "Bottom",
                value = distortion.bottom,
                onValueChange = { newBottom ->
                    onFxChange(distortion.copy(bottom = newBottom))
                },
                valueRange = 0f..100f
            )

            SliderWithLabel(
                label = "Tone",
                value = distortion.tone,
                onValueChange = { newTone ->
                    onFxChange(distortion.copy(tone = newTone))
                },
                valueRange = 0f..100f
            )

            SliderWithLabel(
                label = "Effect Level",
                value = distortion.effectLevel.toFloat(),
                onValueChange = { newEffectLevel ->
                    onFxChange(distortion.copy(effectLevel = newEffectLevel.toInt()))
                },
                valueRange = 0f..100f
            )
        }
    }
}