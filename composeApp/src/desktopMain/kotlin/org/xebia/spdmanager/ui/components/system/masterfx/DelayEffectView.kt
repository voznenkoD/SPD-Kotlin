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
import org.xebia.spdmanager.model.system.fx.mainTypes.DelayEffect
import org.xebia.spdmanager.model.system.fx.mainTypes.DelayPreset
import org.xebia.spdmanager.ui.components.common.ButtonRow
import org.xebia.spdmanager.ui.components.common.IntStepSliderWithLabel

@Composable
fun DelayEffectView(initialConfig: DelayEffect, onUpdate: (DelayEffect) -> Unit) {
    var preset by remember { mutableStateOf(initialConfig.preset) }
    var type by remember { mutableStateOf(initialConfig.type) }
    var syncSW by remember { mutableStateOf(initialConfig.syncSW) }
    var delayTime by remember { mutableStateOf(initialConfig.delayTime) }
    var tapTime by remember { mutableStateOf(initialConfig.tapTime) }
    var lowCut by remember { mutableStateOf(initialConfig.lowCut) }
    var highCut by remember { mutableStateOf(initialConfig.highCut) }
    var directLevel by remember { mutableStateOf(initialConfig.directLevel) }


    LaunchedEffect(preset, type, syncSW, delayTime, tapTime, lowCut, highCut, directLevel) {
        onUpdate(DelayEffect(preset, type, syncSW, delayTime, tapTime, lowCut, highCut, directLevel))
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Delay Effect", style = MaterialTheme.typography.titleMedium)

        ButtonRow("Preset", preset, DelayPreset.entries.toTypedArray()) {
            preset = it
        }
        ButtonRow("Type", type, DelayType.entries.toTypedArray()) {
            type = it
        }

        Switch(
            checked = syncSW == SyncSwitch.ON,
            onCheckedChange = {
                syncSW = if (it) SyncSwitch.ON else SyncSwitch.OFF
                delayTime = if (syncSW == SyncSwitch.ON) {
                    DelayTime.EnumTime(DelayTimeEnum.fromIndex(0)) // Default EnumTime
                } else {
                    DelayTime.IntTime(0) // Default IntTime
                }
            }
        )

        if (syncSW == SyncSwitch.ON) {
            ButtonRow("Delay Time", (delayTime as DelayTime.EnumTime).delayTimeEnum, DelayTimeEnum.entries.toTypedArray()) {
                delayTime = DelayTime.EnumTime(it)
            }
        } else {
            IntStepSliderWithLabel("Delay Time (ms)", (delayTime as DelayTime.IntTime).intTime, 0..1300) {
                delayTime = DelayTime.IntTime(it)
            }
        }

        IntStepSliderWithLabel("Tap Time", tapTime, 0..100) {
            tapTime = it
        }

        ButtonRow("Low Cut", lowCut, LowCut.entries.toTypedArray()) {
            lowCut = it
        }

        ButtonRow("High Cut", highCut, HighCut.entries.toTypedArray()) {
            highCut = it
        }

        IntStepSliderWithLabel("Direct Level", directLevel, 0..100) {
            directLevel = it
        }
    }
}
