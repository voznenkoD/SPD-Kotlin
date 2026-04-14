package org.xebia.spdmanager.ui.components.kit

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.ui.theme.*
import org.xebia.spdmanager.model.kit.pad.PadNumber
import org.xebia.spdmanager.ui.components.common.DropdownSelector

@Composable
fun PadLinkSelector(
    padLink1: PadNumber,
    onPadLink1Selected: (PadNumber) -> Unit,
    padLink2: PadNumber,
    onPadLink2Selected: (PadNumber) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .weight(2f)
                .border(Spacing.xs, ColorDivider, ShapeCard)
                .padding(Spacing.m)
        ) {
            DropdownSelector(
                label = "Pad Link from :",
                selectedItem = padLink1,
                onItemSelected = onPadLink1Selected,
                items = PadNumber.entries
            )
        }

        Text("---->", style = Typography.title, color = ColorTextPrimary, modifier = Modifier.padding(horizontal = Spacing.xl))

        Box(
            modifier = Modifier
                .weight(2f)
                .border(Spacing.xs, ColorDivider, ShapeCard)
                .padding(Spacing.m)
        ) {
            DropdownSelector(
                label = "Pad Link to :",
                selectedItem = padLink2,
                onItemSelected = onPadLink2Selected,
                items = PadNumber.entries
            )
        }
    }
}
