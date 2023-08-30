package com.dishdiscoverers.core.data.model

import com.google.gson.annotations.SerializedName

/**
 * Represents a food category.
 * @property id The unique identifier of the category.
 * @property name The name of the category.
 * @property imageUrl The URL of the image associated with the category.
 * @property description The description of the category.
 */
data class FoodCategory(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("description")
    val description: String,
)