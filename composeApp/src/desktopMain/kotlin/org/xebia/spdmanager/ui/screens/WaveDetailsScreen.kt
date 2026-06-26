package org.xebia.spdmanager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.ui.theme.*
import org.xebia.spdmanager.ui.theme.Typography as AppTypography
import org.xebia.spdmanager.audioplayer.SingleFilePlayer
import org.xebia.spdmanager.audioplayer.readWavFile
import org.xebia.spdmanager.model.Device
import org.xebia.spdmanager.model.Wave
import org.xebia.spdmanager.ui.components.wave.WaveParamKnobs
import org.xebia.spdmanager.ui.waveform.DisplayWaveformWithGrid
import java.io.File
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun WaveDetailsScreen(wave: Wave?, device: Device?, onWaveChange: (Wave) -> Unit = {}) {
    val path by remember { mutableStateOf(device?.rootPath) }
    // Key only on the audio file (wave.path), not the whole Wave: editing tempo/beat/measure/start/end
    // produces a new Wave copy but the same underlying file, so the WAV must NOT be re-decoded per edit.
    val filePath = remember(wave?.path, path) {
        if (wave != null && !path.isNullOrEmpty()) "$path/WAVE/DATA/${wave.path}" else ""
    }

    val waveformData = remember(filePath) {
        if (filePath.isNotEmpty()) readWavFile(filePath) else null
    }

    if (wave == null || waveformData == null) {
        Box(modifier = Modifier.fillMaxSize().padding(Spacing.xl), contentAlignment = Alignment.Center) {
            Text("No wave selected", style = AppTypography.body, color = ColorTextSecondary)
        }
        return
    }

    val totalSamples = waveformData.samples.size

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

    Column(modifier = Modifier.fillMaxWidth().padding(Spacing.xl)) {
        // Waveform with playhead
        Row(modifier = Modifier.fillMaxWidth()) {
            DisplayWaveformWithGrid(
                waveformData = waveformData,
                progress = progress,
                zoomLevel = zoomLevel
            )
        }

        Spacer(Modifier.height(Spacing.l))

        // Read-only identity below the waveform — compact, left-aligned
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.xxl),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ReadOnlyParam("Number", wave.number.toString())
            ReadOnlyParam("Name", wave.name)
            ReadOnlyParam("Path", wave.path, modifier = Modifier.weight(1f, fill = false))
        }

        Spacer(Modifier.height(Spacing.l))

        // Lower area split into left (editable knobs) and right (playback controls) zones,
        // separated by a vertical grey divider line.
        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            WaveParamKnobs(
                wave = wave,
                totalSamples = totalSamples,
                onWaveChange = onWaveChange,
                modifier = Modifier.weight(1f)
            )

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(ColorDivider)
            )

            Column(
                modifier = Modifier.padding(start = Spacing.xl),
                verticalArrangement = Arrangement.spacedBy(Spacing.m)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.m)) {
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

@Composable
private fun ReadOnlyParam(label: String, value: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Spacing.s),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "$label:", style = AppTypography.caption, color = ColorTextSecondary)
        Text(
            text = value,
            style = AppTypography.body,
            color = ColorTextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
