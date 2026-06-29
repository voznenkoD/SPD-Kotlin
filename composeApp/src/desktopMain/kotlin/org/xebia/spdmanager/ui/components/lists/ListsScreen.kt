package org.xebia.spdmanager.ui.components.lists

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.model.kit.Kit
import org.xebia.spdmanager.ui.theme.*
import org.xebia.spdmanager.ui.theme.Typography
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
    onKitSelected: (index: Int) -> Unit,
    onWaveSelected: (ListedWave) -> Unit,
    onCopyKit: (Kit) -> Unit = {},
    onPasteKit: (index: Int) -> Unit = {},
    hasCopiedKit: Boolean = false,
    onDuplicateKit: (index: Int) -> Unit = {},
    kitLimitReached: Boolean = false,
    onClearKitLimitReached: () -> Unit = {},
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
    selectedWaveNumber: Int? = null,
    selectedKitIndex: Int? = null,
    onStartWaveDrag: (waveNumber: Int, waveName: String) -> Unit = { _, _ -> },
    onUpdateDragPosition: (Offset) -> Unit = {},
    onEndWaveDrag: () -> Unit = {},
    onCancelWaveDrag: () -> Unit = {},
    onRenameWave: (waveNumber: Int, newName: String) -> Unit = { _, _ -> },
    onMoveWaveToCategory: (waveNumber: Int, categoryName: String) -> Unit = { _, _ -> },
    categoryOfWave: (Int) -> String? = { null },
    waveOpError: String? = null,
    onClearWaveOpError: () -> Unit = {}
) {
    var sortingMode by remember { mutableStateOf(SortingMode.BY_CATEGORY_NAME) }
    var showImportCategoryDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var renameWaveTarget by remember { mutableStateOf<ListedWave?>(null) }

    // Search applies only to the Waves tab; clear it whenever the tab changes (no persistence).
    LaunchedEffect(selectedTab) { searchQuery = "" }
    val query = searchQuery.trim()

    val categoryNames = waveListsHolder.wavesByNamePerCategory.keys.map { it.name }
    val allWaveNames = waveListsHolder.wavesByName.map { it.name }
    val onRequestImport: () -> Unit = { showImportCategoryDialog = true }

    val waveOps = WaveOps(
        allCategoryNames = categoryNames,
        categoryOfWave = categoryOfWave,
        onRequestRenameWave = { renameWaveTarget = it },
        onMoveWaveToCategory = onMoveWaveToCategory
    )

    val sortingMenuItems: () -> List<ContextMenuItem> = {
        SortingMode.entries.map { mode ->
            ContextMenuItem("View: ${mode.displayName}") { sortingMode = mode }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(Spacing.s).border(width = 1.dp, color = ColorDivider)) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = ColorSurface,
            contentColor = ColorTextPrimary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = ColorAccentOrange
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { onSelectedTabChange(0) },
                text = { Text(text = "Kits", style = Typography.body, color = if (selectedTab == 0) ColorTextPrimary else ColorTextSecondary) }
            )
            ContextMenuArea(items = sortingMenuItems) {
                Tab(
                    selected = selectedTab == 1,
                    onClick = { onSelectedTabChange(1) },
                    text = {
                        Text(
                            text = "Waves (${sortingMode.displayName})",
                            style = Typography.body,
                            color = if (selectedTab == 1) ColorTextPrimary else ColorTextSecondary
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
                        onDuplicateKit = onDuplicateKit,
                        onMoveKit = onMoveKit,
                        selectedKitIndex = selectedKitIndex
                    )
                }
            }
            1 -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    WaveSearchField(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        onClear = { searchQuery = "" }
                    )
                    Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                        when (sortingMode) {
                            SortingMode.BY_NAME -> {
                                val filtered = remember(query, waveListsHolder) {
                                    waveListsHolder.wavesByName.filteredByName(query)
                                }
                                if (query.isNotBlank() && filtered.isEmpty()) {
                                    NoWavesFound(sortingMenuItems, onRequestImport)
                                } else {
                                    WaveListByName(
                                        filtered,
                                        onWaveSelected,
                                        waveUsageMap,
                                        onSelectKitByName,
                                        sortingMenuItems,
                                        onRequestImport,
                                        onRequestDeleteWave,
                                        selectedWaveNumber,
                                        onStartWaveDrag,
                                        onUpdateDragPosition,
                                        onEndWaveDrag,
                                        onCancelWaveDrag,
                                        waveOps = waveOps
                                    )
                                }
                            }

                            SortingMode.BY_CATEGORY_NAME -> {
                                val filtered = remember(query, waveListsHolder) {
                                    waveListsHolder.wavesByNamePerCategory
                                        .sortedForNameView()
                                        .filteredByName(query)
                                }
                                if (query.isNotBlank() && filtered.values.all { it.isEmpty() }) {
                                    NoWavesFound(sortingMenuItems, onRequestImport)
                                } else {
                                    WaveListByCategory(
                                        filtered,
                                        onWaveSelected,
                                        waveUsageMap,
                                        onSelectKitByName,
                                        sortingMenuItems,
                                        onRenameCategory,
                                        onRequestImport,
                                        onRequestDeleteWave,
                                        selectedWaveNumber,
                                        onStartWaveDrag,
                                        onUpdateDragPosition,
                                        onEndWaveDrag,
                                        onCancelWaveDrag,
                                        searchQuery = query,
                                        waveOps = waveOps
                                    )
                                }
                            }

                            SortingMode.BY_CATEGORY_NUM -> {
                                val filtered = remember(query, waveListsHolder) {
                                    waveListsHolder.wavesByNumPerCategory.filteredByName(query)
                                }
                                if (query.isNotBlank() && filtered.values.all { it.isEmpty() }) {
                                    NoWavesFound(sortingMenuItems, onRequestImport)
                                } else {
                                    WaveListByCategory(
                                        filtered,
                                        onWaveSelected,
                                        waveUsageMap,
                                        onSelectKitByName,
                                        sortingMenuItems,
                                        onRenameCategory,
                                        onRequestImport,
                                        onRequestDeleteWave,
                                        selectedWaveNumber,
                                        onStartWaveDrag,
                                        onUpdateDragPosition,
                                        onEndWaveDrag,
                                        onCancelWaveDrag,
                                        searchQuery = query,
                                        waveOps = waveOps
                                    )
                                }
                            }
                        }
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
        SimpleInfoDialog("Import failed", importError, onClearImportError)
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
        SimpleInfoDialog("Delete failed", deleteError, onClearDeleteError)
    }

    renameWaveTarget?.let { target ->
        RenameWaveDialog(
            oldName = target.name,
            // Exclude the wave's own name so re-confirming the same name isn't flagged as a duplicate.
            existingNames = allWaveNames.filter { it != target.name },
            onDismiss = { renameWaveTarget = null },
            onConfirm = { newName ->
                onRenameWave(target.number, newName)
                renameWaveTarget = null
            }
        )
    }

    if (waveOpError != null) {
        SimpleInfoDialog("Operation failed", waveOpError, onClearWaveOpError)
    }

    if (kitLimitReached) {
        KitLimitReachedDialog(onDismiss = onClearKitLimitReached)
    }
}

