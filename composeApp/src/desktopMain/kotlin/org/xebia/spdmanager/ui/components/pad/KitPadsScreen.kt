package org.xebia.spdmanager.ui.components.pad

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.kit.Kit
import org.xebia.spdmanager.model.kit.pad.Pad
import org.xebia.spdmanager.model.kit.pad.PadNumber

@Composable
fun PadScreen(
    onSelect: (PadNumber, Boolean) -> Unit,
    kit: Kit?,
    selectedPadNumber: PadNumber? = null,
    isMainSelected: Boolean = true,
    onCopyPad: (PadNumber) -> Unit = {},
    onPastePad: (PadNumber) -> Unit = {},
    onRemoveWave: (PadNumber) -> Unit = {},
    onRemoveSubWave: (PadNumber) -> Unit = {},
    hasCopiedPad: Boolean = false,
    waveNameLookup: (Int) -> String? = { null },
    isDragActive: Boolean = false,
    dragPosition: Offset? = null,
    onRegisterPadBounds: (PadNumber, Rect, Rect) -> Unit = { _, _, _ -> },
    onUnregisterPadBounds: (PadNumber) -> Unit = {}
) {
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
                    ) { (padNumber, pad) ->
                        PadItem(
                            pad = pad,
                            padNumber = padNumber,
                            onSelect = onSelect,
                            isSelected = padNumber == selectedPadNumber,
                            isMainSelected = isMainSelected,
                            onCopyPad = onCopyPad,
                            onPastePad = onPastePad,
                            onRemoveWave = onRemoveWave,
                            onRemoveSubWave = onRemoveSubWave,
                            hasCopiedPad = hasCopiedPad,
                            waveNameLookup = waveNameLookup,
                            isDragActive = isDragActive,
                            dragPosition = dragPosition,
                            onRegisterPadBounds = onRegisterPadBounds,
                            onUnregisterPadBounds = onUnregisterPadBounds
                        )
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
                    ) { (padNumber, pad) ->
                        PadItem(
                            pad = pad,
                            padNumber = padNumber,
                            onSelect = onSelect,
                            isSelected = padNumber == selectedPadNumber,
                            isMainSelected = isMainSelected,
                            onCopyPad = onCopyPad,
                            onPastePad = onPastePad,
                            onRemoveWave = onRemoveWave,
                            onRemoveSubWave = onRemoveSubWave,
                            hasCopiedPad = hasCopiedPad,
                            waveNameLookup = waveNameLookup,
                            isDragActive = isDragActive,
                            dragPosition = dragPosition,
                            onRegisterPadBounds = onRegisterPadBounds,
                            onUnregisterPadBounds = onUnregisterPadBounds
                        )
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
                    ) { (padNumber, pad) ->
                        PadItem(
                            pad = pad,
                            padNumber = padNumber,
                            onSelect = onSelect,
                            isFS = true,
                            isSelected = padNumber == selectedPadNumber,
                            isMainSelected = isMainSelected,
                            onCopyPad = onCopyPad,
                            onPastePad = onPastePad,
                            onRemoveWave = onRemoveWave,
                            onRemoveSubWave = onRemoveSubWave,
                            hasCopiedPad = hasCopiedPad,
                            waveNameLookup = waveNameLookup,
                            isDragActive = isDragActive,
                            dragPosition = dragPosition,
                            onRegisterPadBounds = onRegisterPadBounds,
                            onUnregisterPadBounds = onUnregisterPadBounds
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PadItem(
    pad: Pad,
    padNumber: PadNumber,
    onSelect: (PadNumber, Boolean) -> Unit,
    isFS: Boolean = false,
    isSelected: Boolean = false,
    isMainSelected: Boolean = true,
    onCopyPad: (PadNumber) -> Unit = {},
    onPastePad: (PadNumber) -> Unit = {},
    onRemoveWave: (PadNumber) -> Unit = {},
    onRemoveSubWave: (PadNumber) -> Unit = {},
    hasCopiedPad: Boolean = false,
    waveNameLookup: (Int) -> String? = { null },
    isDragActive: Boolean = false,
    dragPosition: Offset? = null,
    onRegisterPadBounds: (PadNumber, Rect, Rect) -> Unit = { _, _, _ -> },
    onUnregisterPadBounds: (PadNumber) -> Unit = {}
) {
    val backgroundColor = if (isSelected) {
        Color(0x88B71C1C)
    } else {
        Color.DarkGray
    }

    var mainBounds by remember { mutableStateOf(Rect.Zero) }
    var subBounds by remember { mutableStateOf(Rect.Zero) }

    val mainHovered = isDragActive && dragPosition != null && mainBounds != Rect.Zero && mainBounds.contains(dragPosition)
    val subHovered = isDragActive && dragPosition != null && subBounds != Rect.Zero && subBounds.contains(dragPosition)

    DisposableEffect(padNumber) {
        onDispose { onUnregisterPadBounds(padNumber) }
    }

    ContextMenuArea(
        items = {
            buildList {
                add(ContextMenuItem("Copy Pad") { onCopyPad(padNumber) })
                if (hasCopiedPad) {
                    add(ContextMenuItem("Paste Pad") { onPastePad(padNumber) })
                }
                if (pad.main.wave != 0) {
                    add(ContextMenuItem("Remove Wave") { onRemoveWave(padNumber) })
                }
                if (pad.sub.wave != 0) {
                    add(ContextMenuItem("Remove SubWave") { onRemoveSubWave(padNumber) })
                }
            }
        }
    ) {
    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .height(if (isFS) 80.dp else 160.dp)
            .then(if (isFS) Modifier.width(180.dp) else Modifier)
            .padding(4.dp)
            .pointerHoverIcon(PointerIcon.Hand)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .onGloballyPositioned { coords ->
                        mainBounds = coords.boundsInWindow()
                        onRegisterPadBounds(padNumber, mainBounds, subBounds)
                    }
                    .clickable { onSelect(padNumber, true) }
                    .then(if (mainHovered) Modifier.background(Color(0x4400CC00)) else Modifier)
                    .then(
                        if (isSelected && isMainSelected) {
                            Modifier.padding(2.dp)
                        } else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        color = if (isSelected && isMainSelected) Color.White else Color.LightGray,
                        text = waveNameLookup(pad.main.wave) ?: "----",
                        fontSize = 14.sp,
                        fontWeight = if (isSelected && isMainSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }

            Divider(
                color = if (isSelected) Color.Yellow else Color.Red,
                thickness = 2.dp
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .onGloballyPositioned { coords ->
                        subBounds = coords.boundsInWindow()
                        onRegisterPadBounds(padNumber, mainBounds, subBounds)
                    }
                    .clickable { onSelect(padNumber, false) }
                    .then(if (subHovered) Modifier.background(Color(0x4400CC00)) else Modifier)
                    .then(
                        if (isSelected && !isMainSelected) {
                            Modifier.padding(2.dp)
                        } else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        color = if (isSelected && !isMainSelected) Color.White else Color.LightGray,
                        text = waveNameLookup(pad.sub.wave) ?: "----",
                        fontSize = 14.sp,
                        fontWeight = if (isSelected && !isMainSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Text(
                    text = padNumber.name.replace("_", " "),
                    color = if (isSelected) Color.Yellow else Color.LightGray,
                    fontSize = 12.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
    }
}