package org.xebia.spdmanager.ui.components.lists

import androidx.compose.foundation.lazy.LazyListState

/**
 * Animate-scrolls [index] into view, but only when it isn't already among the currently visible
 * items — so an already-visible selection isn't needlessly yanked to the top. Shared by the kit and
 * wave lists, which all reveal their selected row this way.
 */
suspend fun LazyListState.scrollIntoView(index: Int) {
    if (layoutInfo.visibleItemsInfo.none { it.index == index }) {
        animateScrollToItem(index)
    }
}