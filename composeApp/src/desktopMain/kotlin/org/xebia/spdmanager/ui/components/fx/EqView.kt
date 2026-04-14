package org.xebia.spdmanager.ui.components.fx

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.xebia.spdmanager.ui.theme.*
import org.xebia.spdmanager.ui.theme.Typography
import org.xebia.spdmanager.model.system.fx.common.*
import org.xebia.spdmanager.model.system.fx.subtypes.EQ
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect
import org.xebia.spdmanager.ui.components.common.DropdownSelector
import org.xebia.spdmanager.ui.components.common.SliderWithLabel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EqView(
    fx: EQ,
    onFxChange: (FxEffect) -> Unit
) {
    Column(modifier = Modifier.padding(Spacing.m).fillMaxWidth()) {
        Text("EQ Settings", fontSize = Typography.titleSize)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.xl)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                DropdownSelector(
                    label = "Low Cut (Hz)",
                    selectedItem = fx.lowCut,
                    items = LowCut.entries.toList(),
                    onItemSelected = { newLowCut ->
                        onFxChange(fx.copy(lowCut = newLowCut))
                    }
                )

                SliderWithLabel(
                    label = "Low Gain",
                    value = fx.lowGain,
                    onValueChange = { newLowGain ->
                        onFxChange(fx.copy(lowGain = newLowGain))
                    },
                    valueRange = -15f..15f, bipolar = true
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                DropdownSelector(
                    label = "High Cut (Hz)",
                    selectedItem = fx.hiCut,
                    items = HighCut.entries.toList(),
                    onItemSelected = { newHiCut ->
                        onFxChange(fx.copy(hiCut = newHiCut))
                    }
                )
                SliderWithLabel(
                    label = "High Gain",
                    value = fx.hiGain,
                    onValueChange = { newHiGain ->
                        onFxChange(fx.copy(hiGain = newHiGain))
                    },
                    valueRange = -15f..15f, bipolar = true
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.xl)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                DropdownSelector(
                    label = "Pkg1 Frequency",
                    selectedItem = fx.pkg1Freq,
                    items = EqFreq.entries.toList(),
                    onItemSelected = { newPkg1Freq ->
                        onFxChange(fx.copy(pkg1Freq = newPkg1Freq))
                    }
                )

                DropdownSelector(
                    label = "Pkg1 Q",
                    selectedItem = fx.pkg1Q,
                    items = EqQ.entries.toList(),
                    onItemSelected = { newPkg1Q ->
                        onFxChange(fx.copy(pkg1Q = newPkg1Q))
                    }
                )

                SliderWithLabel(
                    label = "Pkg1 Gain",
                    value = fx.pkg1Gain,
                    onValueChange = { newPkg1Gain ->
                        onFxChange(fx.copy(pkg1Gain = newPkg1Gain))
                    },
                    valueRange = -15f..15f, bipolar = true
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                DropdownSelector(
                    label = "Pkg2 Frequency",
                    selectedItem = fx.pkg2Freq,
                    items = EqFreq.entries.toList(),
                    onItemSelected = { newPkg2Freq ->
                        onFxChange(fx.copy(pkg2Freq = newPkg2Freq))
                    }
                )

                DropdownSelector(
                    label = "Pkg2 Q",
                    selectedItem = fx.pkg2Q,
                    items = EqQ.entries.toList(),
                    onItemSelected = { newPkg2Q ->
                        onFxChange(fx.copy(pkg2Q = newPkg2Q))
                    }
                )

                SliderWithLabel(
                    label = "Pkg2 Gain",
                    value = fx.pkg2Gain,
                    onValueChange = { newPkg2Gain ->
                        onFxChange(fx.copy(pkg2Gain = newPkg2Gain))
                    },
                    valueRange = -15f..15f, bipolar = true
                )
            }
        }

        FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xl)) {
            SliderWithLabel(
                label = "Level",
                value = fx.level,
                onValueChange = { newLevel ->
                    onFxChange(fx.copy(level = newLevel))
                },
                valueRange = 0f..100f
            )
        }
    }
}