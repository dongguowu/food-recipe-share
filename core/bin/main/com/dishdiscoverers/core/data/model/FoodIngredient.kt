package com.dishdiscoverers.core.data.model

import com.google.gson.annotations.SerializedName


/**
 * Represents a food ingredient.
 * @property id The unique identifier of the ingredient.
 * @property name The name of the ingredient.
 * @property category The category of the ingredient. Can be null.
 * @property unit The unit of measurement for the ingredient.
 * @property imageUrl The URL of the image associated with the ingredient. Can be null.
 * @property description Description. Can be null.
 */
data class Ingredient(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("category")
    val category: String? = null,
    @SerializedName("unit")
    val unit: String? = null,
    @SerializedName("imageUrl")
    val imageUrl: String? = null,
    @SerializedName("description")
    val description: String? = null,
)