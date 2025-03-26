package org.xebia.spdmanager

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.xebia.spdmanager.service.DeviceManager
import org.xebia.spdmanager.ui.screens.MainScreen
import org.xebia.spdmanager.ui.screens.SetupScreen
import org.xebia.spdmanager.ui.screens.SystemScreen


@Composable
@Preview
fun App() {
    val deviceManager = remember { DeviceManager() }
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Main) }

    CompositionLocalProvider(LocalDeviceManager provides deviceManager) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Display the current screen
            when (currentScreen) {
                Screen.Main -> MainScreen()
                Screen.Setup -> SetupScreen()
                Screen.System -> SystemScreen()
            }

            // Persistent Navigation Bar (Bottom Right)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .align(Alignment.BottomEnd), // Positioning to Bottom Right
                horizontalArrangement = Arrangement.End
            ) {
                NavigationButton("Main") { currentScreen = Screen.Main }
                Spacer(modifier = Modifier.width(8.dp))
                NavigationButton("Setup") { currentScreen = Screen.Setup }
                Spacer(modifier = Modifier.width(8.dp))
                NavigationButton("System") { currentScreen = Screen.System }
            }
        }
    }
}

// Reusable Navigation Button
@Composable
fun NavigationButton(label: String, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(label)
    }
}

// Screen States
sealed class Screen {
    object Main : Screen()
    object Setup : Screen()
    object System : Screen()
}

