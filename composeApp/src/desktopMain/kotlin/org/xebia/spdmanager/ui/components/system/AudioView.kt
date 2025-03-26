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
import org.xebia.spdmanager.ui.components.setup.DropdownWithLabel
import org.xebia.spdmanager.ui.components.setup.SliderWithLabel

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

            SliderWithLabel(
                label = "Audio In Volume",
                value = audioInVolume,
                range = 0..100,
                onValueChange = {
                    audioInVolume = it
                    onAudioConfigChanged(systemAudioConfig.copy(audioInVolume = audioInVolume))
                }
            )

            SliderWithLabel(
                label = "USB In Volume",
                value = usbInVolume,
                range = 0..100,
                onValueChange = {
                    usbInVolume = it
                    onAudioConfigChanged(systemAudioConfig.copy(usbInVolume = usbInVolume))
                }
            )

            SliderWithLabel(
                label = "Sub Out Volume",
                value = subOutVolume,
                range = 0..100,
                onValueChange = {
                    subOutVolume = it
                    onAudioConfigChanged(systemAudioConfig.copy(subOutVolume = subOutVolume))
                }
            )

            DropdownWithLabel(
                label = "System Gain",
                selected = systemGain,
                onSelected = {
                    systemGain = it
                    onAudioConfigChanged(systemAudioConfig.copy(systemGain = systemGain))
                },
                options = SystemGain.entries.toList()
            )

            Row(modifier = Modifier.fillMaxSize()) {
                DropdownWithLabel(
                    label = "Audio In Output",
                    selected = audioInOutput,
                    onSelected = {
                        audioInOutput = it
                        onAudioConfigChanged(systemAudioConfig.copy(audioInOutput = audioInOutput))
                    },
                    options = Output.entries.toList()
                )

                DropdownWithLabel(
                    label = "FX2 Output",
                    selected = fx2Output,
                    onSelected = {
                        fx2Output = it
                        onAudioConfigChanged(systemAudioConfig.copy(fx2Output = fx2Output))
                    },
                    options = FxOutput.entries.toList()
                )
            }
        }

        Column(modifier = Modifier.weight(1f).padding(8.dp)) {
            Text("System EQ")

            FloatSliderWithLabel(
                label = "Low Gain (dB)",
                value = lowGain,
                range = -12f..12f,
                onValueChange = {
                    lowGain = it
                    onAudioConfigChanged(systemAudioConfig.copy(systemEq = systemAudioConfig.systemEq.copy(lowGain = lowGain)))
                }
            )

            DropdownWithLabel(
                label = "Mid Frequency",
                selected = midFreq,
                onSelected = {
                    midFreq = it
                    onAudioConfigChanged(systemAudioConfig.copy(systemEq = systemAudioConfig.systemEq.copy(midFreq = midFreq)))
                },
                options = EqFreq.entries.toList()
            )

            FloatSliderWithLabel(
                label = "Mid Gain (dB)",
                value = midGain,
                range = -12f..12f,
                onValueChange = {
                    midGain = it
                    onAudioConfigChanged(systemAudioConfig.copy(systemEq = systemAudioConfig.systemEq.copy(midGain = midGain)))
                }
            )

            FloatSliderWithLabel(
                label = "High Gain (dB)",
                value = highGain,
                range = -12f..12f,
                onValueChange = {
                    highGain = it
                    onAudioConfigChanged(systemAudioConfig.copy(systemEq = systemAudioConfig.systemEq.copy(highGain = highGain)))
                }
            )
        }
    }
}


@Composable
fun FloatSliderWithLabel(label: String, value: Float, range: ClosedFloatingPointRange<Float>, onValueChange: (Float) -> Unit) {
    Column {
        Text("$label: ${value.toInt()} dB")
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range
        )
    }
}