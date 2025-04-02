package org.xebia.spdmanager.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(0.15f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.weight(0.6f).border(width = 2.dp, color = Color.DarkGray)) {
                ClickView(currentSystemConfig.clickConfig, waves)
            }
            Box(modifier = Modifier.weight(0.4f).border(width = 2.dp, color = Color.DarkGray)) {
                VisualControlView(currentSystemConfig.visualControl, {})
            }
        }
        Column(modifier = Modifier.weight(0.65f)) {
            Row(modifier = Modifier.weight(0.65f).border(width = 2.dp, color = Color.DarkGray)) {
                MasterEffectView(currentSystemConfig.masterEffectConfig, {})
            }
            Row(modifier = Modifier.weight(0.35f).border(width = 2.dp, color = Color.DarkGray)) {
                AudioView(currentSystemConfig.systemAudioConfig, {})
            }
        }
        Box(
            modifier = Modifier
                .weight(0.20f)
                .fillMaxHeight().border(width = 2.dp, color = Color.DarkGray)
        ) {
            KitChainView(currentSystemConfig.kitChains, kits, {})
        }
    }
}
