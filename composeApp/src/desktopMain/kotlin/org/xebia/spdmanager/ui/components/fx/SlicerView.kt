package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Slicer", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SliderWithLabel(
                label = "Pattern",
                value = slicer.pattern.toFloat(),
                onValueChange = { newPattern ->
                    onFxChange(slicer.copy(pattern = newPattern.toInt()))
                },
                valueRange = 0f..100f
            )
        }

        ToggleSwitchWithLabel(
            label = "Rate Sync",
            selectedItem = slicer.rateSync,
            offItem = SyncSwitch.OFF,
            onItem = SyncSwitch.ON,
            onItemSelected = { newRateSync ->
                onFxChange(slicer.copy(rateSync = newRateSync))
            }
        )

        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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