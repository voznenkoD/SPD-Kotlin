package org.xebia.spdmanager.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.ui.theme.*
import org.xebia.spdmanager.LocalDeviceManager
import org.xebia.spdmanager.ui.components.setup.PadSetupView
import org.xebia.spdmanager.ui.components.setup.PadsSetupScreen
import org.xebia.spdmanager.ui.components.setup.SetupGeneralView
import org.xebia.spdmanager.ui.components.setup.SetupMidiView
import org.xebia.spdmanager.ui.panels.DetachablePanelWindow
import org.xebia.spdmanager.ui.panels.PanelLayoutState
import org.xebia.spdmanager.ui.panels.PanelRegion
import org.xebia.spdmanager.ui.panels.ScreenId
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

    val leftContent: @Composable () -> Unit = {
        SetupGeneralView(
            setupConfig = setupConfig!!,
            onUpdate = setupViewModel::updateSetupConfig
        )
    }

    val rightContent: @Composable () -> Unit = {
        SetupMidiView(
            setupConfig = setupConfig!!,
            onUpdate = setupViewModel::updateSetupConfig
        )
    }

    val bottomContent: @Composable () -> Unit = {
        val currentPadControl = padFsControl!![selectedPadNumber]
        if (currentPadControl != null) {
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

    val leftEntry = PanelLayoutState.entry(ScreenId.Setup, PanelRegion.Left)
    val rightEntry = PanelLayoutState.entry(ScreenId.Setup, PanelRegion.Right)
    val bottomEntry = PanelLayoutState.entry(ScreenId.Setup, PanelRegion.Bottom)

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.weight(0.75f).fillMaxWidth()) {
            if (leftEntry.docked) {
                Column(
                    modifier = Modifier.weight(0.2f).fillMaxHeight().border(width = Spacing.s, color = ColorDivider)
                ) {
                    leftContent()
                }
            }
            Column(
                modifier = Modifier.weight(0.6f).fillMaxHeight().border(width = Spacing.s, color = ColorDivider)
            ) {
                PadsSetupScreen(
                    selectedPadNumber = selectedPadNumber,
                    onSelect = setupViewModel::selectPad
                )
            }
            if (rightEntry.docked) {
                Column(
                    modifier = Modifier.weight(0.2f).fillMaxHeight().border(width = Spacing.s, color = ColorDivider)
                ) {
                    rightContent()
                }
            }
        }
        if (bottomEntry.docked) {
            Row(modifier = Modifier.weight(0.25f).fillMaxWidth().border(width = Spacing.s, color = ColorDivider)) {
                bottomContent()
            }
        }
    }

    DetachablePanelWindow(leftEntry, PanelRegion.Left, leftContent)
    DetachablePanelWindow(rightEntry, PanelRegion.Right, rightContent)
    DetachablePanelWindow(bottomEntry, PanelRegion.Bottom, bottomContent)
}