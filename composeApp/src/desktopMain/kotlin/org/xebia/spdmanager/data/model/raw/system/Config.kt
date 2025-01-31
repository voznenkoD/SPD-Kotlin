package org.xebia.spdmanager.data.model.raw.system

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "Root")
data class Config(
    @JacksonXmlProperty(localName = "SetupPrm") val setupPrm: SetupPrm,
    @JacksonXmlProperty(localName = "SysPrm") val sysPrm: SysPrm,
    @JacksonXmlProperty(localName = "KitChainPrm") val kitChainPrm: KitChainPrm,
    @JacksonXmlProperty(localName = "MEfctPrm") val mEfctPrm: MEfctPrm
)
