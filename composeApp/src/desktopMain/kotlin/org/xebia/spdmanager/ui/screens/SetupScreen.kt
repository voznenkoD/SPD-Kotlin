package org.xebia.spdmanager.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.LocalDeviceManager
import org.xebia.spdmanager.ui.components.setup.PadSetupView
import org.xebia.spdmanager.ui.components.setup.PadsSetupScreen
import org.xebia.spdmanager.ui.components.setup.SetupGeneralView
import org.xebia.spdmanager.ui.components.setup.SetupMidiView
import org.xebia.spdmanager.viewmodel.SetupViewModel

@Composable
fun SetupScreen() {
    val deviceManager = LocalDeviceManager.current

    val setupViewModel = remember(deviceManager) {
        SetupViewModel(deviceManager)
    }

    val setupConfig by setupViewModel.setupConfig.collectAsState()
    val padFsControl by setupViewModel.padFsControl.collectAsState()
    val selectedPadNumber by setupViewModel.selectedPadNumber.collectAsState()

    if (setupConfig == null || padFsControl == null) {
        return
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.weight(0.6f).fillMaxWidth()) {
            Column(
                modifier = Modifier.weight(0.2f).fillMaxHeight().border(width = 2.dp, color = Color.DarkGray)
            ) {
                SetupGeneralView(
                    setupConfig = setupConfig!!,
                    onUpdate = setupViewModel::updateSetupConfig
                )
            }
            Column(
                modifier = Modifier.weight(0.5f).fillMaxHeight().border(width = 2.dp, color = Color.DarkGray)
            ) {
                PadsSetupScreen(
                    selectedPadNumber = selectedPadNumber,
                    onSelect = setupViewModel::selectPad
                )
            }
            Column(
                modifier = Modifier.weight(0.3f).fillMaxHeight().border(width = 2.dp, color = Color.DarkGray)
            ) {
                SetupMidiView(
                    setupConfig = setupConfig!!,
                    onUpdate = setupViewModel::updateSetupConfig
                )
            }
        }
        Row(modifier = Modifier.weight(0.4f).fillMaxWidth().border(width = 2.dp, color = Color.DarkGray)) {
            val currentPadControl = padFsControl!![selectedPadNumber] ?: return@Row

            PadSetupView(
                setupConfig = setupConfig!!,
                padNumber = selectedPadNumber,
                padFsControl = currentPadControl,
                onUpdateSetupConfig = setupViewModel::updateSetupConfig,
                onUpdatePadControl = { control ->
                    setupViewModel.updatePadFsControl(selectedPadNumber, control)
                }
            )
        }
    }
}