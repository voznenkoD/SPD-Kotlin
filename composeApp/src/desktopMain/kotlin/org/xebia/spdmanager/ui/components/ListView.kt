package org.xebia.spdmanager.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.model.kit.Kit

@Composable
@Preview
fun ListView(kits: List<Kit>, onItemSelected: (Kit) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(kits) { kit ->
            ListItemView(kit, onItemSelected)
        }
    }
}