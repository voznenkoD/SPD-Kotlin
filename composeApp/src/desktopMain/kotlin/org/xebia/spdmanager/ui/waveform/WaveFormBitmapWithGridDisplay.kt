package org.xebia.spdmanager.ui.waveform

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.audioplayer.WaveformData
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Color
import org.jetbrains.skia.Font
import org.jetbrains.skia.Paint as SkiaPaint
import org.jetbrains.skia.TextLine
import kotlin.math.roundToInt

@Composable
fun DisplayBitmapWaveformWithGrid(waveformData: WaveformData, width: Int = 800, height: Int = 160) {
    val samples = remember(waveformData.samples) { waveformData.samples }
    val sampleRate = remember(waveformData.sampleRate) { waveformData.sampleRate }

    val waveformImage = generateWaveformImageWithGrid(samples, sampleRate.toFloat(), width, height)

    val imageBitmap = waveformImage.asImageBitmap()

    Image(bitmap = imageBitmap, contentDescription = "Waveform with Grid", modifier = Modifier.size(width.dp, height.dp).padding(bottom = 10.dp))
}

fun generateWaveformImageWithGrid(samples: List<Float>, sampleRate: Float, width: Int, height: Int): Bitmap {
    val bitmap = Bitmap()

    bitmap.allocN32Pixels(width, height)

    val canvas = Canvas(bitmap)

    val canvasWidth = width.toFloat()
    val canvasHeight = height.toFloat()
    val pixelsPerSample = canvasWidth / samples.size.toFloat()

    val skiaFont = Font(null, 12f)
    val skiaPaint = SkiaPaint().apply {
        color = 0xFF000000.toInt()
    }

    val durationMs = (samples.size.toDouble() / sampleRate * 1000).toInt()
    val timeStepMs = (durationMs / 10f).roundToInt().coerceAtLeast(1)
    val verticalLines = durationMs / timeStepMs

    for (i in 0..verticalLines) {
        val ms = i * timeStepMs
        val x = ms / 1000f * sampleRate * pixelsPerSample

        canvas.drawLine(x, 0f, x, canvasHeight, SkiaPaint().apply {
            color = Color.BLACK
            strokeWidth = 0.5f
        })

        if (i % 2 == 0 && i != verticalLines) {
            val timeLabel = "$ms"
            val textLine = TextLine.make(timeLabel, skiaFont)

            val labelYPosition = canvasHeight - 5f

            canvas.drawTextLine(
                textLine,
                x + 10f,
                labelYPosition,
                skiaPaint
            )
        }
    }

    val ySteps = 10
    for (i in 0..ySteps) {
        val fraction = i / ySteps.toFloat()
        val y = canvasHeight * fraction
        val value = ((1f - fraction) * 2f - 1f).coerceIn(-1f, 1f)
        val label = "${(value * 100).roundToInt()}%"

        canvas.drawLine(0f, y, canvasWidth, y, SkiaPaint().apply {
            color = Color.BLACK
            strokeWidth = 0.5f
        })

        // Draw amplitude labels
//        val labelWidth = skiaFont.measureText(label)
//        canvas.drawTextLine(
//            TextLine.make(label, skiaFont),
//            -20f,  // Move labels to the left of the grid
//            y + 10f, // Slightly adjust y position to fit within bounds
//            skiaPaint
//        )
    }

    val midY = canvasHeight / 2f
    var lastX = 0f
    var lastY = midY
    val step = samples.size / width.toFloat()

    for (xPixel in 0 until width) {
        val sampleIndex = (xPixel * step).toInt().coerceIn(0, samples.size - 1)
        val sample = samples[sampleIndex]
        val y = midY - sample * midY

        canvas.drawLine(lastX, lastY, xPixel.toFloat(), y, SkiaPaint().apply {
            color = Color.BLACK
            strokeWidth = 3f
        })

        lastX = xPixel.toFloat()
        lastY = y
    }

    return bitmap
}
