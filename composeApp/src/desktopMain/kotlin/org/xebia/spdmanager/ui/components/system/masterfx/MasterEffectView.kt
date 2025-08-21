package org.xebia.spdmanager.ui.components.system.masterfx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.model.system.fx.MasterEffectConfig

@Composable
fun MasterEffectView(
    masterEffectConfig: MasterEffectConfig,
    onUpdate: (MasterEffectConfig) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            listOf("Filter", "Delay", "S.Loop", "FX").forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

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