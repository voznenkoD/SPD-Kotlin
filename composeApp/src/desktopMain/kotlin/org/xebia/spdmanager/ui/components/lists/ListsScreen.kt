package org.xebia.spdmanager.ui.components.lists

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.model.kit.Kit
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.list.Category
import org.xebia.spdmanager.model.list.ListedWave
import org.xebia.spdmanager.model.list.WaveListsHolder
import org.xebia.spdmanager.viewmodel.MainViewModel
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
fun ListsScreen(
    kits: List<Kit>,
    waveListsHolder: WaveListsHolder,
    onKitSelected: (Kit) -> Unit,
    onWaveSelected: (ListedWave) -> Unit,
    onCopyKit: (Kit) -> Unit = {},
    onPasteKit: (Kit) -> Unit = {},
    hasCopiedKit: Boolean = false,
    onMoveKit: (fromIndex: Int, toIndex: Int) -> Unit = { _, _ -> },
    waveUsageMap: Map<Int, List<String>> = emptyMap(),
    onSelectKitByName: (String) -> Unit = {},
    onRenameCategory: (String, String) -> Unit = { _, _ -> },
    onImportWave: (sourceFile: File, categoryName: String) -> Unit = { _, _ -> },
    importError: String? = null,
    onClearImportError: () -> Unit = {},
    onRequestDeleteWave: (Int) -> Unit = {},
    onConfirmDeleteWave: () -> Unit = {},
    deleteConfirm: MainViewModel.DeleteConfirmInfo? = null,
    deleteBlocked: MainViewModel.DeleteBlockedInfo? = null,
    deleteError: String? = null,
    onClearDeleteConfirm: () -> Unit = {},
    onClearDeleteBlocked: () -> Unit = {},
    onClearDeleteError: () -> Unit = {},
    selectedTab: Int = 0,
    onSelectedTabChange: (Int) -> Unit = {},
    selectedWaveNumber: Int? = null
) {
    var sortingMode by remember { mutableStateOf(SortingMode.BY_CATEGORY_NAME) }
    var showImportCategoryDialog by remember { mutableStateOf(false) }

    val categoryNames = waveListsHolder.wavesByNamePerCategory.keys.map { it.name }
    val onRequestImport: () -> Unit = { showImportCategoryDialog = true }

    val sortingMenuItems: () -> List<ContextMenuItem> = {
        SortingMode.entries.map { mode ->
            ContextMenuItem("View: ${mode.displayName}") { sortingMode = mode }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(3.dp).border(width = 1.dp, color = Color.Black)) {
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { onSelectedTabChange(0) },
                text = { Text(text = "Kits", style = MaterialTheme.typography.titleSmall) }
            )
            ContextMenuArea(items = sortingMenuItems) {
                Tab(
                    selected = selectedTab == 1,
                    onClick = { onSelectedTabChange(1) },
                    text = {
                        Text(
                            text = "Waves (${sortingMode.displayName})",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                )
            }
        }

        when (selectedTab) {
            0 -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    KitListView(
                        kits = kits,
                        onKitSelected = onKitSelected,
                        onCopyKit = onCopyKit,
                        onPasteKit = onPasteKit,
                        hasCopiedKit = hasCopiedKit,
                        onMoveKit = onMoveKit
                    )
                }
            }
            1 -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    when (sortingMode) {
                        SortingMode.BY_NAME -> WaveListByName(
                            waveListsHolder.wavesByName,
                            onWaveSelected,
                            waveUsageMap,
                            onSelectKitByName,
                            sortingMenuItems,
                            onRequestImport,
                            onRequestDeleteWave,
                            selectedWaveNumber
                        )

                        SortingMode.BY_CATEGORY_NAME -> WaveListByCategory(
                            waveListsHolder.wavesByNamePerCategory.sortedForNameView(),
                            onWaveSelected,
                            waveUsageMap,
                            onSelectKitByName,
                            sortingMenuItems,
                            onRenameCategory,
                            onRequestImport,
                            onRequestDeleteWave,
                            selectedWaveNumber
                        )

                        SortingMode.BY_CATEGORY_NUM -> WaveListByCategory(
                            waveListsHolder.wavesByNumPerCategory,
                            onWaveSelected,
                            waveUsageMap,
                            onSelectKitByName,
                            sortingMenuItems,
                            onRenameCategory,
                            onRequestImport,
                            onRequestDeleteWave,
                            selectedWaveNumber
                        )
                    }
                }
            }
        }
    }

    if (showImportCategoryDialog) {
        ImportCategoryDialog(
            categoryNames = categoryNames,
            onDismiss = { showImportCategoryDialog = false },
            onConfirm = { chosenCategory ->
                showImportCategoryDialog = false
                val picked = pickWavFile()
                if (picked != null) {
                    onImportWave(picked, chosenCategory)
                }
            }
        )
    }

    if (importError != null) {
        AlertDialog(
            onDismissRequest = onClearImportError,
            title = { Text("Import failed") },
            text = { Text(importError) },
            confirmButton = {
                TextButton(onClick = onClearImportError) { Text("OK") }
            }
        )
    }

    deleteConfirm?.let { info ->
        DeleteWaveConfirmDialog(
            info = info,
            onDismiss = onClearDeleteConfirm,
            onConfirm = onConfirmDeleteWave
        )
    }

    deleteBlocked?.let { info ->
        WaveInUseDialog(
            info = info,
            onDismiss = onClearDeleteBlocked
        )
    }

    if (deleteError != null) {
        AlertDialog(
            onDismissRequest = onClearDeleteError,
            title = { Text("Delete failed") },
            text = { Text(deleteError) },
            confirmButton = {
                TextButton(onClick = onClearDeleteError) { Text("OK") }
            }
        )
    }
}

