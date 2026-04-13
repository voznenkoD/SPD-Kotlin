package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect
import org.xebia.spdmanager.model.system.fx.subtypes.SyncDelay
import org.xebia.spdmanager.ui.components.common.ButtonRowCompact
import org.xebia.spdmanager.ui.components.common.DropdownSelector
import org.xebia.spdmanager.ui.components.common.SliderWithLabel
import org.xebia.spdmanager.ui.components.common.ToggleSwitchWithLabel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SyncDelayView(
    syncDelay: SyncDelay,
    onFxChange: (FxEffect) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Sync Delay", fontSize = 18.sp)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ToggleSwitchWithLabel(
                label = "Delay Type",
                selectedItem = syncDelay.type,
                offItem = DelayType.NORMAL,
                onItem = DelayType.PAN,
                onItemSelected = { newType ->
                    onFxChange(syncDelay.copy(type = newType))
                },
                offLabel = "Normal",
                onLabel = "Pan"
            )
            SliderWithLabel(
                label = "Direct Level",
                value = syncDelay.directLevel.toFloat(),
                onValueChange = { newDirectLevel ->
                    onFxChange(syncDelay.copy(directLevel = newDirectLevel.toInt()))
                },
                valueRange = 0f..100f
            )

            SliderWithLabel(
                label = "Tap Time",
                value = syncDelay.tapTime.toFloat(),
                onValueChange = { newTapTime ->
                    onFxChange(syncDelay.copy(tapTime = newTapTime.toInt()))
                },
                valueRange = 0f..100f
            )
        }

        DropdownSelector(
            label = "Delay Time",
            selectedItem = (syncDelay.delayTime as? DelayTime.EnumTime) ?: DelayTimeEnum.QUARTER,
            items = DelayTimeEnum.entries.toList(),
            onItemSelected = { newDelayTime ->
                onFxChange(syncDelay.copy(delayTime = DelayTime.EnumTime(newDelayTime as DelayTimeEnum)))
            }
        )

        ButtonRowCompact(
            label = "Low Cut (Hz)",
            selectedItem = syncDelay.lowCut,
            items = LowCut.entries.toTypedArray(),
            onItemSelected = { newLowCut ->
                onFxChange(syncDelay.copy(lowCut = newLowCut))
            }
        )
        ButtonRowCompact(
            label = "High Cut (Hz)",
            selectedItem = syncDelay.highCut,
            items = HighCut.entries.toTypedArray(),
            onItemSelected = { newHighCut ->
                onFxChange(syncDelay.copy(highCut = newHighCut))
            }
        )
    }
}