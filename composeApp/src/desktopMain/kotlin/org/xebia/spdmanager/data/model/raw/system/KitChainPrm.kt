package org.xebia.spdmanager.data.model.raw.system

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class KitChainPrm(
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "KitChain")
    val kitChains: Array<KitChain>
)

