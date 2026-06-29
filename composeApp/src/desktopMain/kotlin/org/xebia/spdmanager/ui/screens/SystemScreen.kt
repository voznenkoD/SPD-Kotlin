package org.xebia.spdmanager.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.ui.theme.*
import org.xebia.spdmanager.LocalDeviceManager
import org.xebia.spdmanager.ui.components.common.SelectFolderButton
import org.xebia.spdmanager.ui.components.system.AudioView
import org.xebia.spdmanager.ui.components.system.ClickView
import org.xebia.spdmanager.ui.components.system.KitChainView
import org.xebia.spdmanager.ui.components.system.VisualControlView
import org.xebia.spdmanager.ui.components.system.masterfx.MasterEffectView
import org.xebia.spdmanager.ui.panels.DetachablePanelWindow
import org.xebia.spdmanager.ui.panels.PanelLayoutState
import org.xebia.spdmanager.ui.panels.PanelRegion
import org.xebia.spdmanager.ui.panels.ScreenId
import org.xebia.spdmanager.viewmodel.SystemViewModel

@Composable
fun SystemScreen() {
    val deviceManager = LocalDeviceManager.current

    val systemViewModel = remember(deviceManager) {
        SystemViewModel(deviceManager)
    }

    val systemConfig by systemViewModel.systemConfig.collectAsState()
    val waves by systemViewModel.waves.collectAsState()
    val kits by systemViewModel.kits.collectAsState()

    val config = systemConfig

    if (config == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            SelectFolderButton()
        }
        return
    }

    val leftContent: @Composable () -> Unit = {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.weight(0.6f).border(width = Spacing.s, color = ColorDivider)) {
                ClickView(
                    clickConfig = config.clickConfig,
                    waves = waves,
                    onUpdate = systemViewModel::updateClickConfig
                )
            }
            Box(modifier = Modifier.weight(0.4f).border(width = Spacing.s, color = ColorDivider)) {
                VisualControlView(
                    visualControl = config.visualControl,
                    onUpdate = systemViewModel::updateVisualControl
                )
            }
        }
    }

    val bottomContent: @Composable () -> Unit = {
        AudioView(
            audioConfig = config.systemAudioConfig,
            onUpdate = systemViewModel::updateSystemAudioConfig
        )
    }

    val rightContent: @Composable () -> Unit = {
        KitChainView(
            kitChains = config.kitChains,
            kits = kits,
            onMoveInChain = systemViewModel::moveKitInChain,
            onReplaceInChain = systemViewModel::setKitInChain,
            onInitChain = systemViewModel::initializeKitChain
        )
    }

    val leftEntry = PanelLayoutState.entry(ScreenId.System, PanelRegion.Left)
    val rightEntry = PanelLayoutState.entry(ScreenId.System, PanelRegion.Right)
    val bottomEntry = PanelLayoutState.entry(ScreenId.System, PanelRegion.Bottom)

    Row(modifier = Modifier.fillMaxSize()) {
        if (leftEntry.docked) {
            Column(
                modifier = Modifier
                    .weight(0.2f)
                    .fillMaxHeight()
            ) {
                leftContent()
            }
        }
        Column(modifier = Modifier.weight(0.6f)) {
            Row(modifier = Modifier.weight(0.75f).border(width = Spacing.s, color = ColorDivider)) {
                MasterEffectView(
                    masterEffectConfig = config.masterEffectConfig,
                    onUpdate = systemViewModel::updateMasterEffectConfig
                )
            }
            if (bottomEntry.docked) {
                Row(modifier = Modifier.weight(0.25f).border(width = Spacing.s, color = ColorDivider)) {
                    bottomContent()
                }
            }
        }
        if (rightEntry.docked) {
            Box(
                modifier = Modifier
                    .weight(0.2f)
                    .fillMaxHeight()
                    .border(width = Spacing.s, color = ColorDivider)
            ) {
                rightContent()
            }
        }
    }

    DetachablePanelWindow(leftEntry, PanelRegion.Left, leftContent)
    DetachablePanelWindow(rightEntry, PanelRegion.Right, rightContent)
    DetachablePanelWindow(bottomEntry, PanelRegion.Bottom, bottomContent)
}