package org.xebia.spdmanager.ui.panels

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.ui.theme.*

/**
 * Narrow vertical strip on the far left listing one icon button per UNDOCKED region of
 * [screenId]. Clicking a button toggles that region's pop-out window open/closed.
 *
 * The caller renders this only when [PanelLayoutState.anyUndocked] is true.
 */
@Composable
fun PanelIconStrip(screenId: ScreenId) {
    Column(
        modifier = Modifier
            .width(40.dp)
            .fillMaxHeight()
            .background(ColorSurface)
            .border(width = 1.dp, color = ColorDivider)
            .padding(vertical = Spacing.m),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.m)
    ) {
        PanelRegion.entries.forEach { region ->
            val entry = PanelLayoutState.entry(screenId, region)
            if (!entry.docked) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(
                            if (entry.windowOpen) ColorSurfaceSelected else ColorSurface,
                            shape = ShapeDefault
                        )
                        .border(1.dp, ColorDivider, ShapeDefault)
                        .clickable { entry.windowOpen = !entry.windowOpen }
                        .padding(Spacing.s),
                    contentAlignment = Alignment.Center
                ) {
                    PanelEdgeIcon(region = region, modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}