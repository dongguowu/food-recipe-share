package com.dishdiscoverers.foodrecipe.framework

import com.dishdiscoverers.core.data.model.SavableFoodRecipe

sealed interface RecipeUiState {
    object Loading : RecipeUiState
    data class Success(val recipeResources : List<SavableFoodRecipe>)
}