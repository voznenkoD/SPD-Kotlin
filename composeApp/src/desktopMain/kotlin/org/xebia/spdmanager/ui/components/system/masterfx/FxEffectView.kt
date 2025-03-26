package org.xebia.spdmanager.ui.components.system.masterfx

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xebia.spdmanager.model.system.fx.subtypes.FxEffect

@Composable
fun FxEffectView(fx: FxEffect) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("FX Type: ${fx.fxType}", fontSize = 16.sp)
        fx.renderParameters()
    }
}