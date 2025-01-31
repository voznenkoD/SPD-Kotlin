package org.xebia.spdmanager.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.model.Kit
import org.xebia.spdmanager.model.Pad

@Composable
fun PadScreen(onSelect: (Pad) -> Unit, kit: Kit?) {
    if (kit != null) {
        Surface(
            color = Color.Gray,
            modifier = Modifier.padding(16.dp)
        ) {
            LazyVerticalGrid(columns = GridCells.Fixed(3)) {

                kit.pads.entries.map { (key, pad) ->
                    item {
                        Column(
                            modifier = Modifier.padding(top = 20.dp, bottom = 20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = key.uppercase())
                            Surface(
                                color = Color.DarkGray,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .height(120.dp)
                                    .padding(8.dp)
                                    .clickable(onClick = { onSelect(pad) })
                                    .pointerHoverIcon(PointerIcon.Hand),
                            ) {
                                Column(
                                    modifier = Modifier.padding(4.dp).fillMaxHeight(),
                                    verticalArrangement = Arrangement.SpaceAround,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(color = Color.White, text = "Wave ${pad.mainSound.waveRef}")
                                    HorizontalDivider(color = Color.Black, thickness = 1.dp)
                                    Text(color = Color.White, text = "Wave ${pad.subSound.waveRef}")
                                }
                            }
                        }
                    }
                }

            }

        }
    }
}