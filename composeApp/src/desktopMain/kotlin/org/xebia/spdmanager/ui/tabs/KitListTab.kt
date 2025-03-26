package org.xebia.spdmanager.ui.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.model.kit.Kit
import org.xebia.spdmanager.ui.components.lists.GenericListView

@Composable
fun KitListTab(
    items: List<Kit>,
    onItemSelected: (Kit) -> Unit,
    content: @Composable (Kit) -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        GenericListView(items, onItemSelected, content)
    }
}