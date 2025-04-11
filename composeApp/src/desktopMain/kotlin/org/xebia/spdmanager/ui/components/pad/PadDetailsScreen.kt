package org.xebia.spdmanager.ui.components.pad

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.kit.pad.Pad
import org.xebia.spdmanager.model.kit.pad.PadOutput
import org.xebia.spdmanager.model.system.fx.common.SyncSwitch
import org.xebia.spdmanager.ui.components.common.DropdownSelector
import org.xebia.spdmanager.ui.components.common.SwitchWithLabel

@Composable
fun PadDetailsScreen(pad: Pad?) {
    var selectedTab by remember { mutableStateOf(0) }

    if (pad == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Choose Pad please", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        return
    }

    var editablePad by remember(pad) { mutableStateOf(pad) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        SoundSection(title = "Main", sound = editablePad.main)
        SoundSection(title = "Sub", sound = editablePad.sub)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
            ) {
                MuteGroupSelector(
                    selectedMuteGroup = editablePad.muteGroup,
                    onMuteGroupSelected = { newGroup -> editablePad = editablePad.copy(muteGroup = newGroup) }
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
            ) {
                SwitchWithLabel(
                    "Tempo Sync",
                    syncSwitch = editablePad.tempoSync,
                    onValueChange = { newSync -> editablePad = editablePad.copy(tempoSync = SyncSwitch.fromBoolean(newSync)) }
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
            ) {
                DropdownSelector(
                    label = "Output",
                    selectedItem = editablePad.output,
                    onItemSelected = { newOutput -> editablePad = editablePad.copy(output = newOutput) },
                    items = PadOutput.entries
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

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
            0 -> PadModeView(editablePad.padMode)
            1 -> MidiParamsView(editablePad.midiParams)
        }
    }
}
