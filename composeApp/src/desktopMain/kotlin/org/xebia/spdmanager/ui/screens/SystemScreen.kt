package org.xebia.spdmanager.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
                .weight(0.15f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.weight(0.6f).border(width = 2.dp, color = Color.DarkGray)) {
                ClickView(
                    clickConfig = config.clickConfig,
                    waves = waves,
                    onUpdate = systemViewModel::updateClickConfig
                )
            }
            Box(modifier = Modifier.weight(0.4f).border(width = 2.dp, color = Color.DarkGray)) {
                VisualControlView(
                    visualControl = config.visualControl,
                    onUpdate = systemViewModel::updateVisualControl
                )
            }
        }
        Column(modifier = Modifier.weight(0.65f)) {
            Row(modifier = Modifier.weight(0.65f).border(width = 2.dp, color = Color.DarkGray)) {
                MasterEffectView(
                    masterEffectConfig = config.masterEffectConfig,
                    onUpdate = systemViewModel::updateMasterEffectConfig
                )
            }
            Row(modifier = Modifier.weight(0.35f).border(width = 2.dp, color = Color.DarkGray)) {
                AudioView(
                    audioConfig = config.systemAudioConfig,
                    onUpdate = systemViewModel::updateSystemAudioConfig
                )
            }
        }
        Box(
            modifier = Modifier
                .weight(0.20f)
                .fillMaxHeight()
                .border(width = 2.dp, color = Color.DarkGray)
        ) {
            KitChainView(
                kitChains = config.kitChains,
                kits = kits,
                onUpdate = systemViewModel::updateKitChains
            )
        }
    }
}