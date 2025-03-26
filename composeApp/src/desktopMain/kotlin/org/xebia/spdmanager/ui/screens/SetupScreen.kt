package org.xebia.spdmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.LocalDeviceManager
import org.xebia.spdmanager.model.kit.pad.PadNumber
import org.xebia.spdmanager.ui.components.setup.PadSetupView
import org.xebia.spdmanager.ui.components.setup.SetupGeneralView
import org.xebia.spdmanager.ui.components.setup.SetupMidiView

@Composable
fun SetupScreen() {
    val deviceManager = LocalDeviceManager.current
    val device = deviceManager.device

    var selectedPad by remember { mutableStateOf(PadNumber.PAD_1) }
    var currentSetupConfig by remember { mutableStateOf(device!!.setupConfig) }
    var padFsControl by remember { mutableStateOf(device!!.systemConfig.padFsControl) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.weight(0.7f).fillMaxWidth().padding(5.dp)) {
            Column(
                modifier = Modifier.weight(0.2f).fillMaxHeight().padding(8.dp)
            ) {
                SetupGeneralView(currentSetupConfig) { updatedConfig ->
                    currentSetupConfig = updatedConfig
                    deviceManager.device = device?.copy(setupConfig = updatedConfig)
                }

            }
            Column(
                modifier = Modifier.weight(0.6f).fillMaxHeight().padding(8.dp)
            ) {
                PadsSetupScreen(onSelect = { selectedPad = it })
            }

            Column(
                modifier = Modifier.weight(0.2f).fillMaxHeight().padding(8.dp)
            ) {
                SetupMidiView(setupConfig = currentSetupConfig) { updatedConfig ->
                    currentSetupConfig = updatedConfig
                    deviceManager.device = device?.copy(setupConfig = updatedConfig)
                }
            }
        }
        Row(modifier = Modifier.weight(0.3f).fillMaxWidth().padding(5.dp)) {
            PadSetupView(currentSetupConfig, selectedPad, padFsControl.get(selectedPad)!!)
        }
    }
}