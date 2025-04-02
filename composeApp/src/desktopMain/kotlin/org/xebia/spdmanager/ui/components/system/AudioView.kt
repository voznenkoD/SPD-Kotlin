package org.xebia.spdmanager.ui.components.system

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.model.system.FxOutput
import org.xebia.spdmanager.model.system.Output
import org.xebia.spdmanager.model.system.SystemAudioConfig
import org.xebia.spdmanager.model.system.SystemGain
import org.xebia.spdmanager.model.system.fx.common.EqFreq
import org.xebia.spdmanager.ui.components.common.DropdownSelector
import org.xebia.spdmanager.ui.components.common.IntStepSliderWithLabel
import org.xebia.spdmanager.ui.components.common.SliderWithLabel

@Composable
fun AudioView(systemAudioConfig: SystemAudioConfig, onAudioConfigChanged: (SystemAudioConfig) -> Unit) {
    var audioInVolume by remember { mutableStateOf(systemAudioConfig.audioInVolume) }
    var usbInVolume by remember { mutableStateOf(systemAudioConfig.usbInVolume) }
    var subOutVolume by remember { mutableStateOf(systemAudioConfig.subOutVolume) }
    var systemGain by remember { mutableStateOf(systemAudioConfig.systemGain) }
    var audioInOutput by remember { mutableStateOf(systemAudioConfig.audioInOutput) }
    var fx2Output by remember { mutableStateOf(systemAudioConfig.fx2Output) }

    var lowGain by remember { mutableStateOf(systemAudioConfig.systemEq.lowGain) }
    var midFreq by remember { mutableStateOf(systemAudioConfig.systemEq.midFreq) }
    var midGain by remember { mutableStateOf(systemAudioConfig.systemEq.midGain) }
    var highGain by remember { mutableStateOf(systemAudioConfig.systemEq.highGain) }

    Row(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Column(modifier = Modifier.weight(1f).padding(8.dp)) {
            Text("Audio Settings")

            IntStepSliderWithLabel(
                label = "Audio In Volume",
                value = audioInVolume,
                range = 0..100,
                onValueChange = {
                    audioInVolume = it
                    onAudioConfigChanged(systemAudioConfig.copy(audioInVolume = audioInVolume))
                }
            )

            IntStepSliderWithLabel(
                label = "USB In Volume",
                value = usbInVolume,
                range = 0..100,
                onValueChange = {
                    usbInVolume = it
                    onAudioConfigChanged(systemAudioConfig.copy(usbInVolume = usbInVolume))
                }
            )

            IntStepSliderWithLabel(
                label = "Sub Out Volume",
                value = subOutVolume,
                range = 0..100,
                onValueChange = {
                    subOutVolume = it
                    onAudioConfigChanged(systemAudioConfig.copy(subOutVolume = subOutVolume))
                }
            )


            Row(modifier = Modifier.fillMaxSize()) {
                DropdownSelector(
                    label = "System Gain",
                    selectedItem = systemGain,
                    onItemSelected = {
                        systemGain = it
                        onAudioConfigChanged(systemAudioConfig.copy(systemGain = systemGain))
                    },
                    items = SystemGain.entries.toList()
                )
                DropdownSelector(
                    label = "Audio In Output",
                    selectedItem = audioInOutput,
                    onItemSelected = {
                        audioInOutput = it
                        onAudioConfigChanged(systemAudioConfig.copy(audioInOutput = audioInOutput))
                    },
                    items = Output.entries.toList()
                )

                DropdownSelector(
                    label = "FX2 Output",
                    selectedItem = fx2Output,
                    onItemSelected = {
                        fx2Output = it
                        onAudioConfigChanged(systemAudioConfig.copy(fx2Output = fx2Output))
                    },
                    items = FxOutput.entries.toList()
                )
            }
        }

        Column(modifier = Modifier.weight(1f).padding(8.dp)) {
            Text("System EQ")

            SliderWithLabel(
                label = "Low Gain (dB)",
                value = lowGain,
                valueRange = -12f..12f,
                onValueChange = {
                    lowGain = it
                    onAudioConfigChanged(systemAudioConfig.copy(systemEq = systemAudioConfig.systemEq.copy(lowGain = lowGain)))
                }
            )

            DropdownSelector(
                label = "Mid Frequency",
                selectedItem = midFreq,
                onItemSelected = {
                    midFreq = it
                    onAudioConfigChanged(systemAudioConfig.copy(systemEq = systemAudioConfig.systemEq.copy(midFreq = midFreq)))
                },
                items = EqFreq.entries.toList()
            )

            SliderWithLabel(
                label = "Mid Gain (dB)",
                value = midGain,
                valueRange = -12f..12f,
                onValueChange = {
                    midGain = it
                    onAudioConfigChanged(systemAudioConfig.copy(systemEq = systemAudioConfig.systemEq.copy(midGain = midGain)))
                }
            )

            SliderWithLabel(
                label = "High Gain (dB)",
                value = highGain,
                valueRange = -12f..12f,
                onValueChange = {
                    highGain = it
                    onAudioConfigChanged(systemAudioConfig.copy(systemEq = systemAudioConfig.systemEq.copy(highGain = highGain)))
                }
            )
        }
    }
}
