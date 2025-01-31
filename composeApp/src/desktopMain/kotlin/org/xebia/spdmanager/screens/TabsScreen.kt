package org.xebia.spdmanager.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.model.Kit
import org.xebia.spdmanager.tabs.KitTab
import org.xebia.spdmanager.tabs.WaveTab

@Composable
fun TabsScreen(kits: List<Kit>, onItemClicked: (Kit) -> Unit) {
    var selectedTab by remember { mutableStateOf(0) } // Keep track of selected tab

    val tabs = listOf("Kit", "Wave")

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
            0 -> KitTab(kits, onItemClicked)
            1 -> WaveTab()
        }
    }
}
