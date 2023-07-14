package com.dishdiscoverers.foodrecipe.dongguo.repository.api.email


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GridEmail(
    @SerialName("content")
    val content: List<Content?>?,
    @SerialName("from")
    val from: From?,
    @SerialName("personalizations")
    val personalizations: List<Personalization?>?,
    @SerialName("subject")
    val subject: String?
)