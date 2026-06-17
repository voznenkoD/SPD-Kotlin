package org.xebia.spdmanager.ui.components.system

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.xebia.spdmanager.ui.theme.*
import org.xebia.spdmanager.ui.theme.Typography as AppTypography
import org.xebia.spdmanager.model.KitChain
import org.xebia.spdmanager.model.kit.Kit
import org.xebia.spdmanager.model.kit.Kit.Companion.formatKitNumber
import org.xebia.spdmanager.ui.components.common.DropdownSelector

// Negative kit-ref convention meaning "no kit assigned" to this chain slot.
private const val NO_KIT = -1

@Composable
fun KitChainView(
    kitChains: Map<Char, KitChain>,
    kits: List<Kit>,
    onMoveInChain: (chainKey: Char, from: Int, to: Int) -> Unit = { _, _, _ -> },
    onReplaceInChain: (chainKey: Char, index: Int, newRef: Int) -> Unit = { _, _, _ -> }
) {
    var selectedTab by remember { mutableStateOf(kitChains.keys.firstOrNull() ?: 'A') }

    Column(modifier = Modifier.fillMaxSize().padding(Spacing.xl)) {
        TabRow(
            selectedTabIndex = kitChains.keys.indexOf(selectedTab),
            containerColor = ColorSurface,
            contentColor = ColorTextPrimary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[kitChains.keys.indexOf(selectedTab)]),
                    color = ColorAccentOrange
                )
            }
        ) {
            kitChains.keys.forEach { key ->
                Tab(
                    selected = key == selectedTab,
                    onClick = { selectedTab = key },
                    text = { Text(key.toString(), fontWeight = FontWeight.Bold, color = if (key == selectedTab) ColorTextPrimary else ColorTextSecondary) }
                )
            }
        }

        kitChains[selectedTab]?.let { kitChain ->
            Text(
                text = kitChain.name,
                style = AppTypography.title,
                color = ColorTextPrimary,
                modifier = Modifier.padding(vertical = Spacing.xl)
            )

            Box(Modifier.fillMaxSize()) {
                KitChainList(
                    chainKey = selectedTab,
                    kitRefs = kitChain.kitRefs,
                    kits = kits,
                    onMove = onMoveInChain,
                    onReplace = onReplaceInChain
                )
            }
        }
    }
}

@Composable
private fun KitChainList(
    chainKey: Char,
    kitRefs: List<Int>,
    kits: List<Kit>,
    onMove: (chainKey: Char, from: Int, to: Int) -> Unit,
    onReplace: (chainKey: Char, index: Int, newRef: Int) -> Unit
) {
    val listState = rememberLazyListState()

    var draggedIndex by remember { mutableStateOf<Int?>(null) }
    var dragOffsetY by remember { mutableStateOf(0f) }
    var targetIndex by remember { mutableStateOf<Int?>(null) }

    // Dropdown options: "No Kit" first, then every kit by index.
    val kitOptions = remember(kits.size) { listOf(NO_KIT) + kits.indices.toList() }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(kitRefs) { index, refIndex ->
            val isDragged = draggedIndex == index
            val isDropTarget = targetIndex == index && draggedIndex != null && draggedIndex != index

            val currentOnMove by rememberUpdatedState(onMove)
            val currentOnReplace by rememberUpdatedState(onReplace)
            val currentChainKey by rememberUpdatedState(chainKey)
            val currentSize by rememberUpdatedState(kitRefs.size)

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
                    .pointerInput(kitRefs) {
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
                                    .coerceIn(0, currentSize - 1)
                                targetIndex = if (newTarget != index) newTarget else null
                            },
                            onDragEnd = {
                                val from = draggedIndex
                                val to = targetIndex
                                if (from != null && to != null) {
                                    currentOnMove(currentChainKey, from, to)
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
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            when {
                                isDragged -> ColorSurfaceHover
                                isDropTarget -> ColorAccentYellow
                                else -> ColorSurface
                            }
                        )
                        .padding(Spacing.xl),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Position number doubles as the drag handle (long-press to reorder).
                    Text(
                        "%02d".format(index + 1),
                        style = AppTypography.mono,
                        color = ColorTextPrimary
                    )
                    Spacer(Modifier.width(Spacing.l))
                    DropdownSelector(
                        selectedItem = refIndex,
                        onItemSelected = { newRef -> currentOnReplace(currentChainKey, index, newRef) },
                        items = kitOptions,
                        width = null,
                        modifier = Modifier.weight(1f),
                        content = { ref ->
                            val k = kits.getOrNull(ref)
                            Text(
                                if (k != null) "${formatKitNumber(ref)}  ${k.name} ${k.subName}" else "No Kit",
                                style = AppTypography.mono,
                                color = ColorTextPrimary,
                                maxLines = 1
                            )
                        }
                    )
                }
            }
        }
    }
}