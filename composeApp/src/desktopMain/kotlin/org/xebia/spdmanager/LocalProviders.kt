package org.xebia.spdmanager

import androidx.compose.runtime.staticCompositionLocalOf
import org.xebia.spdmanager.service.DeviceManager

val LocalDeviceManager = staticCompositionLocalOf<DeviceManager> {
    error("No DeviceManager provided")
}