package org.xebia.spdmanager.data.model.raw.system

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "WvListSortbyName")
data class WvListSortbyName(
    @JacksonXmlElementWrapper(useWrapping = false)
    @param:JacksonXmlProperty(localName = "Wv") @get:JacksonXmlProperty(localName = "Wv")
    val wvList: List<Int>
)

