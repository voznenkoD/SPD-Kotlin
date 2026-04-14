package org.xebia.spdmanager.ui.components.system.masterfx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.ui.theme.*
import org.xebia.spdmanager.model.system.fx.MasterEffectConfig

@Composable
fun MasterEffectView(
    masterEffectConfig: MasterEffectConfig,
    onUpdate: (MasterEffectConfig) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
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
            listOf("Filter", "Delay", "S.Loop", "FX").forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title, color = if (selectedTab == index) ColorTextPrimary else ColorTextSecondary) }
                )
            }
        }

        Spacer(modifier = Modifier.height(Spacing.xxl))

        when (selectedTab) {
            0 -> FilterEffectView(
                filterEffect = masterEffectConfig.filterEffect,
                onFilterChange = { updatedFilter ->
                    onUpdate(masterEffectConfig.copy(filterEffect = updatedFilter))
                }
            )
            1 -> DelayEffectView(
                delayEffect = masterEffectConfig.delayEffect,
                onDelayChange = { updatedDelay ->
                    onUpdate(masterEffectConfig.copy(delayEffect = updatedDelay))
                }
            )
            2 -> SLoopEffectView(
                sLoopEffect = masterEffectConfig.sLoopEffect,
                onSLoopChange = { updatedSLoop ->
                    onUpdate(masterEffectConfig.copy(sLoopEffect = updatedSLoop))
                }
            )
            3 -> FxEffectView(
                fx = masterEffectConfig.fxEffect,
                onFxChange = { updatedFx ->
                    onUpdate(masterEffectConfig.copy(fxEffect = updatedFx))
                }
            )
        }
    }
}