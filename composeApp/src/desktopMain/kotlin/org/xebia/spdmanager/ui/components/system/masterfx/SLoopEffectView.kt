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
import org.xebia.spdmanager.model.system.fx.applyPreset
import org.xebia.spdmanager.model.system.fx.common.SyncSwitch
import org.xebia.spdmanager.model.system.fx.mainTypes.*
import org.xebia.spdmanager.model.system.fx.reconcilePreset
import org.xebia.spdmanager.ui.components.common.ButtonRowCompact
import org.xebia.spdmanager.ui.components.common.IntStepSliderWithLabel
import org.xebia.spdmanager.ui.components.common.ToggleSwitchWithLabel

@Composable
fun SLoopEffectView(
    sLoopEffect: SLoopEffect,
    onSLoopChange: (SLoopEffect) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(Spacing.xxl)) {
        Text("S.Loop Effect", style = Typography.title, color = ColorTextPrimary)

        ButtonRowCompact(
            label = "Preset",
            selectedItem = sLoopEffect.preset,
            items = SLoopPreset.entries.toTypedArray(),
            onItemSelected = { newPreset ->
                onSLoopChange(sLoopEffect.applyPreset(newPreset))
            }
        )

        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xl)) {
            ToggleSwitchWithLabel(
                label = "Mode",
                selectedItem = sLoopEffect.mode,
                offItem = SLoopMode.MANUAL,
                onItem = SLoopMode.AUTO,
                onItemSelected = { newMode ->
                    onSLoopChange(sLoopEffect.copy(mode = newMode).reconcilePreset())
                },
                offLabel = "Manual",
                onLabel = "Auto"
            )

            ToggleSwitchWithLabel(
                label = "Timing",
                selectedItem = sLoopEffect.timing,
                offItem = SLoopTiming.FIRST_HALF,
                onItem = SLoopTiming.SECOND_HALF,
                onItemSelected = { newTiming ->
                    onSLoopChange(sLoopEffect.copy(timing = newTiming))
                },
                offLabel = "1st Half",
                onLabel = "2nd Half"
            )
        }

        Switch(
            checked = sLoopEffect.rateSync == SyncSwitch.ON,
            onCheckedChange = { isOn ->
                val newRateSync = if (isOn) SyncSwitch.ON else SyncSwitch.OFF
                val newRate = if (isOn) {
                    SLoopRate.EnumRate(SLoopRateEnum.fromIndex(0))
                } else {
                    SLoopRate.IntRate(
                        (sLoopEffect.rate as? SLoopRate.EnumRate)?.rateEnum?.ordinal ?: 0
                    )
                }
                onSLoopChange(
                    sLoopEffect.copy(
                        rateSync = newRateSync,
                        rate = newRate
                    ).reconcilePreset()
                )
            },
            colors = SwitchDefaults.colors(
                checkedTrackColor = ColorAccentOrange,
                uncheckedTrackColor = ColorDivider,
                checkedThumbColor = ColorBackground
            )
        )

        if (sLoopEffect.rateSync == SyncSwitch.ON) {
            ButtonRowCompact(
                label = "Rate",
                selectedItem = (sLoopEffect.rate as? SLoopRate.EnumRate)?.rateEnum ?: SLoopRateEnum.fromIndex(0),
                items = SLoopRateEnum.entries.toTypedArray(),
                onItemSelected = { newRateEnum ->
                    onSLoopChange(sLoopEffect.copy(rate = SLoopRate.EnumRate(newRateEnum)).reconcilePreset())
                }
            )
        } else {
            IntStepSliderWithLabel(
                label = "Rate",
                value = (sLoopEffect.rate as? SLoopRate.IntRate)?.intRate ?: 0,
                range = 0..127,
                onValueChange = { newIntRate ->
                    // ms rate is a continuous param (only the synced note form is structural),
                    // so it must never relabel the preset — no reconcile here (mirrors Filter's ms slider).
                    onSLoopChange(sLoopEffect.copy(rate = SLoopRate.IntRate(newIntRate)))
                }
            )
        }
    }
}