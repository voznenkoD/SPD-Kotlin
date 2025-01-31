package org.xebia.spdmanager.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen() {
    var selectedItem by remember { mutableStateOf<String?>(null) }

    val onItemSelected: (String) -> Unit = { item ->
        selectedItem = item
    }

    Row {
        Column(Modifier.weight(0.3f).fillMaxHeight()) {
            Text("Hello")
        }
        Column(Modifier.weight(0.4f).fillMaxHeight()) {
            Text("Selected item: ${selectedItem}")
        }
        Column(Modifier.weight(0.3f).fillMaxHeight()) {
            TabsScreen(onItemSelected)
        }
    }
}
