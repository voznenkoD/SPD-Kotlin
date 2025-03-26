package org.xebia.spdmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.Wave
import org.xebia.spdmanager.ui.waveform.DisplayWaveform

@Composable
fun WaveDetailsScreen(wave: Wave?) {
    if (wave == null) {
        Text("No wave selected", fontSize = 18.sp)
        return
    }

    Spacer(modifier = Modifier.height(16.dp))

    DisplayWaveform(waveformSamples = List(500) { Math.sin(it.toDouble()).toFloat() }) // Mock data for testing

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Wave Details", fontSize = 20.sp, style = MaterialTheme.typography.headlineMedium)

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
    }
}

@Composable
fun GroupedDetailRow(label1: String, value1: String, label2: String, value2: String, label3: String, value3: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(text = label1, fontSize = 16.sp, modifier = Modifier.weight(1f))
        Text(text = value1, fontSize = 16.sp, modifier = Modifier.weight(2f))

        Text(text = label2, fontSize = 16.sp, modifier = Modifier.weight(1f))
        Text(text = value2, fontSize = 16.sp, modifier = Modifier.weight(2f))

        if (label3.isNotEmpty()) {
            Text(text = label3, fontSize = 16.sp, modifier = Modifier.weight(1f))
            Text(text = value3, fontSize = 16.sp, modifier = Modifier.weight(2f))
        }
    }
}
