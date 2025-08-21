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
import org.xebia.spdmanager.ui.components.common.IntStepSliderWithLabel

@Composable
fun SLoopEffectView(
    sLoopEffect: SLoopEffect,
    onSLoopChange: (SLoopEffect) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("S.Loop Effect", style = MaterialTheme.typography.titleMedium)

        ButtonRow(
            label = "Preset",
            selectedItem = sLoopEffect.preset,
            items = SLoopPreset.entries.toTypedArray(),
            onItemSelected = { newPreset ->
                onSLoopChange(sLoopEffect.copy(preset = newPreset))
            }
        )

        ButtonRow(
            label = "Mode",
            selectedItem = sLoopEffect.mode,
            items = SLoopMode.entries.toTypedArray(),
            onItemSelected = { newMode ->
                onSLoopChange(sLoopEffect.copy(mode = newMode))
            }
        )

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
                    )
                )
            }
        )

        if (sLoopEffect.rateSync == SyncSwitch.ON) {
            ButtonRow(
                label = "Rate",
                selectedItem = (sLoopEffect.rate as? SLoopRate.EnumRate)?.rateEnum ?: SLoopRateEnum.fromIndex(0),
                items = SLoopRateEnum.entries.toTypedArray(),
                onItemSelected = { newRateEnum ->
                    onSLoopChange(sLoopEffect.copy(rate = SLoopRate.EnumRate(newRateEnum)))
                }
            )
        } else {
            IntStepSliderWithLabel(
                label = "Rate",
                value = (sLoopEffect.rate as? SLoopRate.IntRate)?.intRate ?: 0,
                range = 0..127,
                onValueChange = { newIntRate ->
                    onSLoopChange(sLoopEffect.copy(rate = SLoopRate.IntRate(newIntRate)))
                }
            )
        }

        ButtonRow(
            label = "Timing",
            selectedItem = sLoopEffect.timing,
            items = SLoopTiming.entries.toTypedArray(),
            onItemSelected = { newTiming ->
                onSLoopChange(sLoopEffect.copy(timing = newTiming))
            }
        )
    }
}