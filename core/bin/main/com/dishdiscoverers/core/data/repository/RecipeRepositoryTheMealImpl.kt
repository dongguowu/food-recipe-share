package com.dishdiscoverers.core.data.repository

import com.dishdiscoverers.core.data.model.FoodRecipe
import com.dishdiscoverers.core.data.repository.dataSource.RecipeRemoteDataSource
import com.dishdiscoverers.core.data.utility.Resource
import com.dishdiscoverers.core.repository.RecipeRepository

class RecipeRepositoryTheMealImpl(private val dataSource: RecipeRemoteDataSource) :
    RecipeRepository {
    override suspend fun getsearchedRecipes(searchQuery: String): Resource<List<FoodRecipe>> {
        return dataSource.searchRecipes(searchQuery)
    }
}