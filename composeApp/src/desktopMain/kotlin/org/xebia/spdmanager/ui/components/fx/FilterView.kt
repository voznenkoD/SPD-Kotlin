package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.model.system.fx.subtypes.Filter
import org.xebia.spdmanager.ui.components.ButtonRow
import org.xebia.spdmanager.ui.components.SliderWithLabel

@Composable
fun FilterView(fx: Filter) {
    var selectedFilterType by remember { mutableStateOf(fx.type) }
    var resonance by remember { mutableStateOf(fx.resonance) }
    var selectedSlope by remember { mutableStateOf(fx.slope) }
    var selectedRateSyncSW by remember { mutableStateOf(fx.rateSyncSW) }
    var selectedModRate by remember { mutableStateOf(fx.modRate) }
    var selectedLfoWave by remember { mutableStateOf(fx.lfoWave) }

    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Filter Settings", fontSize = 18.sp)

        // Filter Type (Button Row)
        ButtonRow(
            label = "Filter Type",
            selectedItem = selectedFilterType,
            items = FilterType.entries.toTypedArray(),
            onItemSelected = { selectedFilterType = it }
        )

        // Resonance (Slider)
        SliderWithLabel(
            label = "Resonance",
            value = resonance.toFloat(),
            onValueChange = { resonance = it.toInt() },
            valueRange = 0f..100f
        )

        // Filter Slope (Button Row)
        ButtonRow(
            label = "Slope",
            selectedItem = selectedSlope,
            items = FilterSlope.entries.toTypedArray(),
            onItemSelected = { selectedSlope = it }
        )

        // Rate Sync Switch (Button Row)
        ButtonRow(
            label = "Rate Sync",
            selectedItem = selectedRateSyncSW,
            items = SyncSwitch.entries.toTypedArray(),
            onItemSelected = { selectedRateSyncSW = it }
        )

        // Modulation Rate (Dynamic Content Based on SyncSwitch)
        if (selectedRateSyncSW == SyncSwitch.ON) {
            // Display Enum Rate Buttons when SyncSwitch is ON
            val modRateEnum = (selectedModRate as? ModRate.EnumRate)!!.modRateEnum
            ButtonRow(
                label = "Modulation Rate",
                selectedItem = modRateEnum,
                items = ModRateEnum.entries.toTypedArray(),
                onItemSelected = { selectedModRate = ModRate.EnumRate(it) }
            )
        } else {
            // Display Slider when SyncSwitch is OFF
            val modRateInt = (selectedModRate as? ModRate.IntRate)?.intRate?.toFloat() ?: 0f
            SliderWithLabel(
                label = "Modulation Rate",
                value = modRateInt,
                onValueChange = { selectedModRate = ModRate.IntRate(it.toInt()) },
                valueRange = 0f..100f
            )
        }

        // LFO Wave (Button Row)
        ButtonRow(
            label = "LFO Wave",
            selectedItem = selectedLfoWave,
            items = LfoWave.entries.toTypedArray(),
            onItemSelected = { selectedLfoWave = it }
        )
    }
}

