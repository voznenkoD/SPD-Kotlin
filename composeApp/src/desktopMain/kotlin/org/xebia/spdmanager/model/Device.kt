package org.xebia.spdmanager.model

import org.xebia.spdmanager.model.kit.Kit
import org.xebia.spdmanager.model.list.WaveListsHolder
import org.xebia.spdmanager.model.setup.SetupConfig
import org.xebia.spdmanager.model.system.SystemConfig

data class Device(
    val setupConfig: SetupConfig,
    val systemConfig: SystemConfig,
    val kits: List<Kit>,
    val waves: List<Wave>,
    val waveLists: WaveListsHolder,
    val rootPath: String
)
