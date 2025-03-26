package org.xebia.spdmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.audioplayer.SingleFilePlayer
import org.xebia.spdmanager.audioplayer.readWavFile
import org.xebia.spdmanager.model.Wave
import org.xebia.spdmanager.ui.components.common.GroupedDetailRow
import org.xebia.spdmanager.ui.waveform.DisplayWaveform
import java.io.File

@Composable
fun WaveDetailsScreen(wave: Wave?, path: String?) {
    if (wave == null || path.isNullOrEmpty()) {
        Text("No wave selected", fontSize = 18.sp)
        return
    }

    val filePath = "$path/WAVE/DATA/${wave.path}"

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Wave Details", fontSize = 20.sp, style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        DisplayWaveform(waveformSamples = readWavFile(filePath))

        Spacer(modifier = Modifier.height(16.dp))

        GroupedDetailRow(
            label1 = "Number:", value1 = wave.number.toString(),
            label2 = "Name:", value2 = wave.name,
            label3 = "Path:", value3 = wave.path
        )

        Spacer(modifier = Modifier.height(16.dp))

        GroupedDetailRow(
            label1 = "Tempo:", value1 = wave.tempo.toString(),
            label2 = "Beat:", value2 = wave.beat.toString(),
            label3 = "Measure:", value3 = wave.measure.toString()
        )

        Spacer(modifier = Modifier.height(16.dp))

        GroupedDetailRow(
            label1 = "Start:", value1 = wave.start.toString(),
            label2 = "End:", value2 = wave.end.toString(),
            label3 = "", value3 = ""
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { SingleFilePlayer.play(File(filePath)) }) {
                Text("Play")
            }
            Button(onClick = { SingleFilePlayer.stop() }) {
                Text("Stop")
            }
        }
    }
}