private fun pickWavFile(): File? {
    val dialog = FileDialog(null as Frame?, "Select .wav file", FileDialog.LOAD).apply {
        setFilenameFilter { _, name -> name.lowercase().endsWith(".wav") }
        file = "*.wav"
        isVisible = true
    }
    val dir = dialog.directory ?: return null
    val name = dialog.file ?: return null
    return File(dir, name)
}

enum class SortingMode(val displayName: String) {
    BY_CATEGORY_NAME("By Category (Name)"),
    BY_CATEGORY_NUM("By Category (Number)"),
    BY_NAME("By Name");
}

private fun Map<Category, List<ListedWave>>.sortedForNameView(): Map<Category, List<ListedWave>> =
    entries
        .sortedWith(compareBy({ it.key.name != "Default" }, { it.key.name.lowercase() }))
        .associateTo(LinkedHashMap()) { it.key to it.value }

@Composable
fun WaveListByName(
    waves: List<ListedWave>,
    onItemSelected: (ListedWave) -> Unit,
    waveUsageMap: Map<Int, List<String>>,
    onSelectKitByName: (String) -> Unit,
    sortingMenuItems: () -> List<ContextMenuItem>,
    onRequestImport: () -> Unit,
    onRequestDeleteWave: (Int) -> Unit,
    selectedWaveNumber: Int?
) {
    val listState = rememberLazyListState()

    LaunchedEffect(selectedWaveNumber, waves) {
        val target = selectedWaveNumber ?: return@LaunchedEffect
        val index = waves.indexOfFirst { it.number == target }
        if (index >= 0 && listState.layoutInfo.visibleItemsInfo.none { it.index == index }) {
            listState.animateScrollToItem(index)
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
        items(waves.size) { index ->
            val wave = waves[index]
            WaveListItem(
                wave,
                onItemSelected,
                waveUsageMap,
                onSelectKitByName,
                sortingMenuItems,
                onRequestImport,
                onRequestDeleteWave,
                isHighlighted = wave.number == selectedWaveNumber
            )
        }
    }
}

@Composable
fun WaveListByCategory(
    wavesByCategory: Map<Category, List<ListedWave>>,
    onItemSelected: (ListedWave) -> Unit,
    waveUsageMap: Map<Int, List<String>>,
    onSelectKitByName: (String) -> Unit,
    sortingMenuItems: () -> List<ContextMenuItem>,
    onRenameCategory: (String, String) -> Unit,
    onRequestImport: () -> Unit,
    onRequestDeleteWave: (Int) -> Unit,
    selectedWaveNumber: Int?
) {
    val expandedCategories = remember { mutableStateMapOf<String, Boolean>().apply { put("Default", true) } }
    var renameDialogFor by remember { mutableStateOf<String?>(null) }
    val listState = rememberLazyListState()

    val existingNames = wavesByCategory.keys.map { it.name }

    LaunchedEffect(selectedWaveNumber, wavesByCategory) {
        val target = selectedWaveNumber ?: return@LaunchedEffect
        val containing = wavesByCategory.entries.firstOrNull { (_, waves) ->
            waves.any { it.number == target }
        } ?: return@LaunchedEffect
        if (expandedCategories[containing.key.name] != true) {
            expandedCategories[containing.key.name] = true
        }
        var idx = 0
        var found = -1
        for ((cat, waves) in wavesByCategory) {
            idx++
            val expanded = expandedCategories[cat.name] == true
            if (expanded) {
                val waveIdx = waves.indexOfFirst { it.number == target }
                if (waveIdx >= 0) {
                    found = idx + waveIdx
                    break
                }
                idx += waves.size
            }
        }
        if (found >= 0 && listState.layoutInfo.visibleItemsInfo.none { it.index == found }) {
            listState.animateScrollToItem(found)
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
        wavesByCategory.forEach { (category, waves) ->
            val isCollapsed = expandedCategories[category.name] != true
            item {
                ContextMenuArea(
                    items = {
                        listOf(
                            ContextMenuItem("Rename Category…") { renameDialogFor = category.name },
                            ContextMenuItem("Import Wave…") { onRequestImport() }
                        )
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedCategories[category.name] = isCollapsed }
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = category.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (isCollapsed) "▶" else "▼",
                            fontSize = 16.sp
                        )
                    }
                }
            }
            if (!isCollapsed) {
                items(waves.size) { index ->
                    val wave = waves[index]
                    WaveListItem(
                        wave,
                        onItemSelected,
                        waveUsageMap,
                        onSelectKitByName,
                        sortingMenuItems,
                        onRequestImport,
                        onRequestDeleteWave,
                        isHighlighted = wave.number == selectedWaveNumber
                    )
                }
            }
        }
    }

    renameDialogFor?.let { oldName ->
        RenameCategoryDialog(
            oldName = oldName,
            existingNames = existingNames,
            onDismiss = { renameDialogFor = null },
            onConfirm = { newName ->
                val wasExpanded = expandedCategories[oldName] == true
                expandedCategories.remove(oldName)
                if (wasExpanded) expandedCategories[newName] = true
                onRenameCategory(oldName, newName)
                renameDialogFor = null
            }
        )
    }
}

