package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.model.system.fx.subtypes.SyncDelay
import org.xebia.spdmanager.ui.components.ButtonRow
import org.xebia.spdmanager.ui.components.DropdownSelector
import org.xebia.spdmanager.ui.components.SliderWithLabel

@Composable
fun SyncDelayView(syncDelay: SyncDelay) {
    var selectedType by remember { mutableStateOf(syncDelay.type) }
    var selectedDelayTime by remember { mutableStateOf(syncDelay.delayTime) }
    var tapTime by remember { mutableStateOf(syncDelay.tapTime.toFloat()) }
    var selectedLowCut by remember { mutableStateOf(syncDelay.lowCut) }
    var selectedHighCut by remember { mutableStateOf(syncDelay.highCut) }
    var directLevel by remember { mutableStateOf(syncDelay.directLevel.toFloat()) }

    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Sync Delay", fontSize = 18.sp)

        ButtonRow(
            label = "Delay Type",
            selectedItem = selectedType,
            items = DelayType.entries.toTypedArray(),
            onItemSelected = { selectedType = it }
        )

        DropdownSelector(
            label = "Delay Time",
            selectedItem = (selectedDelayTime as? DelayTime.EnumTime) ?: DelayTimeEnum.QUARTER,
            items = DelayTimeEnum.entries.toList(),
            onItemSelected = { selectedDelayTime = DelayTime.EnumTime(it as DelayTimeEnum) }
        )

        SliderWithLabel(
            label = "Tap Time",
            value = tapTime,
            onValueChange = { tapTime = it },
            valueRange = 0f..100f
        )

        // Button row for Low Cut
        ButtonRow(
            label = "Low Cut",
            selectedItem = selectedLowCut,
            items = LowCut.entries.toTypedArray(),
            onItemSelected = { selectedLowCut = it }
        )

        ButtonRow(
            label = "High Cut",
            selectedItem = selectedHighCut,
            items = HighCut.entries.toTypedArray(),
            onItemSelected = { selectedHighCut = it }
        )

        SliderWithLabel(
            label = "Direct Level",
            value = directLevel,
            onValueChange = { directLevel = it },
            valueRange = 0f..100f
        )
    }
}
