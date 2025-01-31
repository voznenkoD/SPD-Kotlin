package org.xebia.spdmanager.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.ListView

@Composable
fun HomeScreen() {
    Row {
        Column(Modifier.weight(0.3f).fillMaxHeight()) {
            Text("Hello")
        }
        Column(Modifier.weight(0.4f).fillMaxHeight()) {
            Text("World")
        }
        Column(Modifier.weight(0.3f).fillMaxHeight()) {
            ListView()
        }
    }
}