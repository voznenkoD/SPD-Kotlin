package org.xebia.spdmanager.ui.components.system.masterfx

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.model.system.fx.subtypes.FXType
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect
import org.xebia.spdmanager.ui.components.common.DropdownSelector

@Composable
fun FxEffectView(
    fx: FxEffect,
    onFxChange: (FxEffect) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        DropdownSelector(
            label = "FX Type",
            selectedItem = fx.fxType,
            onItemSelected = { newType ->
                val newFx = FxEffect.fromValues(newType.value, List(20) { 0 })
                onFxChange(newFx)
            },
            items = FXType.entries.toList(),
            width = 200.dp
        )

        Spacer(modifier = Modifier.height(8.dp))

        fx.renderEditableParameters(onFxChange)
    }
}
