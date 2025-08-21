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
fun AudioView(
    audioConfig: SystemAudioConfig,
    onUpdate: (SystemAudioConfig) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Column(modifier = Modifier.weight(1f).padding(8.dp)) {
            Text("Audio Settings")

            IntStepSliderWithLabel(
                label = "Audio In Volume",
                value = audioConfig.audioInVolume,
                range = 0..100,
                onValueChange = { volume ->
                    onUpdate(audioConfig.copy(audioInVolume = volume))
                }
            )

            IntStepSliderWithLabel(
                label = "USB In Volume",
                value = audioConfig.usbInVolume,
                range = 0..100,
                onValueChange = { volume ->
                    onUpdate(audioConfig.copy(usbInVolume = volume))
                }
            )

            IntStepSliderWithLabel(
                label = "Sub Out Volume",
                value = audioConfig.subOutVolume,
                range = 0..100,
                onValueChange = { volume ->
                    onUpdate(audioConfig.copy(subOutVolume = volume))
                }
            )

            Row(modifier = Modifier.fillMaxSize()) {
                DropdownSelector(
                    label = "System Gain",
                    selectedItem = audioConfig.systemGain,
                    onItemSelected = { gain ->
                        onUpdate(audioConfig.copy(systemGain = gain))
                    },
                    items = SystemGain.entries.toList()
                )

                DropdownSelector(
                    label = "Audio In Output",
                    selectedItem = audioConfig.audioInOutput,
                    onItemSelected = { output ->
                        onUpdate(audioConfig.copy(audioInOutput = output))
                    },
                    items = Output.entries.toList()
                )

                DropdownSelector(
                    label = "FX2 Output",
                    selectedItem = audioConfig.fx2Output,
                    onItemSelected = { output ->
                        onUpdate(audioConfig.copy(fx2Output = output))
                    },
                    items = FxOutput.entries.toList()
                )
            }
        }

        Column(modifier = Modifier.weight(1f).padding(8.dp)) {
            Text("System EQ")

            SliderWithLabel(
                label = "Low Gain (dB)",
                value = audioConfig.systemEq.lowGain,
                valueRange = -12f..12f,
                onValueChange = { gain ->
                    onUpdate(
                        audioConfig.copy(
                            systemEq = audioConfig.systemEq.copy(lowGain = gain)
                        )
                    )
                }
            )

            DropdownSelector(
                label = "Mid Frequency",
                selectedItem = audioConfig.systemEq.midFreq,
                onItemSelected = { freq ->
                    onUpdate(
                        audioConfig.copy(
                            systemEq = audioConfig.systemEq.copy(midFreq = freq)
                        )
                    )
                },
                items = EqFreq.entries.toList()
            )

            SliderWithLabel(
                label = "Mid Gain (dB)",
                value = audioConfig.systemEq.midGain,
                valueRange = -12f..12f,
                onValueChange = { gain ->
                    onUpdate(
                        audioConfig.copy(
                            systemEq = audioConfig.systemEq.copy(midGain = gain)
                        )
                    )
                }
            )

            SliderWithLabel(
                label = "High Gain (dB)",
                value = audioConfig.systemEq.highGain,
                valueRange = -12f..12f,
                onValueChange = { gain ->
                    onUpdate(
                        audioConfig.copy(
                            systemEq = audioConfig.systemEq.copy(highGain = gain)
                        )
                    )
                }
            )
        }
    }
}