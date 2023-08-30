package com.dishdiscoverers.core.domain.model

data class SavableFoodRecipe(
    val foodRecipe: FoodRecipe,
    val isBookmarked: Boolean
)