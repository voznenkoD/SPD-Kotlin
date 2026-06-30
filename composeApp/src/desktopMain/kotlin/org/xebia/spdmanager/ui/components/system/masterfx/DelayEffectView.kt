package org.xebia.spdmanager.ui.components.system.masterfx

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.ui.theme.*
import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.model.system.fx.applyPreset
import org.xebia.spdmanager.model.system.fx.mainTypes.DelayEffect
import org.xebia.spdmanager.model.system.fx.mainTypes.DelayPreset
import org.xebia.spdmanager.model.system.fx.reconcilePreset
import org.xebia.spdmanager.ui.components.common.ButtonRowCompact
import org.xebia.spdmanager.ui.components.common.IntStepSliderWithLabel
import org.xebia.spdmanager.ui.components.common.ToggleSwitchWithLabel

@Composable
fun DelayEffectView(
    delayEffect: DelayEffect,
    onDelayChange: (DelayEffect) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(Spacing.xxl)) {
        Text("Delay Effect", style = Typography.title, color = ColorTextPrimary)

        ButtonRowCompact(
            label = "Preset",
            selectedItem = delayEffect.preset,
            items = DelayPreset.entries.toTypedArray(),
            onItemSelected = { newPreset ->
                onDelayChange(delayEffect.applyPreset(newPreset))
            }
        )

        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xl)) {
            ToggleSwitchWithLabel(
                label = "Type",
                selectedItem = delayEffect.type,
                offItem = DelayType.NORMAL,
                onItem = DelayType.PAN,
                onItemSelected = { newType ->
                    onDelayChange(delayEffect.copy(type = newType).reconcilePreset())
                },
                offLabel = "Normal",
                onLabel = "Pan"
            )

            IntStepSliderWithLabel(
                label = "Tap Time",
                value = delayEffect.tapTime,
                range = 0..100,
                onValueChange = { newTapTime ->
                    onDelayChange(delayEffect.copy(tapTime = newTapTime))
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
                    ).reconcilePreset()
                )
            },
            colors = SwitchDefaults.colors(
                checkedTrackColor = ColorAccentOrange,
                uncheckedTrackColor = ColorDivider,
                checkedThumbColor = ColorBackground
            )
        )

        if (delayEffect.syncSW == SyncSwitch.ON) {
            ButtonRowCompact(
                label = "Delay Time",
                selectedItem = (delayEffect.delayTime as? DelayTime.EnumTime)?.delayTimeEnum ?: DelayTimeEnum.fromIndex(0),
                items = DelayTimeEnum.entries.toTypedArray(),
                onItemSelected = { newDelayTimeEnum ->
                    onDelayChange(delayEffect.copy(delayTime = DelayTime.EnumTime(newDelayTimeEnum)).reconcilePreset())
                }
            )
        } else {
            IntStepSliderWithLabel(
                label = "Delay Time (ms)",
                value = (delayEffect.delayTime as? DelayTime.IntTime)?.intTime ?: 0,
                range = 0..1300,
                onValueChange = { newIntTime ->
                    // ms delay time is a continuous param (only the synced note form is structural),
                    // so it must never relabel the preset — no reconcile here (mirrors Filter's ms slider).
                    onDelayChange(delayEffect.copy(delayTime = DelayTime.IntTime(newIntTime)))
                }
            )
        }

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
    }
}