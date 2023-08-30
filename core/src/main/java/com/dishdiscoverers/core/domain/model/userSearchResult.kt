package com.dishdiscoverers.core.data.model

data class userSearchResult(
    val userId: String,
    val recipesList: List<FoodRecipe> = emptyList()
)