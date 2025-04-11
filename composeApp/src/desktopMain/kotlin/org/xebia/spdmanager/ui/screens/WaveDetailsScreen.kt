package org.xebia.spdmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var filePath = ""

    // Only load waveform data once when the wave or path changes
    val waveformData = remember(wave, path) {
        if (wave != null && !path.isNullOrEmpty()) {
            filePath = "$path/WAVE/DATA/${wave.path}"
            readWavFile("$path/WAVE/DATA/${wave.path}")
        } else {
            null
        }
    }

    if (wave == null || waveformData == null) {
        Text("No wave selected", fontSize = 18.sp)
        return
    }

    var zoomLevel by remember { mutableStateOf(1f) } // for zooming
    var progress by remember { mutableStateOf(0f) } // for tracking progress
    var isPlaying by remember { mutableStateOf(false) } // for play/pause state

    // LaunchedEffect to update progress based on audio playback
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (isPlaying) {
                val clip = SingleFilePlayer.getPlayingClip()  // Get current playing clip
                val currentProgress = clip?.microsecondPosition?.toFloat() ?: 0f
                val duration = clip?.microsecondLength?.toFloat() ?: 1f
                progress = currentProgress / duration
                kotlinx.coroutines.delay(100) // update progress every 100 ms
            }
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)) {

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
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
            Text(text = "Start:", fontSize = 16.sp, modifier = Modifier.weight(1f))
            Text(text = wave.start.toString(), fontSize = 16.sp, modifier = Modifier.weight(2f))

            Text(text = "End:", fontSize = 16.sp, modifier = Modifier.weight(1f))
            Text(text = wave.end.toString(), fontSize = 16.sp, modifier = Modifier.weight(2f))

            // Play button
            Button(onClick = {
                SingleFilePlayer.play(File(filePath))
                isPlaying = true
            }) {
                Text("Play")
            }

            // Stop button
            Button(onClick = {
                SingleFilePlayer.stop()
                isPlaying = false
            }) {
                Text("Stop")
            }

            Button(onClick = { zoomLevel *= 1.25f }) { Text("+") }
            Button(onClick = { zoomLevel /= 1.25f }) { Text("-") }

        }
    }
}

