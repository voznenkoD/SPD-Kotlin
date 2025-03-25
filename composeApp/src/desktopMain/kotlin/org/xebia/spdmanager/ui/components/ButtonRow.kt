package org.xebia.spdmanager.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun <T : Enum<T>> ButtonRow(
    label: String,
    selectedItem: T,
    items: Array<T>,
    onItemSelected: (T) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            items.forEach { item ->
                Button(
                    onClick = { onItemSelected(item) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedItem == item) Color.Gray else Color.LightGray
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(item.name, fontSize = 14.sp)
                }
            }
        }
    }
}

