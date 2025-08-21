package org.xebia.spdmanager.ui.components.pad

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.kit.pad.PadNumber
import org.xebia.spdmanager.model.kit.pad.PadOutput
import org.xebia.spdmanager.model.system.fx.common.SyncSwitch
import org.xebia.spdmanager.service.DeviceManager
import org.xebia.spdmanager.ui.components.common.DropdownSelector
import org.xebia.spdmanager.ui.components.common.SwitchWithLabel
import org.xebia.spdmanager.viewmodel.PadViewModel

@Composable
fun PadDetailsScreen(
    padNumber: PadNumber,
    kitIndex: Int,
    deviceManager: DeviceManager
) {
    val viewModel = remember(kitIndex, padNumber) {
        PadViewModel(kitIndex, padNumber, deviceManager)
    }

    val pad = viewModel.pad

    var selectedTab by remember { mutableStateOf(0) }

    if (pad == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Pad not found", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        SoundSection(
            title = "Main",
            sound = pad.main,
            onSoundChange = { newSound ->
                viewModel.updateMainSound(newSound)
            }
        )

        SoundSection(
            title = "Sub",
            sound = pad.sub,
            onSoundChange = { newSound ->
                viewModel.updateSubSound(newSound)
            }
        )

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
                    selectedMuteGroup = pad.muteGroup,
                    onMuteGroupSelected = viewModel::updateMuteGroup
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
            ) {
                SwitchWithLabel(
                    label = "Tempo Sync",
                    syncSwitch = pad.tempoSync,
                    onValueChange = { isOn ->
                        viewModel.updateTempoSync(SyncSwitch.fromBoolean(isOn))
                    }
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
            ) {
                DropdownSelector(
                    label = "Output",
                    selectedItem = pad.output,
                    onItemSelected = viewModel::updateOutput,
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
            0 -> PadModeView(
                padMode = pad.padMode,
                onPadModeChange = viewModel::updatePadMode
            )
            1 -> MidiParamsView(
                midiParams = pad.midiParams,
                onMidiParamsChange = viewModel::updateMidiParams
            )
        }
    }
}