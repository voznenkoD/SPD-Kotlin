package org.xebia.spdmanager.ui.components.setup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.kit.pad.PadNumber

@Composable
fun PadsSetupScreen(
    selectedPadNumber: PadNumber,
    onSelect: (PadNumber) -> Unit
) {
    val pads = PadNumber.entries

    Surface(
        color = Color.Gray,
        modifier = Modifier.height(500.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.weight(0.75f)
            ) {
                items(
                    pads.filter { it in PadNumber.PAD_1..PadNumber.PAD_9 }
                        .sortedBy { it.value }
                ) {
                    PadItem(
                        padNumber = it,
                        isSelected = it == selectedPadNumber,
                        onSelect = onSelect
                    )
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.weight(0.25f)
            ) {
                items(
                    pads.filter { it in PadNumber.TRIG_1..PadNumber.TRIG_4 }
                        .sortedBy { it.value }
                ) {
                    PadItem(
                        padNumber = it,
                        isSelected = it == selectedPadNumber,
                        onSelect = onSelect
                    )
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(0.15f)
            ) {
                items(
                    pads.filter { it in PadNumber.FS_1..PadNumber.FS_2 }
                        .sortedBy { it.value }
                ) {
                    PadItem(
                        padNumber = it,
                        isSelected = it == selectedPadNumber,
                        onSelect = onSelect,
                        isFS = true
                    )
                }
            }
        }
    }
}

@Composable
fun PadItem(
    padNumber: PadNumber,
    isSelected: Boolean,
    onSelect: (PadNumber) -> Unit,
    isFS: Boolean = false
) {
    Surface(
        color = if (isSelected) Color.Yellow else Color.DarkGray,
        shape = RoundedCornerShape(8.dp),
        border = if (isSelected) BorderStroke(2.dp, Color.White) else null,
        modifier = Modifier
            .height(if (isFS) 50.dp else 100.dp)
            .then(if (isFS) Modifier.width(30.dp) else Modifier.width(20.dp))
            .padding(4.dp)
            .clickable { onSelect(padNumber) }
            .pointerHoverIcon(PointerIcon.Hand)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = padNumber.name.replace("_", " "),
                color = if (isSelected) Color.Black else Color.Yellow,
                fontSize = 20.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}