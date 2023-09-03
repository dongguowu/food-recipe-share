package com.dishdiscoverers.foodrecipe.presentation.recipe_list

import com.dishdiscoverers.core.domain.model.FoodRecipe

data class RecipeListState(
    val isLoading: Boolean = false,
    val recipes: List<FoodRecipe> = emptyList(),
    val error: String = "",
)