/**
 * Reusable single-message dialog with one OK button, styled like the app's other info dialogs.
 * Shared by the import/delete/wave-op error dialogs and the kit-limit dialog.
 */
@Composable
fun SimpleInfoDialog(title: String, message: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ColorSurface,
        titleContentColor = ColorTextPrimary,
        textContentColor = ColorTextPrimary,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("OK", color = ColorAccentOrange) }
        }
    )
}

/**
 * Reusable dialog shown when an operation would exceed the device's kit limit (e.g. duplicating a
 * kit when the list is full). Kept generic so other kit-limit cases can reuse it.
 */
@Composable
fun KitLimitReachedDialog(onDismiss: () -> Unit) {
    SimpleInfoDialog(
        title = "Maximum of kit numbers has been reached",
        message = "You cannot add another kit.",
        onDismiss = onDismiss
    )
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

/**
 * Bundles the callbacks for the right-click wave operations (rename, move-to-category) so they can be
 * threaded through the list composables without exploding every signature. [categoryOfWave] lets the
 * "Move to" menu exclude the wave's current category (needed in the flat By-Name view too).
 */
class WaveOps(
    val allCategoryNames: List<String>,
    val categoryOfWave: (Int) -> String?,
    val onRequestRenameWave: (ListedWave) -> Unit,
    val onMoveWaveToCategory: (Int, String) -> Unit
)

private fun Map<Category, List<ListedWave>>.sortedForNameView(): Map<Category, List<ListedWave>> =
    entries
        .sortedWith(compareBy({ it.key.name != "Default" }, { it.key.name.lowercase() }))
        .associateTo(LinkedHashMap()) { it.key to it.value }

// Case-insensitive substring match anywhere in the wave name (not prefix-based). Blank query = no filtering.
private fun List<ListedWave>.filteredByName(query: String): List<ListedWave> =
    if (query.isBlank()) this else filter { it.name.contains(query, ignoreCase = true) }

// Filters each category's waves while preserving every category key and its order, so categories
// with zero matches remain shown (empty) rather than being dropped.
private fun Map<Category, List<ListedWave>>.filteredByName(query: String): Map<Category, List<ListedWave>> =
    if (query.isBlank()) this
    else mapValues { (_, waves) -> waves.filteredByName(query) }

// Built on BasicTextField to match the app's other editable fields (e.g. KitScreen), rather than
// introducing a second Material3 TextField styling system.
@Composable
private fun WaveSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(Spacing.s),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            singleLine = true,
            textStyle = Typography.body.copy(color = ColorTextPrimary),
            cursorBrush = SolidColor(ColorAccentOrange),
            modifier = Modifier
                .weight(1f)
                .height(Heights.button)
                .border(1.dp, ColorDivider, ShapeDefault)
                .padding(horizontal = Spacing.l, vertical = Spacing.m),
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.CenterStart) {
                    if (query.isEmpty()) {
                        Text(
                            text = "Search waves by name",
                            style = Typography.body,
                            color = ColorTextSecondary
                        )
                    }
                    innerTextField()
                }
            }
        )
        if (query.isNotEmpty()) {
            Text(
                text = "✕",
                style = Typography.body,
                color = ColorAccentOrange,
                modifier = Modifier
                    .clickable { onClear() }
                    .padding(horizontal = Spacing.l)
            )
        }
    }
}

