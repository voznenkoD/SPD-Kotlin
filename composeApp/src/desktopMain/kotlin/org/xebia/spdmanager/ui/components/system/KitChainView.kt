package org.xebia.spdmanager.ui.components.system

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.model.KitChain
import org.xebia.spdmanager.model.kit.Kit

@Composable
fun KitChainView(
    kitChains: Map<Char, KitChain>,
    kits: List<Kit>,
    onItemClicked: (Int) -> Unit
) {
    var selectedTab by remember { mutableStateOf(kitChains.keys.firstOrNull() ?: 'A') }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Tabs for each Char key
        TabRow(selectedTabIndex = kitChains.keys.indexOf(selectedTab)) {
            kitChains.keys.forEach { key ->
                Tab(
                    selected = key == selectedTab,
                    onClick = { selectedTab = key },
                    text = { Text(key.toString(), fontWeight = FontWeight.Bold) }
                )
            }
        }

        // Display KitChain Name Below Tabs
        kitChains[selectedTab]?.let { kitChain ->
            Text(
                text = kitChain.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Map kitRefs to actual Kit objects
            val mappedKits = kitChain.kitRefs.mapNotNull { index ->
                kits.getOrNull(index)?.let { kit -> index to kit }
            }.toMap()

            // LazyColumn for displaying kits
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(mappedKits.toList()) { (index, kit) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { onItemClicked(index) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Index in Red
                        Text(
                            text = "$index --> ",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = 4.dp)
                        )

                        // Kit Name and Subname in Black
                        Text(
                            text = "${kit.name} ${kit.subName}",
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}
