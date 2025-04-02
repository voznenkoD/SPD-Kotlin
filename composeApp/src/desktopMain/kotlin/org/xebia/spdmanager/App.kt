package org.xebia.spdmanager

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.*
import org.xebia.spdmanager.service.DeviceManager
import org.xebia.spdmanager.service.openFolderDialog
import org.xebia.spdmanager.ui.screens.MainScreen
import org.xebia.spdmanager.ui.screens.SetupScreen
import org.xebia.spdmanager.ui.screens.SystemScreen

fun main() = application {
    val deviceManager = remember { DeviceManager() }

    CompositionLocalProvider(LocalDeviceManager provides deviceManager) {
        Window(title = "SPD Manager", onCloseRequest = ::exitApplication) {
            MenuBar {
                Menu("File", mnemonic = 'F') {
                    Item("Choose folder", onClick = {
                        openFolderDialog { folderPath ->
                            deviceManager.readDevice(folderPath)
                        }
                    })
                }
                Menu("View", mnemonic = 'V') {
                    Item("Main", onClick = { AppState.currentScreen = Screen.Main })
                    Item("Setup", onClick = { AppState.currentScreen = Screen.Setup })
                    Item("System", onClick = { AppState.currentScreen = Screen.System })
                }
            }
            App()
        }
    }
}

@Composable
fun App() {
    Box(modifier = Modifier.fillMaxSize()) {
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
