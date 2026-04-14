package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.ui.theme.*
import org.xebia.spdmanager.ui.theme.Typography
import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.model.system.fx.subtypes.Filter
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect
import org.xebia.spdmanager.ui.components.common.ButtonRowCompact
import org.xebia.spdmanager.ui.components.common.SliderWithLabel
import org.xebia.spdmanager.ui.components.common.ToggleSwitchWithLabel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterView(
    fx: Filter,
    onFxChange: (FxEffect) -> Unit
) {
    Column(modifier = Modifier.padding(Spacing.m).fillMaxWidth()) {
        Text("Filter Settings", fontSize = Typography.titleSize)

        ButtonRowCompact(
            label = "Filter Type",
            selectedItem = fx.type,
            items = FilterType.entries.toTypedArray(),
            onItemSelected = { newType ->
                onFxChange(fx.copy(type = newType))
            }
        )

        ButtonRowCompact(
            label = "Slope",
            selectedItem = fx.slope,
            items = FilterSlope.entries.toTypedArray(),
            onItemSelected = { newSlope ->
                onFxChange(fx.copy(slope = newSlope))
            }
        )

        FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xl)) {

            ToggleSwitchWithLabel(
                label = "Rate Sync",
                selectedItem = fx.rateSyncSW,
                offItem = SyncSwitch.OFF,
                onItem = SyncSwitch.ON,
                onItemSelected = { newRateSyncSW ->
                    onFxChange(fx.copy(rateSyncSW = newRateSyncSW))
                }
            )

            SliderWithLabel(
                label = "Resonance",
                value = fx.resonance.toFloat(),
                onValueChange = { newResonance ->
                    onFxChange(fx.copy(resonance = newResonance.toInt()))
                },
                valueRange = 0f..100f
            )

            if (fx.rateSyncSW == SyncSwitch.ON) {
                val modRateEnum = (fx.modRate as? ModRate.EnumRate)?.modRateEnum ?: ModRateEnum.QUARTER
                ButtonRowCompact(
                    label = "Modulation Rate",
                    selectedItem = modRateEnum,
                    items = ModRateEnum.entries.toTypedArray(),
                    onItemSelected = { newModRateEnum ->
                        onFxChange(fx.copy(modRate = ModRate.EnumRate(newModRateEnum)))
                    }
                )
            } else {
                val modRateInt = (fx.modRate as? ModRate.IntRate)?.intRate?.toFloat() ?: 0f
                SliderWithLabel(
                    label = "Modulation Rate",
                    value = modRateInt,
                    onValueChange = { newModRate ->
                        onFxChange(fx.copy(modRate = ModRate.IntRate(newModRate.toInt())))
                    },
                    valueRange = 0f..100f
                )
            }
        }

        ButtonRowCompact(
            label = "LFO Wave",
            selectedItem = fx.lfoWave,
            items = LfoWave.entries.toTypedArray(),
            onItemSelected = { newLfoWave ->
                onFxChange(fx.copy(lfoWave = newLfoWave))
            }
        )
    }
}