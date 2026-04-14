package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.ui.theme.*
import org.xebia.spdmanager.ui.theme.Typography
import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect
import org.xebia.spdmanager.model.system.fx.subtypes.Phaser
import org.xebia.spdmanager.ui.components.common.DropdownSelector
import org.xebia.spdmanager.ui.components.common.SliderWithLabel
import org.xebia.spdmanager.ui.components.common.ToggleSwitchWithLabel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PhaserView(
    fx: Phaser,
    onFxChange: (FxEffect) -> Unit
) {
    Column(modifier = Modifier.padding(Spacing.m).fillMaxWidth()) {
        Text("Phaser", fontSize = Typography.titleSize)

        DropdownSelector(
            label = "Phaser Type",
            selectedItem = fx.type,
            items = PhaserType.entries.toList(),
            onItemSelected = { newType ->
                onFxChange(fx.copy(type = newType))
            }
        )

        ToggleSwitchWithLabel(
            label = "Rate Sync Switch",
            selectedItem = fx.rateSyncSW,
            offItem = SyncSwitch.OFF,
            onItem = SyncSwitch.ON,
            onItemSelected = { newRateSyncSW ->
                onFxChange(fx.copy(rateSyncSW = newRateSyncSW))
            }
        )

        FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xl)) {
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
}