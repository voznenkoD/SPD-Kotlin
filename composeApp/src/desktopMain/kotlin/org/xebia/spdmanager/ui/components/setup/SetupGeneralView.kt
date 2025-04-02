package org.xebia.spdmanager.ui.components.setup

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.xebia.spdmanager.model.setup.SetupConfig
import org.xebia.spdmanager.model.setup.PadIndication
import org.xebia.spdmanager.model.setup.DispMode
import org.xebia.spdmanager.model.setup.AutoOff
import org.xebia.spdmanager.model.setup.UsbMidiMode
import org.xebia.spdmanager.model.system.fx.common.SyncSwitch
import org.xebia.spdmanager.ui.components.common.DropdownSelector
import org.xebia.spdmanager.ui.components.common.SliderWithLabel
import org.xebia.spdmanager.ui.components.common.SwitchWithLabel

@Composable
fun SetupGeneralView(setupConfig: SetupConfig, onUpdate: (SetupConfig) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        SliderWithLabel(
            label = "LCD Contrast",
            value = setupConfig.lcdContrast.toFloat(),
            valueRange = 1f..10f,
            onValueChange = { onUpdate(setupConfig.copy(lcdContrast = it.toInt())) }
        )

        SliderWithLabel(
            label = "LCD Brightness",
            value = setupConfig.lcdBrightness.toFloat(),
            valueRange = 0f..10f,
            onValueChange = { onUpdate(setupConfig.copy(lcdBrightness = it.toInt())) }
        )

        DropdownSelector(
            label = "Pad Indication",
            selectedItem = setupConfig.padIndication,
            items = PadIndication.entries,
            onItemSelected = { onUpdate(setupConfig.copy(padIndication = it)) }
        )

        DropdownSelector(
            label = "Display Mode",
            selectedItem = setupConfig.dispMode,
            items = DispMode.entries,
            onItemSelected = { onUpdate(setupConfig.copy(dispMode = it)) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        SwitchWithLabel(
            label = "Tempo Indication",
            syncSwitch = setupConfig.tempoIndication,
            onValueChange = { onUpdate(setupConfig.copy(tempoIndication = SyncSwitch.fromBoolean(it))) }
        )

        SwitchWithLabel(
            label = "Pad Lock",
            syncSwitch = setupConfig.padLock,
            onValueChange = { onUpdate(setupConfig.copy(padLock = SyncSwitch.fromBoolean(it))) }
        )

        DropdownSelector(
            label = "Auto Power Off",
            selectedItem = setupConfig.autoPowerOff,
            items = AutoOff.entries,
            onItemSelected = { onUpdate(setupConfig.copy(autoPowerOff = it)) }
        )

        DropdownSelector(
            label = "USB Device Mode",
            selectedItem = setupConfig.usbDevMode,
            items = UsbMidiMode.entries,
            onItemSelected = { onUpdate(setupConfig.copy(usbDevMode = it)) }
        )
    }
}
