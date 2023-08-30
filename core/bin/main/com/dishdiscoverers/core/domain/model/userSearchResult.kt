package com.dishdiscoverers.core.domain.model

data class userSearchResult(
    val userId: String,
    val recipesList: List<FoodRecipe> = emptyList()
)