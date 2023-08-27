package com.dishdiscoverers.core.data.repository

import com.dishdiscoverers.core.data.model.FoodRecipe
import com.dishdiscoverers.core.data.repository.dataSource.RecipeDataSource
import com.dishdiscoverers.core.data.utility.Resource
import com.dishdiscoverers.core.repository.RecipeRepository

class RecipeRepositoryDateSourceImpl(private val dataSource: RecipeDataSource) :
    RecipeRepository {
    override suspend fun getSearchedRecipes(searchQuery: String): Resource<List<FoodRecipe>> {
        return dataSource.searchRecipes(searchQuery)
    }
}