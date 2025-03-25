package org.xebia.spdmanager.ui.components.kit

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            .padding(vertical = 8.dp)
    ) {
        Text("Pad Link:", fontSize = 16.sp, modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .weight(2f)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(4.dp)
        ) {
            DropdownSelector(
                label = "From",
                selectedItem = padLink1,
                onItemSelected = onPadLink1Selected,
                items = PadNumber.entries
            )
        }

        Text("-->", fontSize = 20.sp, modifier = Modifier.padding(horizontal = 8.dp))

        Box(
            modifier = Modifier
                .weight(2f)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(4.dp)
        ) {
            DropdownSelector(
                label = "To",
                selectedItem = padLink2,
                onItemSelected = onPadLink2Selected,
                items = PadNumber.entries
            )
        }
    }
}
