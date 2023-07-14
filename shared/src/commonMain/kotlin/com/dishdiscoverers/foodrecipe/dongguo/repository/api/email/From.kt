package com.dishdiscoverers.foodrecipe.dongguo.repository.api.email


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class From(
    @SerialName("email")
    val email: String?
)