@Composable
private fun NoWavesFound(
    sortingMenuItems: () -> List<ContextMenuItem>,
    onRequestImport: () -> Unit
) {
    // Keep the right-click affordances (view modes, Import Wave…) available on the empty-results
    // screen so a search-then-import flow isn't blocked.
    ContextMenuArea(
        items = {
            buildList {
                addAll(sortingMenuItems())
                add(ContextMenuItem("Import Wave…") { onRequestImport() })
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "No waves found",
                style = Typography.body,
                color = ColorTextSecondary
            )
        }
    }
}

@Composable
fun WaveListByName(
    waves: List<ListedWave>,
    onItemSelected: (ListedWave) -> Unit,
    waveUsageMap: Map<Int, List<String>>,
    onSelectKitByName: (String) -> Unit,
    sortingMenuItems: () -> List<ContextMenuItem>,
    onRequestImport: () -> Unit,
    onRequestDeleteWave: (Int) -> Unit,
    selectedWaveNumber: Int?,
    onStartWaveDrag: (Int, String) -> Unit = { _, _ -> },
    onUpdateDragPosition: (Offset) -> Unit = {},
    onEndWaveDrag: () -> Unit = {},
    onCancelWaveDrag: () -> Unit = {},
    waveOps: WaveOps
) {
    val listState = rememberLazyListState()

    // Key only on the selection: the `waves` list identity changes on every search keystroke, and
    // keying on it would re-fire this effect per character, scrolling the list mid-typing.
    LaunchedEffect(selectedWaveNumber) {
        val target = selectedWaveNumber ?: return@LaunchedEffect
        val index = waves.indexOfFirst { it.number == target }
        if (index >= 0) listState.scrollIntoView(index)
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
                isHighlighted = wave.number == selectedWaveNumber,
                onStartWaveDrag = onStartWaveDrag,
                onUpdateDragPosition = onUpdateDragPosition,
                onEndWaveDrag = onEndWaveDrag,
                onCancelWaveDrag = onCancelWaveDrag,
                waveOps = waveOps
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
    selectedWaveNumber: Int?,
    onStartWaveDrag: (Int, String) -> Unit = { _, _ -> },
    onUpdateDragPosition: (Offset) -> Unit = {},
    onEndWaveDrag: () -> Unit = {},
    onCancelWaveDrag: () -> Unit = {},
    searchQuery: String = "",
    waveOps: WaveOps
) {
    val expandedCategories = remember { mutableStateMapOf<String, Boolean>().apply { put("Default", true) } }
    // Transient collapse overrides (true = collapsed) used only while a search is active, so the
    // persisted manual state in expandedCategories is never touched and is restored verbatim once
    // the query is cleared.
    val searchExpandOverride = remember { mutableStateMapOf<String, Boolean>() }
    var renameDialogFor by remember { mutableStateOf<String?>(null) }
    val listState = rememberLazyListState()
    val queryActive = searchQuery.isNotBlank()

    // Reset the transient overrides whenever the query text changes (including when it is cleared),
    // so a collapse chosen for one search term doesn't leak into a different term within the same
    // active session.
    LaunchedEffect(searchQuery) { searchExpandOverride.clear() }

    // Display-only collapse state. During a search a category is auto-expanded when it has matches,
    // unless the user explicitly toggled it this search session; otherwise the persisted manual
    // state drives it. Reading/writing this never mutates expandedCategories while searching.
    fun isCategoryCollapsed(name: String, waves: List<ListedWave>): Boolean =
        if (queryActive) searchExpandOverride[name] ?: waves.isEmpty()
        else expandedCategories[name] != true

    val existingNames = wavesByCategory.keys.map { it.name }

    // Key only on the selection: wavesByCategory is rebuilt on every search keystroke, and keying
    // on it would re-fire this effect per character — reverting in-session collapses and (after the
    // query is cleared) re-expanding categories in the persisted manual state.
    LaunchedEffect(selectedWaveNumber) {
        val target = selectedWaveNumber ?: return@LaunchedEffect
        val containing = wavesByCategory.entries.firstOrNull { (_, waves) ->
            waves.any { it.number == target }
        } ?: return@LaunchedEffect
        // Reveal the containing category, writing to the transient override during search so the
        // persisted manual state stays intact.
        if (queryActive) {
            searchExpandOverride[containing.key.name] = false
        } else if (expandedCategories[containing.key.name] != true) {
            expandedCategories[containing.key.name] = true
        }
        var idx = 0
        var found = -1
        for ((cat, waves) in wavesByCategory) {
            idx++
            val expanded = !isCategoryCollapsed(cat.name, waves)
            if (expanded) {
                val waveIdx = waves.indexOfFirst { it.number == target }
                if (waveIdx >= 0) {
                    found = idx + waveIdx
                    break
                }
                idx += waves.size
            }
        }
        if (found >= 0) listState.scrollIntoView(found)
    }

    LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
        wavesByCategory.forEach { (category, waves) ->
            val isCollapsed = isCategoryCollapsed(category.name, waves)
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
                            .clickable {
                                // While searching, toggle only the transient override so manual
                                // expand/collapse state survives the search untouched.
                                if (queryActive) {
                                    searchExpandOverride[category.name] = !isCollapsed
                                } else {
                                    expandedCategories[category.name] = isCollapsed
                                }
                            }
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = category.name,
                            fontSize = Typography.titleSize,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (isCollapsed) "▶" else "▼",
                            fontSize = Typography.bodySize
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
                        isHighlighted = wave.number == selectedWaveNumber,
                        onStartWaveDrag = onStartWaveDrag,
                        onUpdateDragPosition = onUpdateDragPosition,
                        onEndWaveDrag = onEndWaveDrag,
                        onCancelWaveDrag = onCancelWaveDrag,
                        waveOps = waveOps,
                        categoryName = category.name
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
                // Carry any in-session search collapse override to the new name too.
                searchExpandOverride.remove(oldName)?.let { searchExpandOverride[newName] = it }
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
        containerColor = ColorSurface,
        titleContentColor = ColorTextPrimary,
        textContentColor = ColorTextPrimary,
        title = { Text("Import Wave") },
        text = {
            Column {
                Text("Select target category:", fontSize = Typography.bodySize)
                Spacer(Modifier.height(8.dp))
                Box {
                    TextButton(onClick = { expanded = true }) {
                        Text(selected.ifBlank { "(choose category)" }, color = ColorAccentOrange)
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
            ) { Text("Continue", color = ColorAccentOrange) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = ColorTextSecondary) }
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
    RenameDialog(
        title = "Rename Category",
        fieldLabel = "Category name",
        duplicateMessage = "A category with this name already exists",
        oldName = oldName,
        existingNames = existingNames,
        onDismiss = onDismiss,
        onConfirm = onConfirm
    )
}

/**
 * Shared rename dialog used for both categories and waves. Enforces a non-empty, ≤12-char name that
 * differs from [oldName] and is not among [existingNames]; the only per-use differences are the
 * [title], [fieldLabel] and [duplicateMessage].
 */
@Composable
private fun RenameDialog(
    title: String,
    fieldLabel: String,
    duplicateMessage: String,
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
        isDuplicate -> duplicateMessage
        else -> null
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ColorSurface,
        titleContentColor = ColorTextPrimary,
        textContentColor = ColorTextPrimary,
        title = { Text(title) },
        text = {
            Column {
                TextField(
                    value = input,
                    onValueChange = { if (it.length <= 12) input = it },
                    singleLine = true,
                    isError = errorMessage != null,
                    label = { Text(fieldLabel) },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = ColorSurface,
                        focusedContainerColor = ColorBackground,
                        focusedIndicatorColor = ColorAccentOrange,
                        unfocusedIndicatorColor = ColorDivider,
                        cursorColor = ColorAccentOrange
                    )
                )
                if (errorMessage != null) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = errorMessage,
                        color = ColorAccentOrange,
                        fontSize = Typography.captionSize
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(trimmed) },
                enabled = isValid
            ) { Text("OK", color = ColorAccentOrange) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = ColorTextSecondary) }
        }
    )
}

