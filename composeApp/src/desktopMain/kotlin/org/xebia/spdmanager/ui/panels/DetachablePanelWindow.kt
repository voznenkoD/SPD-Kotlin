package org.xebia.spdmanager.ui.panels

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window

/**
 * Renders [content] in a pop-out window when its region is undocked and the user has
 * opened it (via the [PanelIconStrip]). Declared inside a screen composable so the
 * windowed content shares the screen's ViewModel/state. Closing the window only sets
 * [PanelEntry.windowOpen] = false; the region stays undocked (icon remains).
 */
@Composable
fun DetachablePanelWindow(
    entry: PanelEntry,
    region: PanelRegion,
    content: @Composable () -> Unit
) {
    if (!entry.docked && entry.windowOpen) {
        Window(
            onCloseRequest = { entry.windowOpen = false },
            title = regionTitle(region)
        ) {
            Box(Modifier.fillMaxSize()) {
                content()
            }
        }
    }
}