package com.dishdiscoverers.core.repository

import com.dishdiscoverers.core.data.model.FoodRecipe
import com.dishdiscoverers.core.data.utility.Resource

interface RecipeRepository {
    suspend fun getsearchedRecipes(searchQuery: String) : Resource<List<FoodRecipe>>
}