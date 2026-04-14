package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.ui.theme.*
import org.xebia.spdmanager.ui.theme.Typography
import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect
import org.xebia.spdmanager.model.system.fx.subtypes.Slicer
import org.xebia.spdmanager.ui.components.common.SliderWithLabel
import org.xebia.spdmanager.ui.components.common.ToggleSwitchWithLabel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SlicerView(
    slicer: Slicer,
    onFxChange: (FxEffect) -> Unit
) {
    Column(modifier = Modifier.padding(Spacing.m).fillMaxWidth()) {
        Text("Slicer", fontSize = Typography.titleSize)

        FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xl)) {
            ToggleSwitchWithLabel(
                label = "Rate Sync",
                selectedItem = slicer.rateSync,
                offItem = SyncSwitch.OFF,
                onItem = SyncSwitch.ON,
                onItemSelected = { newRateSync ->
                    onFxChange(slicer.copy(rateSync = newRateSync))
                }
            )

            SliderWithLabel(
                label = "Pattern",
                value = slicer.pattern.toFloat(),
                onValueChange = { newPattern ->
                    onFxChange(slicer.copy(pattern = newPattern.toInt()))
                },
                valueRange = 0f..100f
            )

            SliderWithLabel(
                label = "Attack",
                value = slicer.attack.toFloat(),
                onValueChange = { newAttack ->
                    onFxChange(slicer.copy(attack = newAttack.toInt()))
                },
                valueRange = 0f..100f
            )
        }
    }
}