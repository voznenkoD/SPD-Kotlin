package org.xebia.spdmanager.data.model.raw.system

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "TagList")
data class TagList(
    @JacksonXmlElementWrapper(useWrapping = false)
    @param:JacksonXmlProperty(localName = "TagPrm") @get:JacksonXmlProperty(localName = "TagPrm")
    val tagList: List<TagPrm>
)

