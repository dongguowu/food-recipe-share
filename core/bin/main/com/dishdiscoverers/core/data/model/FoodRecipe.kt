package com.dishdiscoverers.core.data.model

import com.google.gson.annotations.SerializedName

/**
 * Represents a food recipe.
 * @property id The unique identifier of the recipe.
 * @property title The title of the recipe.
 * @property servings The number of servings the recipe yields.
 * @property instructions The instructions to prepare the recipe.
 * @property imageUrl The URL of the image associated with the recipe.
 * @property ingredients A string representation of the ingredients used in the recipe. Can be null.
 */
data class FoodRecipe(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("servings")
    val servings: Int,
    @SerializedName("instructions")
    val instructions: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("ingredients")
    val ingredients: String? = null,
)