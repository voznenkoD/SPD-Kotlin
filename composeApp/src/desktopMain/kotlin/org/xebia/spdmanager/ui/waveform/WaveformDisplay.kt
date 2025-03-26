package org.xebia.spdmanager.ui.waveform

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.Image


@Composable
fun DisplayWaveform(waveformSamples: List<Float>, width: Int = 500, height: Int = 200) {
    val waveformImage = generateWaveformImage(waveformSamples, width, height)
    val imageBitmap = waveformImage.asImageBitmap()
    Image(bitmap = imageBitmap, contentDescription = "Waveform Image")
}