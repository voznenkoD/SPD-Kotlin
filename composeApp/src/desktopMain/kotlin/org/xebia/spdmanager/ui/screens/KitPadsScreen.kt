package org.xebia.spdmanager.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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
import org.xebia.spdmanager.model.kit.Kit
import org.xebia.spdmanager.model.kit.pad.Pad
import org.xebia.spdmanager.model.kit.pad.PadNumber

@Composable
fun PadScreen(onSelect: (Pad) -> Unit, kit: Kit?) {
    if (kit != null) {
        Surface(
            color = Color.Gray,
            modifier = Modifier
                .padding(8.dp)
                .height(800.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.weight(0.75f)
                ) {
                    items(
                        kit.pads.entries
                            .filter { (key, _) -> key in PadNumber.PAD_1..PadNumber.PAD_9 }
                            .sortedBy { it.key.value }
                    ) { (key, pad) ->
                        PadItem(pad, key, onSelect)
                    }
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.weight(0.25f)
                ) {
                    items(
                        kit.pads.entries
                            .filter { (key, _) -> key in PadNumber.TRIG_1..PadNumber.TRIG_4 }
                            .sortedBy { it.key.value }
                    ) { (key, pad) ->
                        PadItem(pad, key, onSelect)
                    }
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.weight(0.15f)
                ) {
                    items(
                        kit.pads.entries
                            .filter { (key, _) -> key in PadNumber.FS_1..PadNumber.FS_2 }
                            .sortedBy { it.key.value }
                    ) { (key, pad) ->
                        PadItem(pad, key, onSelect, isFS = true)
                    }
                }
            }
        }
    }
}

@Composable
fun PadItem(pad: Pad, label: PadNumber, onSelect: (Pad) -> Unit, isFS: Boolean = false) {
    Surface(
        color = Color.DarkGray,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .height(if (isFS) 80.dp else 160.dp)
            .then(if (isFS) Modifier.width(180.dp) else Modifier)
            .padding(4.dp)
            .clickable { onSelect(pad) }
            .pointerHoverIcon(PointerIcon.Hand)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(color = Color.White, text = "Wave ${pad.main.wave}", fontSize = 14.sp)
                HorizontalDivider(color = Color.Red, thickness = 1.dp)
                Text(color = Color.White, text = "Wave ${pad.sub.wave}", fontSize = 14.sp)
            }
            Text(
                text = label.name.replace("_", " "),
                color = Color.Yellow,
                fontSize = 12.sp,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .padding(4.dp)
            )
        }
    }
}
