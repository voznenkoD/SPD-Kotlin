package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.model.system.fx.subtypes.StepPHASR
import org.xebia.spdmanager.ui.components.common.ButtonRow
import org.xebia.spdmanager.ui.components.common.DropdownSelector
import org.xebia.spdmanager.ui.components.common.SliderWithLabel

@Composable
fun StepPhaserView(fx: StepPHASR) {
    var selectedType by remember { mutableStateOf(fx.type) }
    var selectedRateSyncSW by remember { mutableStateOf(fx.rateSyncSW) }
    var selectedStepSyncSW by remember { mutableStateOf(fx.stepSyncSW) }
    var depth by remember { mutableStateOf(fx.depth.toFloat()) }
    var manual by remember { mutableStateOf(fx.manual.toFloat()) }
    var resonance by remember { mutableStateOf(fx.resonance.toFloat()) }
    var separation by remember { mutableStateOf(fx.separation.toFloat()) }
    var effectLevel by remember { mutableStateOf(fx.effectLevel.toFloat()) }
    var directLevel by remember { mutableStateOf(fx.directLevel.toFloat()) }

    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Step Phaser", fontSize = 18.sp)

        // Phaser Type (Dropdown or Button Row)
        DropdownSelector(
            label = "Phaser Type",
            selectedItem = selectedType,
            items = PhaserType.entries.toList(),
            onItemSelected = { selectedType = it }
        )

        // Rate Sync Switch (Button Row)
        ButtonRow(
            label = "Rate Sync Switch",
            selectedItem = selectedRateSyncSW,
            items = SyncSwitch.entries.toTypedArray(),
            onItemSelected = { selectedRateSyncSW = it }
        )

        // Step Sync Switch (Button Row)
        ButtonRow(
            label = "Step Sync Switch",
            selectedItem = selectedStepSyncSW,
            items = SyncSwitch.entries.toTypedArray(),
            onItemSelected = { selectedStepSyncSW = it }
        )

        // Depth (0..100)
        SliderWithLabel(
            label = "Depth",
            value = depth,
            onValueChange = { depth = it },
            valueRange = 0f..100f
        )

        // Manual (0..100)
        SliderWithLabel(
            label = "Manual",
            value = manual,
            onValueChange = { manual = it },
            valueRange = 0f..100f
        )

        // Resonance (0..100)
        SliderWithLabel(
            label = "Resonance",
            value = resonance,
            onValueChange = { resonance = it },
            valueRange = 0f..100f
        )

        // Separation (0..100)
        SliderWithLabel(
            label = "Separation",
            value = separation,
            onValueChange = { separation = it },
            valueRange = 0f..100f
        )

        // Effect Level (0..100)
        SliderWithLabel(
            label = "Effect Level",
            value = effectLevel,
            onValueChange = { effectLevel = it },
            valueRange = 0f..100f
        )

        // Direct Level (0..100)
        SliderWithLabel(
            label = "Direct Level",
            value = directLevel,
            onValueChange = { directLevel = it },
            valueRange = 0f..100f
        )
    }
}
