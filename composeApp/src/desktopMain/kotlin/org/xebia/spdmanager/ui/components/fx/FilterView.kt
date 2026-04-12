package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.model.system.fx.subtypes.Filter
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect
import org.xebia.spdmanager.ui.components.common.ButtonRow
import org.xebia.spdmanager.ui.components.common.SliderWithLabel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterView(
    fx: Filter,
    onFxChange: (FxEffect) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Filter Settings", fontSize = 18.sp)

        ButtonRow(
            label = "Filter Type",
            selectedItem = fx.type,
            items = FilterType.entries.toTypedArray(),
            onItemSelected = { newType ->
                onFxChange(fx.copy(type = newType))
            }
        )

        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SliderWithLabel(
                label = "Resonance",
                value = fx.resonance.toFloat(),
                onValueChange = { newResonance ->
                    onFxChange(fx.copy(resonance = newResonance.toInt()))
                },
                valueRange = 0f..100f
            )
        }

        ButtonRow(
            label = "Slope",
            selectedItem = fx.slope,
            items = FilterSlope.entries.toTypedArray(),
            onItemSelected = { newSlope ->
                onFxChange(fx.copy(slope = newSlope))
            }
        )

        ButtonRow(
            label = "Rate Sync",
            selectedItem = fx.rateSyncSW,
            items = SyncSwitch.entries.toTypedArray(),
            onItemSelected = { newRateSyncSW ->
                onFxChange(fx.copy(rateSyncSW = newRateSyncSW))
            }
        )

        if (fx.rateSyncSW == SyncSwitch.ON) {
            val modRateEnum = (fx.modRate as? ModRate.EnumRate)?.modRateEnum ?: ModRateEnum.QUARTER
            ButtonRow(
                label = "Modulation Rate",
                selectedItem = modRateEnum,
                items = ModRateEnum.entries.toTypedArray(),
                onItemSelected = { newModRateEnum ->
                    onFxChange(fx.copy(modRate = ModRate.EnumRate(newModRateEnum)))
                }
            )
        } else {
            val modRateInt = (fx.modRate as? ModRate.IntRate)?.intRate?.toFloat() ?: 0f
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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

        ButtonRow(
            label = "LFO Wave",
            selectedItem = fx.lfoWave,
            items = LfoWave.entries.toTypedArray(),
            onItemSelected = { newLfoWave ->
                onFxChange(fx.copy(lfoWave = newLfoWave))
            }
        )
    }
}