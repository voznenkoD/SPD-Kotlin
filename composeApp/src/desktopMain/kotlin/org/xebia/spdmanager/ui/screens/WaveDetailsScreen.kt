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
import org.xebia.spdmanager.ui.waveform.DisplayWaveform
import java.io.File

@Composable
fun WaveDetailsScreen(wave: Wave?, device: Device?) {
    val path by remember { mutableStateOf(device?.rootPath) }

    if (wave == null || path.isNullOrEmpty()) {
        Text("No wave selected", fontSize = 18.sp)
        return
    }

    val filePath = "$path/WAVE/DATA/${wave.path}"

    Column(modifier = Modifier
        .fillMaxSize().padding(8.dp)) {

        DisplayWaveform(waveformSamples = readWavFile(filePath))

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

        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
            Text(text = "Start:", fontSize = 16.sp, modifier = Modifier.weight(1f))
            Text(text = wave.start.toString(), fontSize = 16.sp, modifier = Modifier.weight(2f))

            Text(text = "End:", fontSize = 16.sp, modifier = Modifier.weight(1f))
            Text(text =  wave.end.toString(), fontSize = 16.sp, modifier = Modifier.weight(2f))

            Button(onClick = { SingleFilePlayer.play(File(filePath)) }) {
                Text("Play")
            }
            Button(onClick = { SingleFilePlayer.stop() }) {
                Text("Stop")
            }
        }
    }
}
