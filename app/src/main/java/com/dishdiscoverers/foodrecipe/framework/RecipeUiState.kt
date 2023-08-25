package com.dishdiscoverers.foodrecipe.framework

import com.dishdiscoverers.core.data.SaveableRecipeResource

sealed interface RecipeUiState {
    object Loading : RecipeUiState
    data class Success(val recipeResources : List<SaveableRecipeResource>)
}