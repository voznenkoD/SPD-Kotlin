package org.xebia.spdmanager.ui.components.pad

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.ui.theme.*
import org.xebia.spdmanager.model.kit.pad.mode.*
import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.ui.components.common.ButtonRowCompact
import org.xebia.spdmanager.ui.components.common.ToggleSwitchWithLabel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PadModeView(
    padMode: PadMode,
    onPadModeChange: (PadMode) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth().padding(Spacing.xxl),
        horizontalArrangement = Arrangement.spacedBy(Spacing.xl)
    ) {
        ButtonRowCompact(
            label = "Template",
            items = PadTemplate.entries.toTypedArray(),
            selectedItem = padMode.template,
            onItemSelected = { newTemplate ->
                onPadModeChange(padMode.copy(template = newTemplate))
            }
        )

        ButtonRowCompact(
            label = "Loop",
            items = PadLoop.entries.toTypedArray(),
            selectedItem = padMode.loop,
            onItemSelected = { newLoop ->
                onPadModeChange(padMode.copy(loop = newLoop))
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.xl),
            verticalAlignment = Alignment.CenterVertically
        ) {

            ToggleSwitchWithLabel(
                label = "Trigger Type",
                selectedItem = padMode.trigType,
                offItem = TrigType.SHOT,
                onItem = TrigType.ALT,
                onItemSelected = { newTrigType ->
                    onPadModeChange(padMode.copy(trigType = newTrigType))
                },
                offLabel = "Shot",
                onLabel = "Alt"
            )

            ToggleSwitchWithLabel(
                label = "Dynamics",
                selectedItem = padMode.dynamics,
                offItem = SyncSwitch.OFF,
                onItem = SyncSwitch.ON,
                onItemSelected = { newDynamics ->
                    onPadModeChange(padMode.copy(dynamics = newDynamics))
                }
            )

            ToggleSwitchWithLabel(
                label = "PolyMono",
                selectedItem = padMode.polyMono,
                offItem = PolyMono.MONO,
                onItem = PolyMono.POLY,
                onItemSelected = { newPolyMono ->
                    onPadModeChange(padMode.copy(polyMono = newPolyMono))
                },
                offLabel = "Mono",
                onLabel = "Poly"
            )
        }
    }
}