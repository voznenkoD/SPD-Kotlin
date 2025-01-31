package org.xebia.spdmanager

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ListItemView(item: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = 4.dp
    ) {
        Surface(modifier = Modifier.padding(16.dp)) {
            Text(text = item, fontSize = 18.sp)
        }
    }
}