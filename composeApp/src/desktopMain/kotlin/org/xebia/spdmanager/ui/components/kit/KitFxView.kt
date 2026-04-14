package org.xebia.spdmanager.ui.components.kit

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.ui.theme.*
import org.xebia.spdmanager.ui.theme.Typography
import org.xebia.spdmanager.model.kit.fx.KitFX
import org.xebia.spdmanager.model.system.fx.common.SyncSwitch
import org.xebia.spdmanager.model.system.fx.subtypes.FXType
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect
import org.xebia.spdmanager.ui.components.common.DropdownSelector

@Composable
fun KitFXView(
    kitFX: KitFX,
    onFxChange: (KitFX) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("FX Enabled", fontSize = Typography.titleSize)
            Switch(
                checked = kitFX.sw == SyncSwitch.ON,
                onCheckedChange = { isEnabled ->
                    onFxChange(
                        kitFX.copy(
                            sw = if (isEnabled) SyncSwitch.ON else SyncSwitch.OFF
                        )
                    )
                },
                colors = SwitchDefaults.colors(
                    checkedTrackColor = ColorAccentOrange,
                    uncheckedTrackColor = ColorDivider,
                    checkedThumbColor = ColorBackground
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (kitFX.sw == SyncSwitch.OFF) {
            Text(
                "FX Disabled",
                fontSize = Typography.bodySize,
                color = ColorTextSecondary
            )
        } else {
            DropdownSelector(
                label = "FX Type",
                selectedItem = kitFX.fx.fxType,
                onItemSelected = { newType ->
                    val newFx = FxEffect.fromValues(newType.value, List(20) { 0 })
                    onFxChange(kitFX.copy(fx = newFx))
                },
                items = FXType.entries.toList(),
                width = 200.dp
            )

            Spacer(modifier = Modifier.height(8.dp))

            kitFX.fx.renderEditableParameters { updatedFx ->
                onFxChange(kitFX.copy(fx = updatedFx))
            }
        }
    }
}