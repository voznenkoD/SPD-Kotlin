package org.xebia.spdmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.model.kit.Kit
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.list.ListedWave
import org.xebia.spdmanager.model.list.WaveListsHolder
import org.xebia.spdmanager.ui.tabs.KitListTab
import org.xebia.spdmanager.ui.tabs.WaveListTab

@Composable
fun ListsScreen(
    kits: List<Kit>,
    waveListsHolder: WaveListsHolder,
    onKitSelected: (Kit) -> Unit,
    onWaveSelected: (ListedWave) -> Unit
) {
    ListHeaderTabs(kits, waveListsHolder, onKitSelected, onWaveSelected)
}

@Composable
fun ListHeaderTabs(
    kits: List<Kit>,
    waveListsHolder: WaveListsHolder,
    onKitSelected: (Kit) -> Unit,
    onWaveSelected: (ListedWave) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }

    val tabs = listOf("Kits", "Waves")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedTab
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTab) {
            0 -> KitListTab(kits, onKitSelected) { kit -> Text(text = kit.name, fontSize = 18.sp) }
            1 -> WaveListTab(waveListsHolder, onWaveSelected)
        }
    }
}
