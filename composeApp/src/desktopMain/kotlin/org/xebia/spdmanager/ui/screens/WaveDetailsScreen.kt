package org.xebia.spdmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.ui.theme.*
import org.xebia.spdmanager.ui.theme.Typography as AppTypography
import org.xebia.spdmanager.audioplayer.SingleFilePlayer
import org.xebia.spdmanager.audioplayer.readWavFile
import org.xebia.spdmanager.model.Device
import org.xebia.spdmanager.model.Wave
import org.xebia.spdmanager.ui.components.common.GroupedDetailRow
import org.xebia.spdmanager.ui.waveform.DisplayWaveformWithGrid
import java.io.File
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun WaveDetailsScreen(wave: Wave?, device: Device?) {
    val path by remember { mutableStateOf(device?.rootPath) }
    val filePath = remember(wave, path) {
        if (wave != null && !path.isNullOrEmpty()) "$path/WAVE/DATA/${wave.path}" else ""
    }

    val waveformData = remember(wave, path) {
        if (wave != null && !path.isNullOrEmpty()) readWavFile(filePath) else null
    }

    if (wave == null || waveformData == null) {
        Box(modifier = Modifier.fillMaxSize().padding(Spacing.xl), contentAlignment = Alignment.Center) {
            Text("No wave selected", style = AppTypography.body, color = ColorTextSecondary)
        }
        return
    }

    var zoomLevel by remember { mutableStateOf(1.25f) }
    var progress by remember { mutableStateOf(0f) }
    var isPlaying by remember { mutableStateOf(false) }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (isPlaying) {
                val clip = SingleFilePlayer.getPlayingClip()
                if (clip == null || !clip.isRunning) {
                    progress = 0f
                    isPlaying = false
                    break
                }
                val currentProgress = clip.microsecondPosition.toFloat()
                val duration = clip.microsecondLength.toFloat().coerceAtLeast(1f)
                progress = currentProgress / duration
                kotlinx.coroutines.delay(10.milliseconds)
            }
        }
    }

    Row(modifier = Modifier.fillMaxWidth().padding(Spacing.xl)) {
        DisplayWaveformWithGrid(
            waveformData = waveformData,
            progress = progress,
            zoomLevel = zoomLevel
        )
    }

    Row {
        Column(modifier = Modifier.weight(0.85f).padding(Spacing.xl)) {
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
            Row(modifier = Modifier.fillMaxWidth()) {

                Text(
                    text = "Start:",
                    style = AppTypography.body,
                    color = ColorTextSecondary,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = wave.start.toString(),
                    style = AppTypography.body,
                    color = ColorTextPrimary,
                    modifier = Modifier.weight(2f)
                )

                Text(
                    text = "End:",
                    style = AppTypography.body,
                    color = ColorTextSecondary,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = wave.end.toString(),
                    style = AppTypography.body,
                    color = ColorTextPrimary,
                    modifier = Modifier.weight(2f)
                )
            }
        }

        Column(modifier = Modifier.weight(0.15f).padding(Spacing.xl)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { zoomLevel *= 1.25f },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ColorAccentOrange,
                        contentColor = ColorTextOnAccent
                    ),
                    shape = ShapeDefault
                ) { Text("+") }
                Button(
                    onClick = { zoomLevel /= 1.25f },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ColorAccentOrange,
                        contentColor = ColorTextOnAccent
                    ),
                    shape = ShapeDefault
                ) { Text("-") }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        if (isPlaying) {
                            SingleFilePlayer.stop()
                            isPlaying = false
                        } else {
                            SingleFilePlayer.play(File(filePath))
                            isPlaying = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ColorAccentOrange,
                        contentColor = ColorTextOnAccent
                    ),
                    shape = ShapeDefault
                ) {
                    Text(if (isPlaying) "Stop" else "Play")
                }
            }
        }
    }
}
