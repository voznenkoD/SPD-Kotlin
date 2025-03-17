package org.xebia.spdmanager

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "SPD-Manager",
    ) {
        window.minimumSize = java.awt.Dimension(1200, 800)
        App()
    }
}