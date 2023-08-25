package com.dishdiscoverers.core.data.repository.dataSource

import com.dishdiscoverers.core.data.model.FoodRecipe
import com.dishdiscoverers.core.data.utility.Resource

interface RecipeRemoteDataSource {
    suspend fun searchRecipes(searchQuery: String): Resource<List<FoodRecipe>>
}