package org.xebia.spdmanager.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun ListView(onItemClicked: (String) -> Unit) {
    val items = List(20) { "Item #$it" } // Generate a list of 20 items

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(items) { item ->
            ListItemView(item, onItemClicked)
        }
    }
}