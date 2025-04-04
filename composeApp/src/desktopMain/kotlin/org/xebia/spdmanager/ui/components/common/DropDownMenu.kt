package org.xebia.spdmanager.ui.components.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp

@Composable
fun <T> DropdownSelector(
    label: String,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    items: List<T>,
    content: @Composable (T) -> Unit = { item -> Text(item.toString()) },
    width: Dp = 150.dp
) {
    var expanded by remember { mutableStateOf(false) }

    Column(Modifier.width(width)) {
        Text(label, fontSize = 14.sp, modifier = Modifier.padding(bottom = 4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, Color.DarkGray), shape = RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(4.dp))
                .clickable { expanded = true }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            // Render selected item using content
            content(selectedItem)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.border(BorderStroke(1.dp, Color.DarkGray)).width(width)
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { content(item) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

