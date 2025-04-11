package org.xebia.spdmanager.ui.waveform

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import org.jetbrains.skia.Paint
import org.jetbrains.skia.Font
import org.jetbrains.skia.TextLine
import org.xebia.spdmanager.audioplayer.WaveformData
import kotlin.math.*

@Composable
fun DisplayWaveformWithGrid(waveformData: WaveformData, width: Int = 800, height: Int = 160) {
    val samples = remember(waveformData.samples) { waveformData.samples }
    val sampleRate = remember(waveformData.sampleRate) { waveformData.sampleRate }

    val durationMs = remember(waveformData) { (samples.size.toDouble() / sampleRate * 1000).toInt() }
    val step = remember(waveformData) { samples.size / width.toFloat() }

    Canvas(modifier = Modifier.size(width.dp, height.dp).padding(bottom = 10.dp)) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val pixelsPerSample = canvasWidth / samples.size.toFloat()

        // Font and paint setup
        val skiaFont = Font(null, 24f) // Use a larger font size
        val skiaPaint = Paint().apply {
            color = 0xFF000000.toInt() // Black color for text
        }

        // Draw grid and labels into the canvas
        drawIntoCanvas { canvas ->
            val nativeCanvas = canvas.nativeCanvas

            // Choose time step as 1/10th of total duration
            val timeStepMs = (durationMs / 10f).roundToInt().coerceAtLeast(1)
            val verticalLines = durationMs / timeStepMs

            // X-axis: time grid labels at the bottom
            for (i in 0..verticalLines) {
                val ms = i * timeStepMs
                val x = ms / 1000f * sampleRate * pixelsPerSample

                // Vertical grid line
                drawLine(
                    color = Color.Gray,
                    start = Offset(x, 0f),
                    end = Offset(x, canvasHeight),
                    strokeWidth = 0.5f
                )

                // Only draw label if within reasonable bounds
                if (i % 2 == 0 && i!=verticalLines) {  // Reduce label density
                    // Create a TextLine for time label
                    val timeLabel = "$ms ms"
                    val textLine = TextLine.make(timeLabel, skiaFont)
                    nativeCanvas.drawTextLine(
                        textLine,
                        x + 10f,  // Padding for visibility
                        canvasHeight + 20f,  // Move labels below the grid
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
                drawLine(
                    color = Color.DarkGray,
                    start = Offset(0f, y),
                    end = Offset(canvasWidth, y),
                    strokeWidth = 0.5f
                )

//                // Create a TextLine for amplitude label (only draw at lower density)
//                if (i % 2 == 0) {  // Reduce label density on the Y-axis too
//                    val textLine = TextLine.make(label, skiaFont)
//                    nativeCanvas.drawTextLine(
//                        textLine,
//                        -20f,  // Move labels to the left of the grid
//                        y + 10f,  // Position the label a bit above the grid line
//                        skiaPaint
//                    )
//                }
            }
        }

        // Draw waveform (only when the waveform data changes)
        val midY = canvasHeight / 2f
        var lastX = 0f
        var lastY = midY
        for (xPixel in 0 until width) {
            val sampleIndex = (xPixel * step).toInt().coerceIn(0, samples.size - 1)
            val sample = samples[sampleIndex]
            val y = midY - sample * midY
            drawLine(
                color = Color.Black,
                start = Offset(lastX, lastY),
                end = Offset(xPixel.toFloat(), y),
                strokeWidth = 3f
            )
            lastX = xPixel.toFloat()
            lastY = y
        }
    }
}
