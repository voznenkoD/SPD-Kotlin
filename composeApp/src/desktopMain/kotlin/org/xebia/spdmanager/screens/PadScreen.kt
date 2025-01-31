package org.xebia.spdmanager.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PadScreen(selected: String?) {
    Surface(color = Color.Gray, modifier = Modifier.padding(16.dp)) {
        LazyVerticalGrid(columns = GridCells.Fixed(3)) {
            items(9) {
                Text("hello")
            }
        }
    }
}