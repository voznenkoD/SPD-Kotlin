package org.xebia.spdmanager.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WaveTab() {
    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Wave")
    }
}
