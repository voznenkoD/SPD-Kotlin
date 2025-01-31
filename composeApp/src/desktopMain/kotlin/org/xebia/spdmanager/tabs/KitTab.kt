package org.xebia.spdmanager.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.components.ListView
import org.xebia.spdmanager.model.Kit


@Composable
fun KitTab(kits: List<Kit>, onItemClicked: (Kit) -> Unit) {
    Box(Modifier.fillMaxSize()) {
        ListView(kits, onItemClicked)
    }
}