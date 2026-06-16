package org.xebia.spdmanager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.window.*
import org.xebia.spdmanager.service.DeviceManager
import org.xebia.spdmanager.service.openFolderDialog
import org.xebia.spdmanager.ui.screens.MainScreen
import org.xebia.spdmanager.ui.screens.SetupScreen
import org.xebia.spdmanager.ui.screens.SystemScreen
import org.xebia.spdmanager.ui.theme.BASE_WIDTH
import org.xebia.spdmanager.ui.theme.ColorBackground
import org.xebia.spdmanager.ui.theme.LocalScale

fun main() = application {
    val deviceManager = remember { DeviceManager() }

    CompositionLocalProvider(LocalDeviceManager provides deviceManager) {
        Window(title = "SPD Manager", onCloseRequest = ::exitApplication) {
            var scaleFactor by remember { mutableFloatStateOf(1f) }
            MenuBar {
                Menu("File", mnemonic = 'F') {
                    Item("Choose folder", onClick = {
                        openFolderDialog { folderPath ->
                            deviceManager.readDevice(folderPath)
                        }
                    })
                    Item("Save", onClick = {
                        deviceManager.saveDevice()
                    })
                }
                Menu("View", mnemonic = 'V') {
                    // Reserved for later use.
                }
                Menu("Window", mnemonic = 'W') {
                    RadioButtonItem(
                        "Main",
                        selected = AppState.currentScreen == Screen.Main,
                        onClick = { AppState.currentScreen = Screen.Main }
                    )
                    RadioButtonItem(
                        "Setup",
                        selected = AppState.currentScreen == Screen.Setup,
                        onClick = { AppState.currentScreen = Screen.Setup }
                    )
                    RadioButtonItem(
                        "System",
                        selected = AppState.currentScreen == Screen.System,
                        onClick = { AppState.currentScreen = Screen.System }
                    )
                }
            }
            CompositionLocalProvider(LocalScale provides scaleFactor) {
                App(onMeasured = { widthPx -> scaleFactor = (widthPx / BASE_WIDTH).coerceIn(0.75f, 1.5f) })
            }
        }
    }
}

@Composable
fun App(onMeasured: (Float) -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorBackground)
            .onGloballyPositioned { coords ->
                onMeasured(coords.size.width.toFloat())
            }
    ) {
        when (AppState.currentScreen) {
            Screen.Main -> MainScreen()
            Screen.Setup -> SetupScreen()
            Screen.System -> SystemScreen()
        }
    }
}

object AppState {
    var currentScreen by mutableStateOf<Screen>(Screen.Main)
}

sealed class Screen {
    object Main : Screen()
    object Setup : Screen()
    object System : Screen()
}
