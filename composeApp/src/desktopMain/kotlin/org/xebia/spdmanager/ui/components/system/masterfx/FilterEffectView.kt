package org.xebia.spdmanager.ui.components.system.masterfx

import androidx.compose.foundation.layout.Column
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
import org.xebia.spdmanager.model.system.fx.mainTypes.FilterEffect
import org.xebia.spdmanager.model.system.fx.mainTypes.FilterPreset
import org.xebia.spdmanager.model.system.fx.reconcilePreset
import org.xebia.spdmanager.ui.components.common.ButtonRowCompact
import org.xebia.spdmanager.ui.components.common.DropdownSelector
import org.xebia.spdmanager.ui.components.common.IntStepSliderWithLabel

@Composable
fun FilterEffectView(
    filterEffect: FilterEffect,
    onFilterChange: (FilterEffect) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(Spacing.xxl)) {
        Text("Filter Effect", style = Typography.title, color = ColorTextPrimary)

        ButtonRowCompact(
            label = "Preset",
            selectedItem = filterEffect.preset,
            items = FilterPreset.entries.toTypedArray(),
            onItemSelected = { newPreset ->
                onFilterChange(filterEffect.applyPreset(newPreset))
            }
        )

        ButtonRowCompact(
            label = "Type",
            selectedItem = filterEffect.type,
            items = FilterType.entries.toTypedArray(),
            onItemSelected = { newType ->
                onFilterChange(filterEffect.copy(type = newType).reconcilePreset())
            }
        )

        ButtonRowCompact(
            label = "Slope",
            selectedItem = filterEffect.slope,
            items = FilterSlope.entries.toTypedArray(),
            onItemSelected = { newSlope ->
                onFilterChange(filterEffect.copy(slope = newSlope))
            }
        )

        Switch(
            checked = filterEffect.rateSync == SyncSwitch.ON,
            onCheckedChange = { isOn ->
                val newRateSync = if (isOn) SyncSwitch.ON else SyncSwitch.OFF
                val newModRate = if (isOn) {
                    ModRate.EnumRate(ModRateEnum.fromIndex(0))
                } else {
                    ModRate.IntRate(
                        (filterEffect.modRate as? ModRate.EnumRate)?.modRateEnum?.ordinal ?: 0
                    )
                }
                onFilterChange(
                    filterEffect.copy(
                        rateSync = newRateSync,
                        modRate = newModRate
                    ).reconcilePreset()
                )
            },
            colors = SwitchDefaults.colors(
                checkedTrackColor = ColorAccentOrange,
                uncheckedTrackColor = ColorDivider,
                checkedThumbColor = ColorBackground
            )
        )

        if (filterEffect.rateSync == SyncSwitch.ON) {
            ButtonRowCompact(
                label = "Mod Rate",
                selectedItem = (filterEffect.modRate as? ModRate.EnumRate)?.modRateEnum ?: ModRateEnum.fromIndex(0),
                items = ModRateEnum.entries.toTypedArray(),
                onItemSelected = { newModRateEnum ->
                    onFilterChange(filterEffect.copy(modRate = ModRate.EnumRate(newModRateEnum)))
                }
            )
        } else {
            IntStepSliderWithLabel(
                label = "Mod Rate (ms)",
                value = (filterEffect.modRate as? ModRate.IntRate)?.intRate ?: 0,
                range = 0..100,
                onValueChange = { newIntRate ->
                    onFilterChange(filterEffect.copy(modRate = ModRate.IntRate(newIntRate)))
                }
            )
        }

        IntStepSliderWithLabel(
            label = "Mod Depth",
            value = filterEffect.modDepth,
            range = 0..100,
            onValueChange = { newModDepth ->
                onFilterChange(filterEffect.copy(modDepth = newModDepth))
            }
        )

        DropdownSelector(
            label = "LFO Wave",
            selectedItem = filterEffect.lfoWave,
            items = LfoWave.entries,
            onItemSelected = { newLfoWave ->
                onFilterChange(filterEffect.copy(lfoWave = newLfoWave))
            }
        )
    }
}