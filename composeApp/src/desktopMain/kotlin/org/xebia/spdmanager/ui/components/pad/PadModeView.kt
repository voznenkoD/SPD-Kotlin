package org.xebia.spdmanager.ui.components.pad

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.model.kit.pad.mode.*
import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.ui.components.common.ButtonRow

@Composable
fun PadModeView(padMode: PadMode) {
    var mode by remember { mutableStateOf(padMode) }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        ButtonRow(
            label = "Template",
            items = PadTemplate.entries.toTypedArray(),
            selectedItem = mode.template,
            onItemSelected = { newTemplate ->
            }
        )

        ButtonRow(
            label = "Loop",
            items = PadLoop.entries.toTypedArray(),
            selectedItem = mode.loop,
            onItemSelected = { newLoop ->
                // Handle loop selection
            }
        )

        ButtonRow(
            label = "Trigger Type",
            items = TrigType.entries.toTypedArray(),
            selectedItem = mode.trigType,
            onItemSelected = { newTrigType ->
            }
        )

        ButtonRow(
            label = "Dynamics",
            items = SyncSwitch.entries.toTypedArray(),
            selectedItem = mode.dynamics,
            onItemSelected = { newDynamics ->
            }
        )

        ButtonRow(
            label = "PolyMono",
            items = PolyMono.entries.toTypedArray(),
            selectedItem = mode.polyMono,
            onItemSelected = { newPolyMono ->
            }
        )
    }
}
