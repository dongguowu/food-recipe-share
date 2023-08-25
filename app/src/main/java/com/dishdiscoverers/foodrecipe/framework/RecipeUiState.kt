package com.dishdiscoverers.foodrecipe.framework

import com.dishdiscoverers.core.data.model.SaveableRecipeResource

sealed interface RecipeUiState {
    object Loading : RecipeUiState
    data class Success(val recipeResources : List<SaveableRecipeResource>)
}