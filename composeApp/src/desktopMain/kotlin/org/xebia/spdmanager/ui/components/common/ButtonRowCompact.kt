package org.xebia.spdmanager.ui.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun <T : Enum<T>> ButtonRowCompact(
    label: String,
    selectedItem: T,
    items: Array<T>,
    onItemSelected: (T) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, fontSize = 14.sp, modifier = Modifier.padding(bottom = 4.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.fillMaxWidth()) {
            items.forEach { item ->
                Button(
                    onClick = { onItemSelected(item) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedItem == item) Color.Gray else Color.LightGray
                    ),
                    contentPadding = PaddingValues(0.dp), // Remove extra padding
                    shape = RectangleShape, // Make it a perfect square
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f) // Ensures a square shape
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(item.name, fontSize = 10.sp)
                    }
                }
            }
        }
    }
}