@Composable
private fun RenameWaveDialog(
    oldName: String,
    existingNames: List<String>,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    RenameDialog(
        title = "Rename Wave",
        fieldLabel = "Wave name",
        duplicateMessage = "A wave with this name already exists",
        oldName = oldName,
        existingNames = existingNames,
        onDismiss = onDismiss,
        onConfirm = onConfirm
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
    isHighlighted: Boolean = false,
    onStartWaveDrag: (Int, String) -> Unit = { _, _ -> },
    onUpdateDragPosition: (Offset) -> Unit = {},
    onEndWaveDrag: () -> Unit = {},
    onCancelWaveDrag: () -> Unit = {},
    waveOps: WaveOps,
    categoryName: String? = null
) {
    val usedInKits = waveUsageMap[wave.number].orEmpty()
    val isUsed = usedInKits.isNotEmpty()
    var itemWindowPosition by remember { mutableStateOf(Offset.Zero) }

    // The category currently holding this wave: known directly in the category views, otherwise
    // looked up (flat By-Name view) so the "Move to" menu can exclude the current category.
    val rowCategory = categoryName ?: waveOps.categoryOfWave(wave.number)

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
                add(ContextMenuItem("─────────") {})
                add(ContextMenuItem("Import Wave…") { onRequestImport() })
                add(ContextMenuItem("Rename Wave…") { waveOps.onRequestRenameWave(wave) })
                add(ContextMenuItem("Delete Wave…") { onRequestDeleteWave(wave.number) })
                val moveTargets = waveOps.allCategoryNames.filter { it != rowCategory }
                if (moveTargets.isNotEmpty()) {
                    add(ContextMenuItem("─────────") {})
                    moveTargets.forEach { cat ->
                        add(ContextMenuItem("Move to: $cat") { waveOps.onMoveWaveToCategory(wave.number, cat) })
                    }
                }
            }
        }
    ) {
        val backgroundColor = if (isHighlighted) {
            ColorSurfaceSelected
        } else {
            ColorSurface
        }
        Box(
            modifier = Modifier
                .onGloballyPositioned { coords ->
                    itemWindowPosition = coords.positionInWindow()
                }
                .pointerInput(wave.number, wave.name) {
                    detectDragGesturesAfterLongPress(
                        onDragStart = {
                            onStartWaveDrag(wave.number, wave.name)
                            onUpdateDragPosition(itemWindowPosition + it)
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            onUpdateDragPosition(itemWindowPosition + change.position)
                        },
                        onDragEnd = { onEndWaveDrag() },
                        onDragCancel = { onCancelWaveDrag() }
                    )
                }
        ) {
            GenericListItemView(
                item = wave,
                onItemClicked = onItemSelected,
                backgroundColor = backgroundColor
            ) {
                if (isUsed) {
                    Text(
                        text = "● ",
                        fontSize = Typography.bodySize,
                        fontWeight = FontWeight.Bold,
                        color = ColorAccentOrange
                    )
                }
                Text(
                    text = "${wave.number}. ${wave.name}",
                    fontSize = Typography.bodySize,
                    color = if (isHighlighted) ColorTextOnDark else ColorTextPrimary
                )
            }
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
        containerColor = ColorSurface,
        titleContentColor = ColorTextPrimary,
        textContentColor = ColorTextPrimary,
        title = { Text("Delete wave?") },
        text = {
            Column {
                Text(
                    text = "Wave #${info.waveNumber} \"${info.waveName}\" will be permanently deleted.",
                    fontSize = Typography.bodySize
                )
                Spacer(Modifier.height(Spacing.xl))
                Text(
                    text = "Both the .spd parameter file and the .wav audio payload will be removed from disk. This action cannot be undone.",
                    fontSize = Typography.captionSize,
                    color = ColorAccentOrange
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Delete", color = ColorAccentOrange)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = ColorTextSecondary) }
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
        containerColor = ColorSurface,
        titleContentColor = ColorTextPrimary,
        textContentColor = ColorTextPrimary,
        title = { Text("Cannot delete wave") },
        text = {
            Column {
                Text(
                    text = "Wave \"${info.waveName}\" is in use and cannot be deleted.",
                    fontSize = Typography.bodySize
                )
                Spacer(Modifier.height(Spacing.xl))
                Text(text = "Used in:", fontSize = Typography.captionSize, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(Spacing.m))
                info.kitNames.forEach { kitName ->
                    Text(text = "• $kitName", fontSize = Typography.captionSize)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("OK", color = ColorAccentOrange) }
        }
    )
}