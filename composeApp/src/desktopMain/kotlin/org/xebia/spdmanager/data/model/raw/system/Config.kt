package org.xebia.spdmanager.data.model.raw.system

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "Root")
data class Config(
    @param:JacksonXmlProperty(localName = "SetupPrm") @get:JacksonXmlProperty(localName = "SetupPrm") val setupPrm: SetupPrm,
    @param:JacksonXmlProperty(localName = "SysPrm") @get:JacksonXmlProperty(localName = "SysPrm") val sysPrm: SysPrm,
    @param:JacksonXmlProperty(localName = "KitChainPrm") @get:JacksonXmlProperty(localName = "KitChainPrm") val kitChainPrm: KitChainPrm,
    @param:JacksonXmlProperty(localName = "MEfctPrm") @get:JacksonXmlProperty(localName = "MEfctPrm") val mEfctPrm: MEfctPrm
)
