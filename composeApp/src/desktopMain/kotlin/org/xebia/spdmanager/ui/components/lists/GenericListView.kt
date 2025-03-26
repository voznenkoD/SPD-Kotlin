package org.xebia.spdmanager.ui.components.lists

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier

@Composable
fun <T> GenericListView(
    items: List<T>,
    onItemSelected: (T) -> Unit,
    content: @Composable (T) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items) { item ->
            GenericListItemView(item, onItemSelected, content)
        }
    }
}