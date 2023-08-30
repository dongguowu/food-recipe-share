package com.dishdiscoverers.core.repository

import com.dishdiscoverers.core.data.model.FoodRecipe
import com.dishdiscoverers.core.common.Resource

interface RecipeRepository {
    suspend fun getSearchedRecipes(searchQuery: String) : Resource<List<FoodRecipe>>
}