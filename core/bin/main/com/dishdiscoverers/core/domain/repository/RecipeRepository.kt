package com.dishdiscoverers.core.domain.repository

import com.dishdiscoverers.core.domain.model.FoodRecipe
import com.dishdiscoverers.core.common.Resource

interface RecipeRepository {
    suspend fun getSearchedRecipes(searchQuery: String) : List<FoodRecipe>
}