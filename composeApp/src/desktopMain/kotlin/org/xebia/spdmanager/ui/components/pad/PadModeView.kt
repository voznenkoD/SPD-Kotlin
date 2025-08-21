package org.xebia.spdmanager.ui.components.pad

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.model.kit.pad.mode.*
import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.ui.components.common.ButtonRow

@Composable
fun PadModeView(
    padMode: PadMode,
    onPadModeChange: (PadMode) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        ButtonRow(
            label = "Template",
            items = PadTemplate.entries.toTypedArray(),
            selectedItem = padMode.template,
            onItemSelected = { newTemplate ->
                onPadModeChange(padMode.copy(template = newTemplate))
            }
        )

        ButtonRow(
            label = "Loop",
            items = PadLoop.entries.toTypedArray(),
            selectedItem = padMode.loop,
            onItemSelected = { newLoop ->
                onPadModeChange(padMode.copy(loop = newLoop))
            }
        )

        ButtonRow(
            label = "Trigger Type",
            items = TrigType.entries.toTypedArray(),
            selectedItem = padMode.trigType,
            onItemSelected = { newTrigType ->
                onPadModeChange(padMode.copy(trigType = newTrigType))
            }
        )

        ButtonRow(
            label = "Dynamics",
            items = SyncSwitch.entries.toTypedArray(),
            selectedItem = padMode.dynamics,
            onItemSelected = { newDynamics ->
                onPadModeChange(padMode.copy(dynamics = newDynamics))
            }
        )

        ButtonRow(
            label = "PolyMono",
            items = PolyMono.entries.toTypedArray(),
            selectedItem = padMode.polyMono,
            onItemSelected = { newPolyMono ->
                onPadModeChange(padMode.copy(polyMono = newPolyMono))
            }
        )
    }
}