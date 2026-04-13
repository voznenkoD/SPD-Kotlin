package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.common.DelayType
import org.xebia.spdmanager.model.system.fx.common.HighCut
import org.xebia.spdmanager.model.system.fx.common.LowCut
import org.xebia.spdmanager.model.system.fx.common.SyncSwitch
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect
import org.xebia.spdmanager.model.system.fx.subtypes.StereoDelay
import org.xebia.spdmanager.ui.components.common.ButtonRowCompact
import org.xebia.spdmanager.ui.components.common.SliderWithLabel
import org.xebia.spdmanager.ui.components.common.ToggleSwitchWithLabel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StereoDelayView(
    stereoDelay: StereoDelay,
    onFxChange: (FxEffect) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Stereo Delay", fontSize = 18.sp)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                ToggleSwitchWithLabel(
                    label = "Delay Type",
                    selectedItem = stereoDelay.type,
                    offItem = DelayType.NORMAL,
                    onItem = DelayType.PAN,
                    onItemSelected = { newType ->
                        onFxChange(stereoDelay.copy(type = newType))
                    },
                    offLabel = "Normal",
                    onLabel = "Pan"
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                ToggleSwitchWithLabel(
                    label = "Sync Switch",
                    selectedItem = stereoDelay.syncSW,
                    offItem = SyncSwitch.OFF,
                    onItem = SyncSwitch.ON,
                    onItemSelected = { newSyncSW ->
                        onFxChange(stereoDelay.copy(syncSW = newSyncSW))
                    }
                )
            }
        }

        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SliderWithLabel(
                label = "Delay Time",
                value = stereoDelay.delayTime.toFloat(),
                onValueChange = { newDelayTime ->
                    onFxChange(stereoDelay.copy(delayTime = newDelayTime.toInt()))
                },
                valueRange = 0f..100f
            )
            SliderWithLabel(
                label = "Direct Level",
                value = stereoDelay.directLevel.toFloat(),
                onValueChange = { newDirectLevel ->
                    onFxChange(stereoDelay.copy(directLevel = newDirectLevel.toInt()))
                },
                valueRange = 0f..100f
            )
            SliderWithLabel(
                label = "Tap Time",
                value = stereoDelay.tapTime.toFloat(),
                onValueChange = { newTapTime ->
                    onFxChange(stereoDelay.copy(tapTime = newTapTime.toInt()))
                },
                valueRange = 0f..100f
            )
        }

        ButtonRowCompact(
            label = "Low Cut (Hz)",
            selectedItem = stereoDelay.lowCut,
            items = LowCut.entries.toTypedArray(),
            onItemSelected = { newLowCut ->
                onFxChange(stereoDelay.copy(lowCut = newLowCut))
            }
        )

        ButtonRowCompact(
            label = "High Cut (Hz)",
            selectedItem = stereoDelay.highCut,
            items = HighCut.entries.toTypedArray(),
            onItemSelected = { newHighCut ->
                onFxChange(stereoDelay.copy(highCut = newHighCut))
            }
        )
    }
}