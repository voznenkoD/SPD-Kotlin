package org.xebia.spdmanager

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.window.*
import java.io.File
import org.xebia.spdmanager.service.DeviceManager
import org.xebia.spdmanager.service.openFolderDialog
import org.xebia.spdmanager.ui.components.common.LoadErrorDialog
import org.xebia.spdmanager.ui.components.common.SaveAsDialog
import org.xebia.spdmanager.ui.panels.PanelIconStrip
import org.xebia.spdmanager.ui.panels.PanelLayoutState
import org.xebia.spdmanager.ui.panels.PanelRegion
import org.xebia.spdmanager.ui.panels.ScreenId
import org.xebia.spdmanager.ui.panels.regionTitle
import org.xebia.spdmanager.ui.screens.MainScreen
import org.xebia.spdmanager.ui.screens.SetupScreen
import org.xebia.spdmanager.ui.screens.SystemScreen
import org.xebia.spdmanager.ui.theme.BASE_WIDTH
import org.xebia.spdmanager.ui.theme.ColorBackground
import org.xebia.spdmanager.ui.theme.LocalScale

fun main() {
    // macOS shows this as the application menu name (next to the Apple logo) when run.
    System.setProperty("apple.awt.application.name", "SPD-SX")
    runApp()
}

private fun runApp() = application {
    val deviceManager = remember { DeviceManager() }

    CompositionLocalProvider(LocalDeviceManager provides deviceManager) {
        Window(title = "SPD Manager", onCloseRequest = ::exitApplication, state = rememberWindowState(placement = WindowPlacement.Maximized)) {
            var scaleFactor by remember { mutableFloatStateOf(1f) }
            // Non-null while the Save As dialog is open; holds the chosen destination parent folder.
            var saveAsParent by remember { mutableStateOf<String?>(null) }
            // True while a Save As copy is running; blocks other File actions so they can't write/read
            // the source folder concurrently with the copy.
            var saveAsInProgress by remember { mutableStateOf(false) }
            MenuBar {
                Menu("File", mnemonic = 'F') {
                    Item("Choose folder", enabled = !saveAsInProgress, onClick = {
                        openFolderDialog { folderPath ->
                            deviceManager.readDevice(folderPath)
                        }
                    })
                    Item("Save", enabled = !saveAsInProgress, onClick = {
                        deviceManager.saveDevice()
                    })
                    Item("Save As…", enabled = deviceManager.device != null && !saveAsInProgress, onClick = {
                        openFolderDialog { folderPath ->
                            saveAsParent = folderPath
                        }
                    })
                }
                Menu("View", mnemonic = 'V') {
                    val screenId = AppState.currentScreen.toScreenId()
                    PanelRegion.entries.forEach { region ->
                        CheckboxItem(
                            regionTitle(region),
                            checked = PanelLayoutState.entry(screenId, region).docked,
                            onCheckedChange = { PanelLayoutState.setDocked(screenId, region, it) }
                        )
                    }
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

                saveAsParent?.let { parent ->
                    SaveAsDialog(
                        parentPath = parent,
                        defaultName = File(deviceManager.device?.rootPath ?: "").name,
                        deviceManager = deviceManager,
                        onDismiss = { saveAsParent = null },
                        onCopyingChange = { saveAsInProgress = it }
                    )
                }

                deviceManager.loadError?.let { err ->
                    LoadErrorDialog(
                        fileName = err.fileName,
                        reason = err.reason,
                        onDismiss = { deviceManager.loadError = null }
                    )
                }
            }
        }
    }
}

@Composable
fun App(onMeasured: (Float) -> Unit = {}) {
    val focusManager = LocalFocusManager.current
    val screenId = AppState.currentScreen.toScreenId()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorBackground)
            .onGloballyPositioned { coords ->
                onMeasured(coords.size.width.toFloat())
            }
            .pointerInput(Unit) {
                detectTapGestures { focusManager.clearFocus() }
            }
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Far-left strip with icons for any undocked region (opens it as a pop-out window).
            if (PanelLayoutState.anyUndocked(screenId)) {
                PanelIconStrip(screenId)
            }
            Box(modifier = Modifier.weight(1f).fillMaxSize()) {
                when (AppState.currentScreen) {
                    Screen.Main -> MainScreen()
                    Screen.Setup -> SetupScreen()
                    Screen.System -> SystemScreen()
                }
            }
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

fun Screen.toScreenId(): ScreenId = when (this) {
    Screen.Main -> ScreenId.Main
    Screen.Setup -> ScreenId.Setup
    Screen.System -> ScreenId.System
}
