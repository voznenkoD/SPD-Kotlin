package org.xebia.spdmanager.ui.components.lists

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.xebia.spdmanager.model.kit.Kit
import org.xebia.spdmanager.ui.theme.*

@Composable
fun KitListView(
    kits: List<Kit>,
    onKitSelected: (Kit) -> Unit,
    onCopyKit: (Kit) -> Unit,
    onPasteKit: (Kit) -> Unit,
    hasCopiedKit: Boolean,
    onMoveKit: (fromIndex: Int, toIndex: Int) -> Unit = { _, _ -> },
    selectedKitIndex: Int? = null
) {
    val listState = rememberLazyListState()

    var draggedIndex by remember { mutableStateOf<Int?>(null) }
    var dragOffsetY by remember { mutableStateOf(0f) }
    var targetIndex by remember { mutableStateOf<Int?>(null) }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(kits) { index, kit ->
            val isDragged = draggedIndex == index
            val isDropTarget = targetIndex == index && draggedIndex != null && draggedIndex != index
            val isSelected = index == selectedKitIndex

            val currentOnMoveKit by rememberUpdatedState(onMoveKit)
            val currentKitsSize by rememberUpdatedState(kits.size)

            ContextMenuArea(
                items = {
                    buildList {
                        add(ContextMenuItem("Copy") { onCopyKit(kit) })
                        if (hasCopiedKit) {
                            add(ContextMenuItem("Paste") { onPasteKit(kit) })
                        }
                    }
                }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                        .then(
                            if (isDragged) {
                                Modifier
                                    .zIndex(1f)
                                    .graphicsLayer { translationY = dragOffsetY }
                            } else Modifier
                        )
                        .border(
                            BorderStroke(
                                if (isDropTarget) 2.dp else 1.dp,
                                if (isDropTarget) ColorAccentOrange else ColorDivider
                            ),
                            shape = ShapeCard
                        )
                        .pointerInput(kits) {
                            detectDragGesturesAfterLongPress(
                                onDragStart = {
                                    draggedIndex = index
                                    dragOffsetY = 0f
                                    targetIndex = null
                                },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    dragOffsetY += dragAmount.y

                                    val itemHeight = size.height + 4.dp.toPx()
                                    val rawOffset = dragOffsetY / itemHeight
                                    val newTarget = (index + rawOffset.toInt())
                                        .coerceIn(0, currentKitsSize - 1)
                                    targetIndex = if (newTarget != index) newTarget else null
                                },
                                onDragEnd = {
                                    val from = draggedIndex
                                    val to = targetIndex
                                    if (from != null && to != null) {
                                        currentOnMoveKit(from, to)
                                    }
                                    draggedIndex = null
                                    dragOffsetY = 0f
                                    targetIndex = null
                                },
                                onDragCancel = {
                                    draggedIndex = null
                                    dragOffsetY = 0f
                                    targetIndex = null
                                }
                            )
                        }
                        .clickable { onKitSelected(kit) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                when {
                                    isDragged -> ColorSurfaceHover
                                    isDropTarget -> ColorAccentYellow
                                    isSelected -> ColorSurfaceSelected
                                    else -> ColorSurface
                                }
                            )
                            .padding(Spacing.xl),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = kit.name,
                            fontSize = Typography.bodySize,
                            color = if (isSelected) ColorTextOnDark else ColorTextPrimary
                        )
                    }
                }
            }
        }
    }
}