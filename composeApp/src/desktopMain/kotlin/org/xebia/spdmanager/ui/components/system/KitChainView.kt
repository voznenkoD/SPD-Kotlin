package org.xebia.spdmanager.ui.components.system

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.model.KitChain
import org.xebia.spdmanager.model.kit.Kit
import org.xebia.spdmanager.model.kit.Kit.Companion.formatKitNumber
import org.xebia.spdmanager.ui.components.lists.GenericListView

@Composable
fun KitChainView(
    kitChains: Map<Char, KitChain>,
    kits: List<Kit>,
    onItemClicked: (Kit) -> Unit
) {
    var selectedTab by remember { mutableStateOf(kitChains.keys.firstOrNull() ?: 'A') }

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        TabRow(selectedTabIndex = kitChains.keys.indexOf(selectedTab)) {
            kitChains.keys.forEach { key ->
                Tab(
                    selected = key == selectedTab,
                    onClick = { selectedTab = key },
                    text = { Text(key.toString(), fontWeight = FontWeight.Bold) }
                )
            }
        }

        kitChains[selectedTab]?.let { kitChain ->
            Text(
                text = kitChain.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            val mappedKits = kitChain.kitRefs.mapIndexedNotNull { remappedIndex, refIndex ->
                kits.getOrNull(refIndex)?.let { kit ->
                    Triple(remappedIndex + 1, refIndex, kit) // Shift by +1
                }
            }

            Box(Modifier.fillMaxSize()) {
                GenericListView(mappedKits, {mappedKit -> onItemClicked(mappedKit.third)} ,
                    {mappedKit -> Text("${"%02d".format(mappedKit.first)}    ${formatKitNumber(mappedKit.second)}  ${mappedKit.third.name} ${mappedKit.third.subName}", fontFamily = FontFamily.Monospace)})
            }
        }
    }
}
