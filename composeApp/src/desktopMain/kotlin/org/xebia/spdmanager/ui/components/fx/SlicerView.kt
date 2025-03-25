package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.model.system.fx.subtypes.Slicer
import org.xebia.spdmanager.ui.components.common.ButtonRow
import org.xebia.spdmanager.ui.components.common.SliderWithLabel

@Composable
fun SlicerView(slicer: Slicer) {
    var pattern by remember { mutableStateOf(slicer.pattern.toFloat()) }
    var selectedRateSync by remember { mutableStateOf(slicer.rateSync) }
    var attack by remember { mutableStateOf(slicer.attack.toFloat()) }

    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Slicer", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        // Pattern Selection (Slider)
        SliderWithLabel(
            label = "Pattern",
            value = pattern,
            onValueChange = { pattern = it },
            valueRange = 0f..100f
        )

        // Rate Sync Selection (Button Row)
        ButtonRow(
            label = "Rate Sync",
            items = SyncSwitch.entries.toTypedArray(),
            selectedItem = selectedRateSync,
            onItemSelected = { selectedRateSync = it }
        )

        // Attack Selection (Slider)
        SliderWithLabel(
            label = "Attack",
            value = attack,
            onValueChange = { attack = it },
            valueRange = 0f..100f
        )
    }
}
