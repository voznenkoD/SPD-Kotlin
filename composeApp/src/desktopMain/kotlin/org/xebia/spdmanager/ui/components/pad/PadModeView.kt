package org.xebia.spdmanager.ui.components.pad

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.kit.pad.mode.*
import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.ui.components.common.ButtonRow

@Composable
fun PadModeView(padMode: PadMode) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Pad Mode", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        // PadTemplate - ButtonRow for enum
        ButtonRow(
            label = "Template",
            items = PadTemplate.entries.toTypedArray(),
            selectedItem = padMode.template,
            onItemSelected = { newTemplate ->
                // Handle template selection
            }
        )

        // PadLoop - ButtonRow for enum
        ButtonRow(
            label = "Loop",
            items = PadLoop.entries.toTypedArray(),
            selectedItem = padMode.loop,
            onItemSelected = { newLoop ->
                // Handle loop selection
            }
        )

        // TrigType - ButtonRow for enum
        ButtonRow(
            label = "Trigger Type",
            items = TrigType.entries.toTypedArray(),
            selectedItem = padMode.trigType,
            onItemSelected = { newTrigType ->
                // Handle trig type selection
            }
        )

        // Dynamics - ButtonRow for enum
        ButtonRow(
            label = "Dynamics",
            items = SyncSwitch.entries.toTypedArray(),
            selectedItem = padMode.dynamics,
            onItemSelected = { newDynamics ->
                // Handle dynamics selection
            }
        )

        // PolyMono - ButtonRow for enum
        ButtonRow(
            label = "PolyMono",
            items = PolyMono.entries.toTypedArray(),
            selectedItem = padMode.polyMono,
            onItemSelected = { newPolyMono ->
                // Handle polyMono selection
            }
        )
    }
}
