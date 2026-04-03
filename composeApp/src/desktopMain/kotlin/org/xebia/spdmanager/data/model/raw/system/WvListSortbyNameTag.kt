package org.xebia.spdmanager.data.model.raw.system

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "WvListSortbyName_Tag")
data class WvListSortbyNameTag(
    @JacksonXmlElementWrapper(useWrapping = false)
    @param:JacksonXmlProperty(localName = "WvList") @get:JacksonXmlProperty(localName = "WvList")
    val wvList: List<WvList>
)


