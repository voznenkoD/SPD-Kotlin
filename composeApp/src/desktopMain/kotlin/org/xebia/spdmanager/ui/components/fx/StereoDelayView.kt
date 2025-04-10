package org.xebia.spdmanager.ui.components.fx
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.common.DelayType
import org.xebia.spdmanager.model.system.fx.common.HighCut
import org.xebia.spdmanager.model.system.fx.common.LowCut
import org.xebia.spdmanager.model.system.fx.common.SyncSwitch
import org.xebia.spdmanager.model.system.fx.subtypes.StereoDelay
import org.xebia.spdmanager.ui.components.common.ButtonRow
import org.xebia.spdmanager.ui.components.common.ButtonRowCompact
import org.xebia.spdmanager.ui.components.common.SliderWithLabel

@Composable
fun StereoDelayView(stereoDelay: StereoDelay) {
    var selectedType by remember { mutableStateOf(stereoDelay.type) }
    var selectedSyncSW by remember { mutableStateOf(stereoDelay.syncSW) }
    var delayTime by remember { mutableStateOf(stereoDelay.delayTime.toFloat()) }
    var tapTime by remember { mutableStateOf(stereoDelay.tapTime.toFloat()) }
    var selectedLowCut by remember { mutableStateOf(stereoDelay.lowCut) }
    var selectedHighCut by remember { mutableStateOf(stereoDelay.highCut) }
    var directLevel by remember { mutableStateOf(stereoDelay.directLevel.toFloat()) }

    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Stereo Delay", fontSize = 18.sp)

        ButtonRow(
            label = "Delay Type",
            selectedItem = selectedType,
            items = DelayType.entries.toTypedArray(),
            onItemSelected = { selectedType = it }
        )

        ButtonRow(
            label = "Sync Switch",
            selectedItem = selectedSyncSW,
            items = SyncSwitch.entries.toTypedArray(),
            onItemSelected = { selectedSyncSW = it }
        )

        SliderWithLabel(
            label = "Delay Time",
            value = delayTime,
            onValueChange = { delayTime = it },
            valueRange = 0f..100f
        )

        SliderWithLabel(
            label = "Tap Time",
            value = tapTime,
            onValueChange = { tapTime = it },
            valueRange = 0f..100f
        )

        ButtonRowCompact(
            label = "Low Cut",
            selectedItem = selectedLowCut,
            items = LowCut.entries.toTypedArray(),
            onItemSelected = { selectedLowCut = it }
        )

        ButtonRowCompact(
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
