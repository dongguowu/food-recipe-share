package com.dishdiscoverers.core.repository

import com.dishdiscoverers.core.data.model.FoodRecipe
import com.dishdiscoverers.core.data.utility.Resource

interface RecipeRepository {
    suspend fun getSearchedRecipes(searchQuery: String) : Resource<List<FoodRecipe>>
}