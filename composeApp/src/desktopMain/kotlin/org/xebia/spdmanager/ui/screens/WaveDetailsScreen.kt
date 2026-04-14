package org.xebia.spdmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.ui.theme.*
import org.xebia.spdmanager.ui.theme.Typography as AppTypography
import org.xebia.spdmanager.audioplayer.SingleFilePlayer
import org.xebia.spdmanager.audioplayer.readWavFile
import org.xebia.spdmanager.model.Device
import org.xebia.spdmanager.model.Wave
import org.xebia.spdmanager.ui.components.common.GroupedDetailRow
import org.xebia.spdmanager.ui.waveform.DisplayBitmapWaveformWithGrid
import org.xebia.spdmanager.ui.waveform.DisplayWaveformWithGrid
import java.io.File

@Composable
fun WaveDetailsScreen(wave: Wave?, device: Device?) {
    val path by remember { mutableStateOf(device?.rootPath) }
    val filePath = remember(wave, path) {
        if (wave != null && !path.isNullOrEmpty()) {
            "$path/WAVE/DATA/${wave.path}"
        } else {
            ""
        }
    }

    val waveformData = remember(wave, path) {
        if (wave != null && !path.isNullOrEmpty()) {
            readWavFile(filePath)
        } else {
            null
        }
    }

    if (wave == null || waveformData == null) {
        Column(modifier = Modifier.fillMaxSize().padding(Spacing.xl)) {
            Text("No wave selected", style = AppTypography.title, color = ColorTextSecondary)
        }
        return
    }

    var zoomLevel by remember { mutableStateOf(1f) } // for zooming
    var progress by remember { mutableStateOf(0f) } // for tracking progress
    var isPlaying by remember { mutableStateOf(false) } // for play/pause state

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (isPlaying) {
                val clip = SingleFilePlayer.getPlayingClip()
                val currentProgress = clip?.microsecondPosition?.toFloat() ?: 0f
                val duration = clip?.microsecondLength?.toFloat() ?: 1f
                progress = currentProgress / duration
                kotlinx.coroutines.delay(100) // update progress every 100 ms
            }
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(Spacing.xl)) {

        DisplayWaveformWithGrid(
            waveformData = waveformData,
            progress = progress,
            zoomLevel = zoomLevel
        )

        // Wave details
        GroupedDetailRow(
            label1 = "Number:", value1 = wave.number.toString(),
            label2 = "Name:", value2 = wave.name,
            label3 = "Path:", value3 = wave.path
        )

        GroupedDetailRow(
            label1 = "Tempo:", value1 = wave.tempo.toString(),
            label2 = "Beat:", value2 = wave.beat.toString(),
            label3 = "Measure:", value3 = wave.measure.toString()
        )

        // Row with play, stop, and zoom controls
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = Spacing.m)) {
            Text(text = "Start:", style = AppTypography.body, color = ColorTextSecondary, modifier = Modifier.weight(1f))
            Text(text = wave.start.toString(), style = AppTypography.body, color = ColorTextPrimary, modifier = Modifier.weight(2f))

            Text(text = "End:", style = AppTypography.body, color = ColorTextSecondary, modifier = Modifier.weight(1f))
            Text(text = wave.end.toString(), style = AppTypography.body, color = ColorTextPrimary, modifier = Modifier.weight(2f))

            // Play button
            Button(
                onClick = {
                    SingleFilePlayer.play(File(filePath))
                    isPlaying = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = ColorAccentOrange, contentColor = ColorTextOnAccent),
                shape = ShapeDefault
            ) {
                Text("Play")
            }

            // Stop button
            Button(
                onClick = {
                    SingleFilePlayer.stop()
                    isPlaying = false
                },
                colors = ButtonDefaults.buttonColors(containerColor = ColorAccentOrange, contentColor = ColorTextOnAccent),
                shape = ShapeDefault
            ) {
                Text("Stop")
            }

            Button(onClick = { zoomLevel *= 1.25f }, colors = ButtonDefaults.buttonColors(containerColor = ColorAccentOrange, contentColor = ColorTextOnAccent), shape = ShapeDefault) { Text("+") }
            Button(onClick = { zoomLevel /= 1.25f }, colors = ButtonDefaults.buttonColors(containerColor = ColorAccentOrange, contentColor = ColorTextOnAccent), shape = ShapeDefault) { Text("-") }

        }
    }
}

