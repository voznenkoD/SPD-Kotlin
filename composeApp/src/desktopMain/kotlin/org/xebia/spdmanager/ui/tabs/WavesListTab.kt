package org.xebia.spdmanager.ui.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.list.Category
import org.xebia.spdmanager.model.list.ListedWave
import org.xebia.spdmanager.model.list.WaveListsHolder
import org.xebia.spdmanager.ui.components.lists.GenericListItemView
import org.xebia.spdmanager.ui.components.lists.GenericListView

@Composable
fun WaveListTab(
    waveListsHolder: WaveListsHolder,
    onListedWaveSelected: (ListedWave) -> Unit
) {
    var sortingMode by remember { mutableStateOf(SortingMode.BY_CATEGORY_NAME)}

    Column(Modifier.fillMaxSize()) {
        Box(Modifier.weight(1f)) {
            when (sortingMode) {
                SortingMode.BY_NAME -> GenericListView(waveListsHolder.wavesByName, onListedWaveSelected) { wave ->
                    Text(text = "${wave.number}. ${wave.name}", fontSize = 18.sp)
                }
                SortingMode.BY_CATEGORY_NAME -> WaveListByCategory(waveListsHolder.wavesByNamePerCategory, onListedWaveSelected)
                SortingMode.BY_CATEGORY_NUM -> WaveListByCategory(waveListsHolder.wavesByNumPerCategory, onListedWaveSelected)
            }
        }

        Row(
            Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SortingMode.entries.forEach { mode ->
                Button(onClick = { sortingMode = mode }) {
                    Text(text = mode.displayName)
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

@Composable
fun WaveListByCategory(
    wavesByCategory: Map<Category, List<ListedWave>>,
    onItemSelected: (ListedWave) -> Unit
) {
    LazyColumn(Modifier.fillMaxSize()) {
        wavesByCategory.forEach { (category, waves) ->
            item {
                Text(
                    text = category.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )
            }
            items(waves.size) { index ->
                val wave = waves[index]
                GenericListItemView(item = wave, onItemClicked = onItemSelected) {
                    Text(text = "${wave.number}. ${wave.name}", fontSize = 18.sp)
                }
            }
        }
    }
}
