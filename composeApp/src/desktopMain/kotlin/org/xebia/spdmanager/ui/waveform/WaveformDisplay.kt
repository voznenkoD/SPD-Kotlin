package org.xebia.spdmanager.ui.waveform

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.Image

@Composable
fun DisplayWaveform(waveformSamples: List<Float>, width: Int = 800, height: Int = 320) {
    val waveformImage = generateWaveformImage(waveformSamples, width, height)
    val imageBitmap = waveformImage.asImageBitmap()
    Image(bitmap = imageBitmap, contentDescription = "Waveform Image")
}