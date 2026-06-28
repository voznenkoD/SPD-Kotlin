package org.xebia.spdmanager.ui.components.pad

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.awtTransferable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.ui.theme.*
import org.xebia.spdmanager.model.kit.Kit
import org.xebia.spdmanager.model.kit.pad.Pad
import org.xebia.spdmanager.model.kit.pad.PadNumber
import java.awt.datatransfer.DataFlavor
import java.io.File

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
    onUnregisterPadBounds: (PadNumber) -> Unit = {},
    onExternalWaveDrop: (PadNumber, Boolean, File) -> Unit = { _, _, _ -> }
) {
    if (kit != null) {
        val padEntries = kit.pads.entries.sortedBy { it.key.value }
        val mainPads = padEntries.filter { (key, _) -> key in PadNumber.PAD_1..PadNumber.PAD_9 }
        val trigPads = padEntries.filter { (key, _) -> key in PadNumber.TRIG_1..PadNumber.TRIG_4 }
        val fsPads = padEntries.filter { (key, _) -> key in PadNumber.FS_1..PadNumber.FS_2 }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.s)
                .background(ColorBackground),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            mainPads.chunked(3).forEach { rowPads ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    rowPads.forEach { (padNumber, pad) ->
                        Box(modifier = Modifier.weight(2f)) {
                            PadItem(pad, padNumber, onSelect,
                                isSelected = padNumber == selectedPadNumber, isMainSelected = isMainSelected,
                                onCopyPad = onCopyPad, onPastePad = onPastePad,
                                onRemoveWave = onRemoveWave, onRemoveSubWave = onRemoveSubWave,
                                hasCopiedPad = hasCopiedPad, waveNameLookup = waveNameLookup,
                                isDragActive = isDragActive, dragPosition = dragPosition,
                                onRegisterPadBounds = onRegisterPadBounds, onUnregisterPadBounds = onUnregisterPadBounds,
                            onExternalWaveDrop = onExternalWaveDrop)
                        }
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                trigPads.forEach { (padNumber, pad) ->
                    Box(modifier = Modifier.weight(2f)) {
                        PadItem(pad, padNumber, onSelect, isFS = true,
                            isSelected = padNumber == selectedPadNumber, isMainSelected = isMainSelected,
                            onCopyPad = onCopyPad, onPastePad = onPastePad,
                            onRemoveWave = onRemoveWave, onRemoveSubWave = onRemoveSubWave,
                            hasCopiedPad = hasCopiedPad, waveNameLookup = waveNameLookup,
                            isDragActive = isDragActive, dragPosition = dragPosition,
                            onRegisterPadBounds = onRegisterPadBounds, onUnregisterPadBounds = onUnregisterPadBounds,
                            onExternalWaveDrop = onExternalWaveDrop)
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth(0.8f)) {
                fsPads.forEach { (padNumber, pad) ->
                    Box(modifier = Modifier.weight(2f)) {
                        PadItem(pad, padNumber, onSelect, isFS = true,
                            isSelected = padNumber == selectedPadNumber, isMainSelected = isMainSelected,
                            onCopyPad = onCopyPad, onPastePad = onPastePad,
                            onRemoveWave = onRemoveWave, onRemoveSubWave = onRemoveSubWave,
                            hasCopiedPad = hasCopiedPad, waveNameLookup = waveNameLookup,
                            isDragActive = isDragActive, dragPosition = dragPosition,
                            onRegisterPadBounds = onRegisterPadBounds, onUnregisterPadBounds = onUnregisterPadBounds,
                            onExternalWaveDrop = onExternalWaveDrop)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
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
    onUnregisterPadBounds: (PadNumber) -> Unit = {},
    onExternalWaveDrop: (PadNumber, Boolean, File) -> Unit = { _, _, _ -> }
) {
    val backgroundColor = if (isSelected) {
        ColorSurfaceSelected
    } else {
        ColorSurface
    }

    var mainBounds by remember { mutableStateOf(Rect.Zero) }
    var subBounds by remember { mutableStateOf(Rect.Zero) }

    // Hover state for OS file drags (the framework routes external drops to the zone under the
    // cursor, so we track entry/exit per zone instead of hit-testing a position).
    var mainExternalHover by remember { mutableStateOf(false) }
    var subExternalHover by remember { mutableStateOf(false) }
    val currentOnExternalWaveDrop by rememberUpdatedState(onExternalWaveDrop)

    val mainHovered = (isDragActive && dragPosition != null && mainBounds != Rect.Zero && mainBounds.contains(dragPosition)) || mainExternalHover
    val subHovered = (isDragActive && dragPosition != null && subBounds != Rect.Zero && subBounds.contains(dragPosition)) || subExternalHover

    val mainDropTarget = remember(padNumber) {
        padWaveDropTarget(
            // Clear the sibling zone on enter so a stuck highlight can't linger when the drag moves
            // directly between the adjacent main/sub zones.
            onHoverChange = { hovering -> mainExternalHover = hovering; if (hovering) subExternalHover = false },
            onWavDropped = { file -> currentOnExternalWaveDrop(padNumber, true, file) }
        )
    }
    val subDropTarget = remember(padNumber) {
        padWaveDropTarget(
            onHoverChange = { hovering -> subExternalHover = hovering; if (hovering) mainExternalHover = false },
            onWavDropped = { file -> currentOnExternalWaveDrop(padNumber, false, file) }
        )
    }

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
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier
            .height(if (isFS) 120.dp else 185.dp)
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
                    .dragAndDropTarget(
                        shouldStartDragAndDrop = ::eventHasFileList,
                        target = mainDropTarget
                    )
                    .clickable { onSelect(padNumber, true) }
                    .then(if (mainHovered) Modifier.background(ColorAccentYellow.copy(alpha = 0.3f)) else Modifier)
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
                        color = when {
                            isSelected && isMainSelected -> ColorTextOnDark
                            isSelected -> ColorDivider
                            else -> ColorTextSecondary
                        },
                        text = waveNameLookup(pad.main.wave) ?: "----",
                        fontSize = Typography.bodySize,
                        fontWeight = if (isSelected && isMainSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }

            HorizontalDivider(
                color = if (isSelected) ColorAccentYellow else ColorAccentOrange,
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
                    .dragAndDropTarget(
                        shouldStartDragAndDrop = ::eventHasFileList,
                        target = subDropTarget
                    )
                    .clickable { onSelect(padNumber, false) }
                    .then(if (subHovered) Modifier.background(ColorAccentYellow.copy(alpha = 0.3f)) else Modifier)
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
                        color = when {
                            isSelected && !isMainSelected -> ColorTextOnDark
                            isSelected -> ColorDivider
                            else -> ColorTextSecondary
                        },
                        text = waveNameLookup(pad.sub.wave) ?: "----",
                        fontSize = Typography.bodySize,
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
                    color = if (isSelected) ColorAccentYellow else ColorTextSecondary,
                    fontSize = Typography.captionSize,
                    textAlign = TextAlign.End,
                    modifier = Modifier.padding(Spacing.m)
                )
            }
        }
    }
    }
}

// Gate on the FLAVOR only — during a drag the transferable's *data* is generally not readable on
// desktop (only at drop), so we must NOT call getTransferData here or shouldStartDragAndDrop would
// return false and onDrop would never fire. The actual .wav check happens at drop time.
@OptIn(ExperimentalComposeUiApi::class)
private fun eventHasFileList(event: DragAndDropEvent): Boolean =
    runCatching { event.awtTransferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor) }.getOrDefault(false)

// Extracts the first .wav among dropped files, guarded against any transferable access throwing
// (getTransferData may raise if the OS revokes/locks the file between accept and read).
@OptIn(ExperimentalComposeUiApi::class)
private fun firstWavFromEvent(event: DragAndDropEvent): File? = runCatching {
    val transferable = event.awtTransferable
    if (!transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) return@runCatching null
    @Suppress("UNCHECKED_CAST")
    val files = (transferable.getTransferData(DataFlavor.javaFileListFlavor) as? List<File>).orEmpty()
    files.firstOrNull { it.name.lowercase().endsWith(".wav") }
}.getOrNull()

private fun padWaveDropTarget(
    onHoverChange: (Boolean) -> Unit,
    onWavDropped: (File) -> Unit
): DragAndDropTarget = object : DragAndDropTarget {
    override fun onEntered(event: DragAndDropEvent) = onHoverChange(true)
    override fun onExited(event: DragAndDropEvent) = onHoverChange(false)
    override fun onEnded(event: DragAndDropEvent) = onHoverChange(false)
    override fun onDrop(event: DragAndDropEvent): Boolean {
        onHoverChange(false)
        val file = firstWavFromEvent(event) ?: return false
        onWavDropped(file)
        return true
    }
}