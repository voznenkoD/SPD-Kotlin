package org.xebia.spdmanager.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.xebia.spdmanager.model.KitChain
import org.xebia.spdmanager.model.Wave
import org.xebia.spdmanager.model.kit.Kit
import org.xebia.spdmanager.model.system.ClickConfig
import org.xebia.spdmanager.model.system.SystemAudioConfig
import org.xebia.spdmanager.model.system.SystemConfig
import org.xebia.spdmanager.model.system.fx.MasterEffectConfig
import org.xebia.spdmanager.model.system.vControl.VisualControl
import org.xebia.spdmanager.service.DeviceManager

class SystemViewModel(private val deviceManager: DeviceManager) {
    private val device = deviceManager.device

    private val _systemConfig = MutableStateFlow(device?.systemConfig)
    val systemConfig: StateFlow<SystemConfig?> = _systemConfig.asStateFlow()

    private val _waves = MutableStateFlow(device?.waves ?: emptyList())
    val waves: StateFlow<List<Wave>> = _waves.asStateFlow()

    private val _kits = MutableStateFlow(device?.kits ?: emptyList())
    val kits: StateFlow<List<Kit>> = _kits.asStateFlow()

    fun updateClickConfig(newClickConfig: ClickConfig) {
        _systemConfig.value?.let { config ->
            _systemConfig.value = config.copy(clickConfig = newClickConfig)
            deviceManager.updateSystemConfig(_systemConfig.value!!)
        }
    }

    fun updateVisualControl(newVisualControl: VisualControl) {
        _systemConfig.value?.let { config ->
            _systemConfig.value = config.copy(visualControl = newVisualControl)
            deviceManager.updateSystemConfig(_systemConfig.value!!)
        }
    }

    fun updateMasterEffectConfig(newMasterEffect: MasterEffectConfig) {
        _systemConfig.value?.let { config ->
            _systemConfig.value = config.copy(masterEffectConfig = newMasterEffect)
            deviceManager.updateSystemConfig(_systemConfig.value!!)
        }
    }

    fun updateSystemAudioConfig(newAudioConfig: SystemAudioConfig) {
        _systemConfig.value?.let { config ->
            _systemConfig.value = config.copy(systemAudioConfig = newAudioConfig)
            deviceManager.updateSystemConfig(_systemConfig.value!!)
        }
    }

    fun updateKitChains(newKitChains: Map<Char, KitChain>) {
        _systemConfig.value?.let { config ->
            _systemConfig.value = config.copy(kitChains = newKitChains)
            deviceManager.updateSystemConfig(_systemConfig.value!!)
        }
    }
}