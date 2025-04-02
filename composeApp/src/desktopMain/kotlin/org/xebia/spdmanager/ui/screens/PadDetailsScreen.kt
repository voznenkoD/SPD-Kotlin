package org.xebia.spdmanager.ui.screens

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
import org.xebia.spdmanager.ui.components.pad.*

@Composable
fun PadDetailsScreen(pad: Pad?) {
    var selectedTab by remember { mutableStateOf(0) }
    var selectedPad by remember { mutableStateOf(pad) }

    if (selectedPad == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Choose Pad please", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        SoundSection(title = "Main", sound = selectedPad!!.main)
        SoundSection(title = "Sub", sound = selectedPad!!.sub)

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
                    selectedMuteGroup = selectedPad!!.muteGroup,
                    onMuteGroupSelected = { newGroup -> selectedPad = selectedPad!!.copy(muteGroup = newGroup) }
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
            ) {
                SwitchWithLabel(
                    "Tempo Sync",
                    syncSwitch = selectedPad!!.tempoSync,
                    onValueChange = { newSync -> selectedPad = selectedPad!!.copy(tempoSync = SyncSwitch.fromBoolean(newSync)) }
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
            ) {
                DropdownSelector(
                    label = "Output",
                    selectedItem = selectedPad!!.output,
                    onItemSelected = { newOutput -> selectedPad = selectedPad!!.copy(output = newOutput) },
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
            0 -> PadModeView(selectedPad!!.padMode)
            1 -> MidiParamsView(selectedPad!!.midiParams)
        }
    }
}
