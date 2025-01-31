package org.xebia.spdmanager

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.xebia.spdmanager.service.XmlParser

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "SPD-Manager",
    ) {
        window.minimumSize = java.awt.Dimension(1200, 800)
        val parser = XmlParser()
        parser.parseAllFiles("src/desktopMain/resources/SPD-SX")
        App()
    }
}