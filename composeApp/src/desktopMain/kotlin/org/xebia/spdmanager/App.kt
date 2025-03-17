package org.xebia.spdmanager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.xebia.spdmanager.service.DeviceManager
import org.xebia.spdmanager.ui.screens.HomeScreen

@Composable
@Preview
fun App() {
    val deviceManager = remember { DeviceManager() }

    CompositionLocalProvider(LocalDeviceManager provides deviceManager) {
        HomeScreen()
    }
}