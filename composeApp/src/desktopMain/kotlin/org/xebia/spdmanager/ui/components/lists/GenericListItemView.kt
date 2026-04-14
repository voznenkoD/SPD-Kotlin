package org.xebia.spdmanager.ui.components.lists

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.ui.theme.*

@Composable
fun <T> GenericListItemView(
    item: T,
    onItemClicked: (T) -> Unit,
    backgroundColor: Color = ColorSurface,
    content: @Composable (T) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Spacing.s)
            .border(BorderStroke(0.5.dp, ColorDivider), shape = ShapeDefault)
            .clickable { onItemClicked(item) },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(Heights.listItem)
                .background(backgroundColor, shape = ShapeDefault)
                .padding(horizontal = Spacing.l),
            verticalAlignment = Alignment.CenterVertically
        ) {
            content(item)
        }
    }
}
