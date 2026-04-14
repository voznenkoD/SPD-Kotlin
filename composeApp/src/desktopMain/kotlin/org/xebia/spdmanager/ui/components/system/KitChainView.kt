package org.xebia.spdmanager.ui.components.system

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import org.xebia.spdmanager.ui.theme.*
import org.xebia.spdmanager.ui.theme.Typography as AppTypography
import org.xebia.spdmanager.model.KitChain
import org.xebia.spdmanager.model.kit.Kit
import org.xebia.spdmanager.model.kit.Kit.Companion.formatKitNumber
import org.xebia.spdmanager.ui.components.lists.GenericListView

@Composable
fun KitChainView(
    kitChains: Map<Char, KitChain>,
    kits: List<Kit>,
    onUpdate: (Map<Char, KitChain>) -> Unit
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

            val mappedKits = kitChain.kitRefs.mapIndexedNotNull { remappedIndex, refIndex ->
                kits.getOrNull(refIndex)?.let { kit ->
                    Triple(remappedIndex + 1, refIndex, kit) // Shift by +1
                }
            }

            Box(Modifier.fillMaxSize()) {
                GenericListView(
                    items = mappedKits,
                    onItemSelected = { mappedKit ->
                        // If you need to handle kit selection, you could update the kit chain here
                        // For now, this might just be for viewing/selection UI feedback
                        // You could add logic here if needed to modify kit chains
                    },
                    content = { mappedKit ->
                        Text(
                            "${"%02d".format(mappedKit.first)}    ${formatKitNumber(mappedKit.second)}  ${mappedKit.third.name} ${mappedKit.third.subName}",
                            style = AppTypography.mono,
                            color = ColorTextPrimary
                        )
                    }
                )
            }
        }
    }
}