package org.xebia.spdmanager.ui.components.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.ui.theme.*

@Composable
fun <T> DropdownSelector(
    label: String,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    items: List<T>,
    content: @Composable (T) -> Unit = { item -> Text(item.toString(), style = Typography.body, color = ColorTextPrimary) },
    width: Dp = 150.dp
) {
    var expanded by remember { mutableStateOf(false) }

    Column(Modifier.width(width)) {
        Text(label, style = Typography.label, color = ColorTextSecondary, modifier = Modifier.padding(bottom = Spacing.s))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(Heights.control)
                .border(BorderStroke(1.dp, ColorDivider), shape = ShapeDefault)
                .background(ColorSurface, shape = ShapeDefault)
                .clickable { expanded = true }
                .padding(horizontal = Spacing.l, vertical = Spacing.s),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                content(selectedItem)
            }
            Text("▼", style = Typography.caption, color = ColorTextSecondary)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .border(BorderStroke(1.dp, ColorDivider))
                .background(ColorSurface)
                .width(width)
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
