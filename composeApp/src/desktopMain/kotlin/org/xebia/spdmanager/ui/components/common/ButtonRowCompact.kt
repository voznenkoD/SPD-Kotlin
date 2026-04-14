package org.xebia.spdmanager.ui.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.ui.theme.*

@Composable
fun <T : Enum<T>> ButtonRowCompact(
    label: String,
    selectedItem: T,
    items: Array<T>,
    onItemSelected: (T) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, style = Typography.label, color = ColorTextSecondary, modifier = Modifier.padding(bottom = Spacing.m))

        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.s), modifier = Modifier.fillMaxWidth()) {
            items.forEach { item ->
                Button(
                    onClick = { onItemSelected(item) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedItem == item) ColorAccentOrange else ColorSurface,
                        contentColor = if (selectedItem == item) ColorTextOnAccent else ColorTextPrimary
                    ),
                    contentPadding = PaddingValues(0.dp),
                    shape = ShapeDefault,
                    modifier = Modifier
                        .weight(1f)
                        .height(Heights.button)
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(item.toString(), style = Typography.caption)
                    }
                }
            }
        }
    }
}
