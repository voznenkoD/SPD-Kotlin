package org.xebia.spdmanager.model

data class Device(
    val kits: List<Kit>,
    val waves: List<Wave>,
    val kitChains: List<KitChain>,
    val globalConfig: GlobalConfig,
    val systemConfig: SystemConfig,
    val fxConfig: FXConfig
)
