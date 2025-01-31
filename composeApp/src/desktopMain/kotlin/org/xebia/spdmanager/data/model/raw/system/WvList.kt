package org.xebia.spdmanager.data.model.raw.system

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "WvList")
data class WvList(
    @JacksonXmlProperty(localName = "Wv")
    @JacksonXmlElementWrapper(useWrapping = false)
    val wvList: List<Int?> = emptyList()
)

