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
import org.xebia.spdmanager.model.system.fx.common.SyncSwitch
import org.xebia.spdmanager.model.system.fx.mainTypes.*
import org.xebia.spdmanager.ui.components.common.ButtonRow
import org.xebia.spdmanager.ui.components.setup.SliderWithLabel

@Composable
fun SLoopEffectView(initialConfig: SLoopEffect, onUpdate: (SLoopEffect) -> Unit) {
    var preset by remember { mutableStateOf(initialConfig.preset) }
    var mode by remember { mutableStateOf(initialConfig.mode) }
    var rateSync by remember { mutableStateOf(initialConfig.rateSync) }
    var rate by remember { mutableStateOf(initialConfig.rate) }
    var timing by remember { mutableStateOf(initialConfig.timing) }

    LaunchedEffect(preset, mode, rateSync, rate, timing) {
        onUpdate(SLoopEffect(preset, mode, rateSync, rate, timing))
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("S.Loop Effect", style = MaterialTheme.typography.titleMedium)

        ButtonRow("Preset", preset, SLoopPreset.entries.toTypedArray()) {
            preset = it
        }
        ButtonRow("Mode", mode, SLoopMode.entries.toTypedArray()) {
            mode = it
        }

        Switch(
            checked = rateSync == SyncSwitch.ON,
            onCheckedChange = {
                rateSync = if (it) SyncSwitch.ON else SyncSwitch.OFF
                rate = if (rateSync == SyncSwitch.ON) {
                    SLoopRate.EnumRate(SLoopRateEnum.fromIndex(0)) // Default EnumRate
                } else {
                    SLoopRate.IntRate(0) // Default IntRate
                }
            }
        )

        if (rateSync == SyncSwitch.ON) {
            ButtonRow("Rate", (rate as SLoopRate.EnumRate).rateEnum, SLoopRateEnum.entries.toTypedArray()) {
                rate = SLoopRate.EnumRate(it)
            }
        } else {
            SliderWithLabel("Rate ", (rate as SLoopRate.IntRate).intRate, 0..127) {
                rate = SLoopRate.IntRate(it)
            }
        }

        ButtonRow("Timing", timing, SLoopTiming.entries.toTypedArray()) {
            timing = it
        }
    }
}
