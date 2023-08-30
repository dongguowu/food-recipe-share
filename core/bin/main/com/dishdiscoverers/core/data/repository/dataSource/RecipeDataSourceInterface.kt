package com.dishdiscoverers.core.data.repository.dataSource

import com.dishdiscoverers.core.domain.model.FoodRecipe
import com.dishdiscoverers.core.common.Resource

interface RecipeDataSourceInterface {
    suspend fun searchRecipes(searchQuery: String): List<FoodRecipe>
}