package org.xebia.spdmanager.ui.waveform

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import org.xebia.spdmanager.ui.theme.ColorTextPrimary
import kotlin.math.roundToInt

@Composable
fun DisplayWaveformWithGrid(
    waveformData: WaveformData,
    progress: Float = 0f,
    zoomLevel: Float = 1f,
    height: Int = 120,
    baseWidth: Int = 800
) {
    val samples = remember(waveformData.samples) { waveformData.samples }
    val sampleRate = remember(waveformData.sampleRate) { waveformData.sampleRate }

    val durationMs = remember(waveformData) { (samples.size.toDouble() / sampleRate * 1000).toInt() }
    val displayWidth = (baseWidth * zoomLevel).toInt()
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .horizontalScroll(scrollState)
            .height((height + 40).dp) // extra space for labels
            .width(displayWidth.dp)
            .background(Color(0xFFEFEFEF)) // light grey background
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val midY = canvasHeight / 2f
            val skiaFont = Font(null, 20f)
            val skiaPaint = Paint().apply { color = 0xFF1A1A1A.toInt() }

            drawIntoCanvas { canvas ->
                val nativeCanvas = canvas.nativeCanvas

                // Vertical grid lines — 10 always visible in the viewport
                val viewportWidth = canvasWidth / zoomLevel
                val gridSpacingPx = viewportWidth / 10f
                val totalLines = (canvasWidth / gridSpacingPx).roundToInt()

                for (i in 0..totalLines) {
                    val x = i * gridSpacingPx
                    val ms = ((x / canvasWidth) * durationMs).roundToInt()

                    drawLine(
                        color = ColorTextPrimary,
                        start = Offset(x, 0f),
                        end = Offset(x, canvasHeight),
                        strokeWidth = 1f
                    )

                    if (i < totalLines) {
                        val timeLabel = "${ms}ms"
                        val textLine = TextLine.make(timeLabel, skiaFont)
                        nativeCanvas.drawTextLine(
                            textLine,
                            x + 4f,
                            canvasHeight - 5f,
                            skiaPaint
                        )
                    }
                }

                // Horizontal grid lines (amplitude)
                val ySteps = 4
                for (i in 0..ySteps) {
                    val y = i * canvasHeight / ySteps
                    drawLine(
                        color = Color(0xFFCCCCCC),
                        start = Offset(0f, y),
                        end = Offset(canvasWidth, y),
                        strokeWidth = 0.5f
                    )
                }
            }

            // Draw waveform
            val stepSize = samples.size / canvasWidth
            var lastX = 0f
            var lastY = midY
            for (xPixel in 0 until canvasWidth.toInt()) {
                val sampleIndex = (xPixel * stepSize).toInt().coerceIn(0, samples.size - 1)
                val sample = samples[sampleIndex]
                val y = midY - sample * midY
                drawLine(
                    color = ColorTextPrimary,
                    start = Offset(lastX, lastY),
                    end = Offset(xPixel.toFloat(), y),
                    strokeWidth = 1f
                )
                lastX = xPixel.toFloat()
                lastY = y
            }

            // Playback progress line
            if (progress in 0f..1f) {
                val progressX = canvasWidth * progress
                drawLine(
                    color = Color.Red,
                    start = Offset(progressX, 0f),
                    end = Offset(progressX, canvasHeight),
                    strokeWidth = 2f
                )
            }
        }
    }
}
