package com.dishdiscoverers.foodrecipe.dongguo.repository.api.email


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class To(
    @SerialName("email")
    val email: String?
)