@Composable
private fun ImportCategoryDialog(
    categoryNames: List<String>,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val initial = categoryNames.firstOrNull { it == "Default" } ?: categoryNames.firstOrNull().orEmpty()
    var selected by remember { mutableStateOf(initial) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Import Wave") },
        text = {
            Column {
                Text("Select target category:", fontSize = 14.sp)
                Spacer(Modifier.height(8.dp))
                Box {
                    TextButton(onClick = { expanded = true }) {
                        Text(selected.ifBlank { "(choose category)" })
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categoryNames.forEach { name ->
                            DropdownMenuItem(
                                text = { Text(name) },
                                onClick = {
                                    selected = name
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(selected) },
                enabled = selected.isNotBlank()
            ) { Text("Continue") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun RenameCategoryDialog(
    oldName: String,
    existingNames: List<String>,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var input by remember { mutableStateOf(oldName) }
    val trimmed = input.trim()
    val isEmpty = trimmed.isEmpty()
    val isDuplicate = trimmed != oldName && existingNames.any { it == trimmed }
    val isTooLong = trimmed.length > 12
    val isValid = !isEmpty && !isDuplicate && !isTooLong && trimmed != oldName

    val errorMessage = when {
        isEmpty -> "Name cannot be empty"
        isTooLong -> "Name must be 12 characters or fewer"
        isDuplicate -> "A category with this name already exists"
        else -> null
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Rename Category") },
        text = {
            Column {
                TextField(
                    value = input,
                    onValueChange = { if (it.length <= 12) input = it },
                    singleLine = true,
                    isError = errorMessage != null,
                    label = { Text("Category name") }
                )
                if (errorMessage != null) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(trimmed) },
                enabled = isValid
            ) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun WaveListItem(
    wave: ListedWave,
    onItemSelected: (ListedWave) -> Unit,
    waveUsageMap: Map<Int, List<String>>,
    onSelectKitByName: (String) -> Unit,
    sortingMenuItems: () -> List<ContextMenuItem>,
    onRequestImport: () -> Unit,
    onRequestDeleteWave: (Int) -> Unit,
    isHighlighted: Boolean = false
) {
    val usedInKits = waveUsageMap[wave.number].orEmpty()
    val isUsed = usedInKits.isNotEmpty()

    ContextMenuArea(
        items = {
            buildList {
                if (usedInKits.isNotEmpty()) {
                    usedInKits.forEach { kitName ->
                        add(ContextMenuItem("Used in: $kitName") { onSelectKitByName(kitName) })
                    }
                    add(ContextMenuItem("─────────") {})
                }
                addAll(sortingMenuItems())
                add(ContextMenuItem("Import Wave…") { onRequestImport() })
                add(ContextMenuItem("Delete Wave…") { onRequestDeleteWave(wave.number) })
            }
        }
    ) {
        val backgroundColor = if (isHighlighted) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            Color.White
        }
        GenericListItemView(
            item = wave,
            onItemClicked = onItemSelected,
            backgroundColor = backgroundColor
        ) {
            if (isUsed) {
                Text(
                    text = "● ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1976D2)
                )
            }
            Text(text = "${wave.number}. ${wave.name}", fontSize = 18.sp)
        }
    }
}

@Composable
private fun DeleteWaveConfirmDialog(
    info: MainViewModel.DeleteConfirmInfo,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete wave?") },
        text = {
            Column {
                Text(
                    text = "Wave #${info.waveNumber} \"${info.waveName}\" will be permanently deleted.",
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Both the .spd parameter file and the .wav audio payload will be removed from disk. This action cannot be undone.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Delete", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun WaveInUseDialog(
    info: MainViewModel.DeleteBlockedInfo,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cannot delete wave") },
        text = {
            Column {
                Text(
                    text = "Wave \"${info.waveName}\" is in use and cannot be deleted.",
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(8.dp))
                Text(text = "Used in:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                info.kitNames.forEach { kitName ->
                    Text(text = "• $kitName", fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("OK") }
        }
    )
}