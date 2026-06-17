package org.xebia.spdmanager.ui.panels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/** The three screens that own detachable panels. */
enum class ScreenId { Main, Setup, System }

/** A detachable region on a screen. */
enum class PanelRegion { Left, Right, Bottom }

/**
 * Per (screen, region) panel state.
 * - [docked]: true → rendered in the main layout; false → removed from the layout
 *   and offered as an icon in the [PanelIconStrip].
 * - [windowOpen]: only meaningful while undocked — true → the region is shown in a
 *   pop-out window. Closing the window sets this false but leaves the region undocked.
 */
class PanelEntry {
    var docked by mutableStateOf(true)
    var windowOpen by mutableStateOf(false)
}

/** In-memory panel layout state (resets to all-docked on app restart). */
object PanelLayoutState {
    // Eagerly populated so entry() is a pure read (no state mutation during composition).
    // Reactivity lives in each PanelEntry's mutableStateOf fields, so a plain map suffices.
    private val entries: Map<Pair<ScreenId, PanelRegion>, PanelEntry> = buildMap {
        for (screen in ScreenId.entries) {
            for (region in PanelRegion.entries) {
                put(screen to region, PanelEntry())
            }
        }
    }

    fun entry(screen: ScreenId, region: PanelRegion): PanelEntry =
        entries.getValue(screen to region)

    fun anyUndocked(screen: ScreenId): Boolean =
        PanelRegion.entries.any { !entry(screen, it).docked }

    /** Re-docking also closes any open pop-out window for that region. */
    fun setDocked(screen: ScreenId, region: PanelRegion, docked: Boolean) {
        val e = entry(screen, region)
        e.docked = docked
        if (docked) e.windowOpen = false
    }
}

fun regionTitle(region: PanelRegion): String = when (region) {
    PanelRegion.Left -> "Left Panel"
    PanelRegion.Right -> "Right Panel"
    PanelRegion.Bottom -> "Bottom Panel"
}