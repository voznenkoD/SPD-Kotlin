package org.xebia.spdmanager.ui.waveform

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
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
fun DisplayWaveformWithGrid(waveformData: WaveformData, width: Int = 800, height: Int = 160) {
    val samples = remember(waveformData.samples) { waveformData.samples }
    val sampleRate = remember(waveformData.sampleRate) { waveformData.sampleRate }

    // Generate the waveform image with grid
    val waveformImage = generateWaveformImageWithGrid(samples, sampleRate.toFloat(), width, height)

    // Convert Bitmap to ImageBitmap for display
    val imageBitmap = waveformImage.asImageBitmap()

    // Display the generated image
    Image(bitmap = imageBitmap, contentDescription = "Waveform with Grid", modifier = Modifier.size(width.dp, height.dp).padding(bottom = 10.dp))
}

fun generateWaveformImageWithGrid(samples: List<Float>, sampleRate: Float, width: Int, height: Int): Bitmap {
    val bitmap = Bitmap()

    // Create the bitmap with the provided width and height
    bitmap.allocN32Pixels(width, height)

    // Create the canvas to draw on the bitmap
    val canvas = Canvas(bitmap)

    val canvasWidth = width.toFloat()
    val canvasHeight = height.toFloat()
    val pixelsPerSample = canvasWidth / samples.size.toFloat()

    val skiaFont = Font(null, 12f)
    val skiaPaint = SkiaPaint().apply {
        color = 0xFF000000.toInt() // Black color for text
    }

    // Choose time step as 1/10th of total duration
    val durationMs = (samples.size.toDouble() / sampleRate * 1000).toInt()
    val timeStepMs = (durationMs / 10f).roundToInt().coerceAtLeast(1)
    val verticalLines = durationMs / timeStepMs

    // Draw grid and labels into the canvas
    for (i in 0..verticalLines) {
        val ms = i * timeStepMs
        val x = ms / 1000f * sampleRate * pixelsPerSample

        // Vertical grid line
        canvas.drawLine(x, 0f, x, canvasHeight, SkiaPaint().apply {
            color = Color.BLACK
            strokeWidth = 0.5f
        })

        // Draw time labels
        if (i % 2 == 0 && i != verticalLines) {
            val timeLabel = "$ms"
            val textLine = TextLine.make(timeLabel, skiaFont)

            // Ensure the label stays within the bounds of the canvas
            val labelYPosition = canvasHeight - 5f // Adjusted to position text within the canvas

            canvas.drawTextLine(
                textLine,
                x + 10f, // Padding for visibility
                labelYPosition, // Adjusted label position
                skiaPaint
            )
        }
    }

    // Y-axis: amplitude grid labels on the left side
    val ySteps = 10
    for (i in 0..ySteps) {
        val fraction = i / ySteps.toFloat()
        val y = canvasHeight * fraction
        val value = ((1f - fraction) * 2f - 1f).coerceIn(-1f, 1f)
        val label = "${(value * 100).roundToInt()}%"

        // Horizontal grid line
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

    // Draw waveform (the signal) onto the canvas
    val midY = canvasHeight / 2f
    var lastX = 0f
    var lastY = midY
    val step = samples.size / width.toFloat()

    for (xPixel in 0 until width) {
        val sampleIndex = (xPixel * step).toInt().coerceIn(0, samples.size - 1)
        val sample = samples[sampleIndex]
        val y = midY - sample * midY

        // Draw the waveform line
        canvas.drawLine(lastX, lastY, xPixel.toFloat(), y, SkiaPaint().apply {
            color = Color.BLACK
            strokeWidth = 1f
        })

        lastX = xPixel.toFloat()
        lastY = y
    }

    return bitmap
}
