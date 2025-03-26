package org.xebia.spdmanager.ui.screens

import androidx.compose.runtime.*
import org.xebia.spdmanager.model.Wave

@Composable
fun WaveDetailsScreen(wave: Wave?) {
    var selectedWave by remember { mutableStateOf(wave) }
}