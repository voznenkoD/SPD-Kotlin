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

    /**
     * Applies [transform] to the chain [chainKey] and persists the result. Returning the same chain
     * (or null guards in the caller) makes it a no-op. Centralizes the lookup/copy/persist boilerplate
     * shared by the chain mutators.
     */
    private inline fun updateChain(chainKey: Char, transform: (KitChain) -> KitChain) {
        val config = _systemConfig.value ?: return
        val chain = config.kitChains[chainKey] ?: return
        val newChains = config.kitChains.toMutableMap()
        newChains[chainKey] = transform(chain)
        updateKitChains(newChains)
    }

    fun setKitInChain(chainKey: Char, index: Int, newRef: Int) {
        updateChain(chainKey) { chain ->
            if (index !in chain.kitRefs.indices) return@setKitInChain
            chain.copy(kitRefs = chain.kitRefs.toMutableList().apply { this[index] = newRef })
        }
    }

    /**
     * Resets every slot of the chain [chainKey] to "No Kit", preserving the chain's name and leaving
     * all other chains untouched. The slot count is preserved.
     */
    fun initializeKitChain(chainKey: Char) {
        updateChain(chainKey) { chain -> chain.copy(kitRefs = chain.kitRefs.map { KitChain.NO_KIT }) }
    }

    fun moveKitInChain(chainKey: Char, from: Int, to: Int) {
        updateChain(chainKey) { chain ->
            if (from !in chain.kitRefs.indices || to !in chain.kitRefs.indices || from == to) return@moveKitInChain
            val refs = chain.kitRefs.toMutableList()
            refs.add(to, refs.removeAt(from))
            chain.copy(kitRefs = refs)
        }
    }
}