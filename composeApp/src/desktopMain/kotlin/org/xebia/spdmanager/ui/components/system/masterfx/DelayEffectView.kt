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
import org.xebia.spdmanager.ui.components.common.ButtonRowCompact
import org.xebia.spdmanager.ui.components.common.IntStepSliderWithLabel
import org.xebia.spdmanager.ui.components.common.ToggleSwitchWithLabel

@Composable
fun DelayEffectView(
    delayEffect: DelayEffect,
    onDelayChange: (DelayEffect) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Delay Effect", style = MaterialTheme.typography.titleMedium)

        ButtonRowCompact(
            label = "Preset",
            selectedItem = delayEffect.preset,
            items = DelayPreset.entries.toTypedArray(),
            onItemSelected = { newPreset ->
                onDelayChange(delayEffect.copy(preset = newPreset))
            }
        )

        ToggleSwitchWithLabel(
            label = "Type",
            selectedItem = delayEffect.type,
            offItem = DelayType.NORMAL,
            onItem = DelayType.PAN,
            onItemSelected = { newType ->
                onDelayChange(delayEffect.copy(type = newType))
            },
            offLabel = "Normal",
            onLabel = "Pan"
        )

        Switch(
            checked = delayEffect.syncSW == SyncSwitch.ON,
            onCheckedChange = { isOn ->
                val newSyncSW = if (isOn) SyncSwitch.ON else SyncSwitch.OFF
                val newDelayTime = if (isOn) {
                    DelayTime.EnumTime(DelayTimeEnum.fromIndex(0))
                } else {
                    DelayTime.IntTime(
                        (delayEffect.delayTime as? DelayTime.EnumTime)?.delayTimeEnum?.ordinal ?: 0
                    )
                }
                onDelayChange(
                    delayEffect.copy(
                        syncSW = newSyncSW,
                        delayTime = newDelayTime
                    )
                )
            }
        )

        if (delayEffect.syncSW == SyncSwitch.ON) {
            ButtonRowCompact(
                label = "Delay Time",
                selectedItem = (delayEffect.delayTime as? DelayTime.EnumTime)?.delayTimeEnum ?: DelayTimeEnum.fromIndex(0),
                items = DelayTimeEnum.entries.toTypedArray(),
                onItemSelected = { newDelayTimeEnum ->
                    onDelayChange(delayEffect.copy(delayTime = DelayTime.EnumTime(newDelayTimeEnum)))
                }
            )
        } else {
            IntStepSliderWithLabel(
                label = "Delay Time (ms)",
                value = (delayEffect.delayTime as? DelayTime.IntTime)?.intTime ?: 0,
                range = 0..1300,
                onValueChange = { newIntTime ->
                    onDelayChange(delayEffect.copy(delayTime = DelayTime.IntTime(newIntTime)))
                }
            )
        }

        IntStepSliderWithLabel(
            label = "Tap Time",
            value = delayEffect.tapTime,
            range = 0..100,
            onValueChange = { newTapTime ->
                onDelayChange(delayEffect.copy(tapTime = newTapTime))
            }
        )

        ButtonRowCompact(
            label = "Low Cut (Hz)",
            selectedItem = delayEffect.lowCut,
            items = LowCut.entries.toTypedArray(),
            onItemSelected = { newLowCut ->
                onDelayChange(delayEffect.copy(lowCut = newLowCut))
            }
        )

        ButtonRowCompact(
            label = "High Cut (Hz)",
            selectedItem = delayEffect.highCut,
            items = HighCut.entries.toTypedArray(),
            onItemSelected = { newHighCut ->
                onDelayChange(delayEffect.copy(highCut = newHighCut))
            }
        )

        IntStepSliderWithLabel(
            label = "Direct Level",
            value = delayEffect.directLevel,
            range = 0..100,
            onValueChange = { newDirectLevel ->
                onDelayChange(delayEffect.copy(directLevel = newDirectLevel))
            }
        )
    }
}