package org.xebia.spdmanager.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.xebia.spdmanager.model.kit.pad.PadNumber
import org.xebia.spdmanager.model.setup.SetupConfig
import org.xebia.spdmanager.model.setup.IntPad
import org.xebia.spdmanager.model.setup.ExtPad
import org.xebia.spdmanager.model.setup.FootSwitchPolarity
import org.xebia.spdmanager.model.setup.InputMode
import org.xebia.spdmanager.model.setup.TrigType
import org.xebia.spdmanager.model.setup.VeloCurve
import org.xebia.spdmanager.model.system.PadFsControl
import org.xebia.spdmanager.service.DeviceManager

class SetupViewModel(private val deviceManager: DeviceManager) {

    private val device = deviceManager.device

    private val _setupConfig = MutableStateFlow(
        device?.setupConfig?.let { config ->
            config.copy(
                intPads = config.intPads.map { it.copy() },
                extPads = config.extPads.map { it.copy() }
            )
        }
    )
    val setupConfig: StateFlow<SetupConfig?> = _setupConfig.asStateFlow()

    private val _padFsControl = MutableStateFlow(
        device?.systemConfig?.padFsControl?.toMap()
    )
    val padFsControl: StateFlow<Map<PadNumber, PadFsControl>?> = _padFsControl.asStateFlow()

    private val _selectedPadNumber = MutableStateFlow(PadNumber.PAD_1)
    val selectedPadNumber: StateFlow<PadNumber> = _selectedPadNumber.asStateFlow()

    /**
     * Select a pad for editing
     */
    fun selectPad(padNumber: PadNumber) {
        _selectedPadNumber.value = padNumber
    }

    /**
     * Update the entire setup configuration
     * This is called when any part of the setup config changes
     */
    fun updateSetupConfig(newSetupConfig: SetupConfig) {
        _setupConfig.value = newSetupConfig

        device?.let { currentDevice ->
            val updatedDevice = currentDevice.copy(setupConfig = newSetupConfig)
            deviceManager.device = updatedDevice
        }
    }

    /**
     * Update a specific IntPad
     */
    fun updateIntPad(padIndex: Int, updatedPad: IntPad) {
        _setupConfig.value?.let { config ->
            if (padIndex in config.intPads.indices) {
                val updatedPads = config.intPads.toMutableList()
                updatedPads[padIndex] = updatedPad
                updateSetupConfig(config.copy(intPads = updatedPads))
            }
        }
    }

    /**
     * Update a specific ExtPad
     */
    fun updateExtPad(padIndex: Int, updatedPad: ExtPad) {
        _setupConfig.value?.let { config ->
            if (padIndex in config.extPads.indices) {
                val updatedPads = config.extPads.toMutableList()
                updatedPads[padIndex] = updatedPad
                updateSetupConfig(config.copy(extPads = updatedPads))
            }
        }
    }

    /**
     * Update foot switch 1 polarity
     */
    fun updateFs1Polarity(polarity: FootSwitchPolarity) {
        _setupConfig.value?.let { config ->
            updateSetupConfig(config.copy(fs1Polarity = polarity))
        }
    }

    /**
     * Update foot switch 2 polarity
     */
    fun updateFs2Polarity(polarity: FootSwitchPolarity) {
        _setupConfig.value?.let { config ->
            updateSetupConfig(config.copy(fs2Polarity = polarity))
        }
    }

    /**
     * Update the control type for a specific pad
     */
    fun updatePadFsControl(padNumber: PadNumber, control: PadFsControl) {
        _padFsControl.value?.let { currentControls ->
            val updatedControls = currentControls.toMutableMap()
            updatedControls[padNumber] = control
            _padFsControl.value = updatedControls

            device?.let { currentDevice ->
                val updatedSystemConfig = currentDevice.systemConfig.copy(
                    padFsControl = updatedControls
                )
                deviceManager.updateSystemConfig(updatedSystemConfig)
            }
        }
    }

    /**
     * Get the current IntPad for the selected pad (if it's an internal pad)
     */
    fun getCurrentIntPad(): IntPad? {
        val padNumber = _selectedPadNumber.value
        return if (padNumber in PadNumber.PAD_1..PadNumber.PAD_9) {
            val index = padNumber.ordinal
            _setupConfig.value?.intPads?.getOrNull(index)
        } else null
    }

    /**
     * Get the current ExtPad for the selected pad (if it's an external pad)
     */
    fun getCurrentExtPad(): ExtPad? {
        val padNumber = _selectedPadNumber.value
        return if (padNumber in PadNumber.TRIG_1..PadNumber.TRIG_4) {
            val index = padNumber.ordinal - PadNumber.TRIG_1.ordinal
            _setupConfig.value?.extPads?.getOrNull(index)
        } else null
    }

    /**
     * Get the FS control for the currently selected pad
     */
    fun getCurrentPadFsControl(): PadFsControl? {
        return _padFsControl.value?.get(_selectedPadNumber.value)
    }
}