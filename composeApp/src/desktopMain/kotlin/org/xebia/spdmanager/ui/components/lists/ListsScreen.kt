package org.xebia.spdmanager.ui.components.lists

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
    onRenameCategory: (String, String) -> Unit = { _, _ -> }
) {
    var selectedTab by remember { mutableStateOf(0) }
    var sortingMode by remember { mutableStateOf(SortingMode.BY_CATEGORY_NAME) }

    val sortingMenuItems: () -> List<ContextMenuItem> = {
        SortingMode.entries.map { mode ->
            ContextMenuItem("View: ${mode.displayName}") { sortingMode = mode }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(3.dp).border(width = 1.dp, color = Color.Black)) {
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text(text = "Kits", style = MaterialTheme.typography.titleSmall) }
            )
            ContextMenuArea(items = sortingMenuItems) {
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
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
                            sortingMenuItems
                        )

                        SortingMode.BY_CATEGORY_NAME -> WaveListByCategory(
                            waveListsHolder.wavesByNamePerCategory.sortedForNameView(),
                            onWaveSelected,
                            waveUsageMap,
                            onSelectKitByName,
                            sortingMenuItems,
                            onRenameCategory
                        )

                        SortingMode.BY_CATEGORY_NUM -> WaveListByCategory(
                            waveListsHolder.wavesByNumPerCategory,
                            onWaveSelected,
                            waveUsageMap,
                            onSelectKitByName,
                            sortingMenuItems,
                            onRenameCategory
                        )
                    }
                }
            }
        }
    }
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
    sortingMenuItems: () -> List<ContextMenuItem>
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(waves.size) { index ->
            val wave = waves[index]
            WaveListItem(wave, onItemSelected, waveUsageMap, onSelectKitByName, sortingMenuItems)
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
    onRenameCategory: (String, String) -> Unit
) {
    val expandedCategories = remember { mutableStateMapOf<String, Boolean>().apply { put("Default", true) } }
    var renameDialogFor by remember { mutableStateOf<String?>(null) }

    val existingNames = wavesByCategory.keys.map { it.name }

    LazyColumn(Modifier.fillMaxSize()) {
        wavesByCategory.forEach { (category, waves) ->
            val isCollapsed = expandedCategories[category.name] != true
            item {
                ContextMenuArea(
                    items = {
                        listOf(
                            ContextMenuItem("Rename Category…") { renameDialogFor = category.name }
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
                    WaveListItem(wave, onItemSelected, waveUsageMap, onSelectKitByName, sortingMenuItems)
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
    sortingMenuItems: () -> List<ContextMenuItem>
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
            }
        }
    ) {
        GenericListItemView(item = wave, onItemClicked = onItemSelected) {
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