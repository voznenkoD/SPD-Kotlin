package org.xebia.spdmanager.ui.components.system.masterfx

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.model.system.fx.mainTypes.FilterEffect
import org.xebia.spdmanager.model.system.fx.mainTypes.FilterPreset
import org.xebia.spdmanager.ui.components.common.ButtonRow
import org.xebia.spdmanager.ui.components.common.DropdownSelector
import org.xebia.spdmanager.ui.components.common.IntStepSliderWithLabel


@Composable
fun FilterEffectView(initialConfig: FilterEffect, onUpdate: (FilterEffect) -> Unit) {
    var preset by remember { mutableStateOf(initialConfig.preset) }
    var type by remember { mutableStateOf(initialConfig.type) }
    var slope by remember { mutableStateOf(initialConfig.slope) }
    var rateSync by remember { mutableStateOf(initialConfig.rateSync) }
    var modRate by remember { mutableStateOf(initialConfig.modRate) }
    var modDepth by remember { mutableStateOf(initialConfig.modDepth) }
    var lfoWave by remember { mutableStateOf(initialConfig.lfoWave) }

    LaunchedEffect(preset, type, slope, rateSync, modRate, modDepth, lfoWave) {
        onUpdate(FilterEffect(preset, type, slope, rateSync, modRate, modDepth, lfoWave))
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Filter Effect", style = MaterialTheme.typography.titleMedium)

        ButtonRow("Preset", preset, FilterPreset.entries.toTypedArray()) {
            preset = it
        }
        ButtonRow("Type", type, FilterType.entries.toTypedArray()) {
            type = it
        }
        ButtonRow("Slope", slope, FilterSlope.entries.toTypedArray()) {
            slope = it
        }

        Switch(
            checked = rateSync == SyncSwitch.ON,
            onCheckedChange = {
                rateSync = if (it) SyncSwitch.ON else SyncSwitch.OFF
                modRate = if (rateSync == SyncSwitch.ON) {
                    ModRate.EnumRate(ModRateEnum.fromIndex(0)) // Default EnumRate
                } else {
                    ModRate.IntRate(0) // Default IntRate
                }
            }
        )

        if (rateSync == SyncSwitch.ON) {
            ButtonRow("Mod Rate", (modRate as ModRate.EnumRate).modRateEnum, ModRateEnum.entries.toTypedArray()) {
                modRate = ModRate.EnumRate(it)
            }
        } else {
            IntStepSliderWithLabel("Mod Rate (ms)", (modRate as ModRate.IntRate).intRate, 0..100) {
                modRate = ModRate.IntRate(it)
            }
        }

        IntStepSliderWithLabel("Mod Depth", modDepth, 0..100) {
            modDepth = it
        }

        DropdownSelector(
            label = "LFO Wave",
            selectedItem = lfoWave,
            items = LfoWave.entries,
            onItemSelected = { lfoWave = it }
        )
    }
}
