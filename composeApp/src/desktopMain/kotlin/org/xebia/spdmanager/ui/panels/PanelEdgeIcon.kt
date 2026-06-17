package org.xebia.spdmanager.ui.panels

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.ui.theme.ColorAccentOrange
import org.xebia.spdmanager.ui.theme.ColorTextSecondary

/**
 * Icon for a detachable region: a thin square outline with a thick accent line on the
 * region's corresponding edge (Left/Right/Bottom).
 */
@Composable
fun PanelEdgeIcon(region: PanelRegion, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val outline = 1.5.dp.toPx()
        val thick = 4.dp.toPx()
        val w = size.width
        val h = size.height

        // Thin square outline.
        drawRect(
            color = ColorTextSecondary,
            topLeft = Offset(outline / 2f, outline / 2f),
            size = androidx.compose.ui.geometry.Size(w - outline, h - outline),
            style = Stroke(width = outline)
        )

        // Thick accent line on the region's edge.
        val inset = thick / 2f
        when (region) {
            PanelRegion.Left -> drawLine(
                color = ColorAccentOrange,
                start = Offset(inset, 0f),
                end = Offset(inset, h),
                strokeWidth = thick,
                cap = StrokeCap.Round
            )
            PanelRegion.Right -> drawLine(
                color = ColorAccentOrange,
                start = Offset(w - inset, 0f),
                end = Offset(w - inset, h),
                strokeWidth = thick,
                cap = StrokeCap.Round
            )
            PanelRegion.Bottom -> drawLine(
                color = ColorAccentOrange,
                start = Offset(0f, h - inset),
                end = Offset(w, h - inset),
                strokeWidth = thick,
                cap = StrokeCap.Round
            )
        }
    }
}