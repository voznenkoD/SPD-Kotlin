package org.xebia.spdmanager.data.model.raw.system

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "WvListSortbyNum_Tag")
data class WvListSortbyNumTag(
    @JacksonXmlProperty(localName = "WvList")
    @JacksonXmlElementWrapper(useWrapping = false)
    val wvList: List<WvList>
)

