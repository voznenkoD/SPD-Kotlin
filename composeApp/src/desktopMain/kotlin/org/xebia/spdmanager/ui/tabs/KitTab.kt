package org.xebia.spdmanager.ui.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.ui.components.lists.ListView
import org.xebia.spdmanager.model.kit.Kit


@Composable
fun KitTab(kits: List<Kit>, onItemSelected: (Kit) -> Unit) {
    Box(Modifier.fillMaxSize()) {
        ListView(kits, onItemSelected)
    }
}