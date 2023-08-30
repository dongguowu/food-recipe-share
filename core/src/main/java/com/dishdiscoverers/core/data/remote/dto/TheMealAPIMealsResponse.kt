package com.dishdiscoverers.core.data.model.meal

import com.google.gson.annotations.SerializedName

/**
 * Represents the response from the Meal API for retrieving meals.
 * @property meals The list of [RecipeTheMeal] objects returned in the response. Can be null if no meals are found.
 */
data class TheMealAPIMealsResponse(
    @SerializedName("meals")
    val meals: List<TheMealRecipe>? = null
)