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

    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(0.2f)
                .fillMaxHeight(),
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
        Column(modifier = Modifier.weight(0.6f)) {
            Row(modifier = Modifier.weight(0.75f).border(width = Spacing.s, color = ColorDivider)) {
                MasterEffectView(
                    masterEffectConfig = config.masterEffectConfig,
                    onUpdate = systemViewModel::updateMasterEffectConfig
                )
            }
            Row(modifier = Modifier.weight(0.25f).border(width = Spacing.s, color = ColorDivider)) {
                AudioView(
                    audioConfig = config.systemAudioConfig,
                    onUpdate = systemViewModel::updateSystemAudioConfig
                )
            }
        }
        Box(
            modifier = Modifier
                .weight(0.2f)
                .fillMaxHeight()
                .border(width = Spacing.s, color = ColorDivider)
        ) {
            KitChainView(
                kitChains = config.kitChains,
                kits = kits,
                onMoveInChain = systemViewModel::moveKitInChain,
                onReplaceInChain = systemViewModel::setKitInChain
            )
        }
    }
}