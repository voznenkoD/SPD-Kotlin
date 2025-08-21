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
import org.xebia.spdmanager.ui.components.common.ButtonRow
import org.xebia.spdmanager.ui.components.common.SliderWithLabel

@Composable
fun SlicerView(
    slicer: Slicer,
    onFxChange: (FxEffect) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Slicer", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        SliderWithLabel(
            label = "Pattern",
            value = slicer.pattern.toFloat(),
            onValueChange = { newPattern ->
                onFxChange(slicer.copy(pattern = newPattern.toInt()))
            },
            valueRange = 0f..100f
        )

        ButtonRow(
            label = "Rate Sync",
            items = SyncSwitch.entries.toTypedArray(),
            selectedItem = slicer.rateSync,
            onItemSelected = { newRateSync ->
                onFxChange(slicer.copy(rateSync = newRateSync))
            }
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