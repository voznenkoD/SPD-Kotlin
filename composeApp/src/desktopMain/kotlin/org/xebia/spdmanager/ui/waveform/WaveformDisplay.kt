package org.xebia.spdmanager.ui.waveform

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.Image

import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Color
import org.jetbrains.skia.Paint

@Composable
fun DisplayWaveform(waveformSamples: List<Float>, width: Int = 800, height: Int = 320) {
    val waveformImage = generateWaveformImage(waveformSamples, width, height)
    val imageBitmap = waveformImage.asImageBitmap()
    Image(bitmap = imageBitmap, contentDescription = "Waveform Image")
}

fun generateWaveformImage(waveformSamples: List<Float>, width: Int, height: Int): Bitmap {
    val bitmap = Bitmap()

    bitmap.allocN32Pixels(width, height)

    val canvas = Canvas(bitmap)

    val paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 2f
    }

    val midY = height / 2
    val sampleCount = waveformSamples.size
    val xStep = width.toFloat() / sampleCount

    for (i in waveformSamples.indices) {
        val x = (i * xStep).toInt()
        val y = (midY - (waveformSamples[i] * midY)).toInt()
        canvas.drawLine(x.toFloat(), midY.toFloat(), x.toFloat(), y.toFloat(), paint)
    }

    return bitmap
}
