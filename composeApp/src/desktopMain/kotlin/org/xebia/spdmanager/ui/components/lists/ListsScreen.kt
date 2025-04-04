package org.xebia.spdmanager.ui.components.lists

import androidx.compose.foundation.border
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
    onWaveSelected: (ListedWave) -> Unit
) {
    var sortingMode by remember { mutableStateOf(SortingMode.BY_CATEGORY_NAME) }

    Row(modifier = Modifier.fillMaxSize().padding(3.dp)) {
        Column(
            modifier = Modifier
                .weight(0.45f)
                .fillMaxHeight()
                .padding(end = 8.dp)
                .border(width = 1.dp, color = Color.Black)
        ) {
            Text(text = "Kits", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Box(modifier = Modifier.weight(1f)) {
                GenericListView(kits, onKitSelected) { kit ->
                    Text(text = kit.name, fontSize = 18.sp)
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { }) {
                    Text(text = "Copy")
                }
                Button(onClick = { }) {
                    Text(text = "Paste")
                }
                Button(onClick = { }) {
                    Text(text = "Init")
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(0.55f)
                .fillMaxHeight()
                .border(width = 1.dp, color = Color.Black)
        ) {
            Text(text = "Waves", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(5.dp))
            Box(modifier = Modifier.weight(1f)) {
                when (sortingMode) {
                    SortingMode.BY_NAME -> GenericListView(waveListsHolder.wavesByName, onWaveSelected) { wave ->
                        Text(text = "${wave.number}. ${wave.name}", fontSize = 18.sp)
                    }

                    SortingMode.BY_CATEGORY_NAME -> WaveListByCategory(
                        waveListsHolder.wavesByNamePerCategory,
                        onWaveSelected
                    )

                    SortingMode.BY_CATEGORY_NUM -> WaveListByCategory(
                        waveListsHolder.wavesByNumPerCategory,
                        onWaveSelected
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                SortingMode.entries.forEach { mode ->
                    Button(onClick = { sortingMode = mode }) {
                        Text(text = mode.displayName)
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
