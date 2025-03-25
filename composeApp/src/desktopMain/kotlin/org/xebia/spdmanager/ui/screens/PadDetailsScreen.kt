package org.xebia.spdmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.kit.pad.Pad
import org.xebia.spdmanager.model.kit.pad.PadOutput
import org.xebia.spdmanager.model.system.fx.common.SyncSwitch
import org.xebia.spdmanager.ui.components.common.ButtonRow
import org.xebia.spdmanager.ui.components.pad.*

@Composable
fun PadDetailsScreen(pad: Pad?) {
    var selectedTab by remember { mutableStateOf(0) }
    var selectedPad by remember { mutableStateOf(pad) }

    // Show message if no Pad is selected
    selectedPad?.let {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("Pad Details", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            // Main Sound Section
            SoundSection(title = "Main", sound = it.main)

            // Sub Sound Section
            SoundSection(title = "Sub", sound = it.sub)

            // Mute Group Section (Button Row)
            MuteGroupSelector(
                selectedGroup = it.muteGroup,
                onGroupSelected = { newGroup -> selectedPad = it.copy(muteGroup = newGroup) }
            )

            // Tempo Sync Section (Button Row)
            ButtonRow(
                label = "Tempo Sync",
                items = SyncSwitch.entries.toTypedArray(),
                selectedItem = it.tempoSync,
                onItemSelected = { newSync -> selectedPad = it.copy(tempoSync = newSync) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ButtonRow(
                label = "Output",
                items = PadOutput.entries.toTypedArray(),
                selectedItem = it.output,
                onItemSelected = { newOutput -> selectedPad = it.copy(output = newOutput) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom Tabs (Mode, MIDI)
            TabRow(selectedTabIndex = selectedTab) {
                listOf("Mode", "MIDI").forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTab) {
                0 -> PadModeView(it.padMode)
                1 -> MidiParamsView(it.midiParams)
            }
        }
    } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Choose Pad please", fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}
