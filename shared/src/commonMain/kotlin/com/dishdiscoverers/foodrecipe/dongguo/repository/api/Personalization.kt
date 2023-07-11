package com.dishdiscoverers.foodrecipe.dongguo.repository.api


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Personalization(
    @SerialName("to")
    val to: List<To?>?
)