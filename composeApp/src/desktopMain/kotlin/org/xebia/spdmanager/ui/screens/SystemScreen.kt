package org.xebia.spdmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.LocalDeviceManager

import org.xebia.spdmanager.ui.components.system.AudioView
import org.xebia.spdmanager.ui.components.system.ClickView
import org.xebia.spdmanager.ui.components.system.KitChainView
import org.xebia.spdmanager.ui.components.system.VisualControlView
import org.xebia.spdmanager.ui.components.system.masterfx.MasterEffectView

@Composable
fun SystemScreen() {
    val deviceManager = LocalDeviceManager.current
    val device = deviceManager.device
    var currentSystemConfig by remember { mutableStateOf(device!!.systemConfig)}
    var waves by remember {mutableStateOf(device!!.waves)}
    var kits by remember {mutableStateOf(device!!.kits)}

    Row(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column(
            modifier = Modifier
                .weight(0.25f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.weight(0.5f)) {
                ClickView(currentSystemConfig.clickConfig, waves)
            }
            Box(modifier = Modifier.weight(0.5f)) {
                VisualControlView(currentSystemConfig.visualControl, {})
            }
        }

        Column(modifier = Modifier.weight(0.5f)) {
            Box(modifier = Modifier.weight(0.67f)) {
                MasterEffectView(currentSystemConfig.masterEffectConfig, {})
            }
            Box(modifier = Modifier.weight(0.33f)) {
                AudioView(currentSystemConfig.systemAudioConfig, {})
            }
        }

        Box(
            modifier = Modifier
                .weight(0.25f)
                .fillMaxHeight()
        ) {
            KitChainView(currentSystemConfig.kitChains, kits, {})
        }
    }
}
