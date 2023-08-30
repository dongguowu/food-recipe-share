package com.dishdiscoverers.core.data.repository

import com.dishdiscoverers.core.domain.model.FoodRecipe
import com.dishdiscoverers.core.data.repository.dataSource.RecipeDataSourceInterface
import com.dishdiscoverers.core.common.Resource
import com.dishdiscoverers.core.domain.repository.RecipeRepository
import jakarta.inject.Inject


class RecipeRepositoryDateSourceImpl @Inject constructor(private var dataSource: RecipeDataSourceInterface) :
    RecipeRepository {

    override suspend fun getSearchedRecipes(searchQuery: String): List<FoodRecipe> {
        return dataSource.searchRecipes(searchQuery)
    }
}