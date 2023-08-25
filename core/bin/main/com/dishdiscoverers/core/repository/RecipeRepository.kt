package com.dishdiscoverers.core.repository

import com.dishdiscoverers.core.data.RecipeResource

class RecipeRepository(private val dataSource: RecipeDataSource) {
    suspend fun addRecipe(recipeResource: RecipeResource) = dataSource.add(recipeResource)
    suspend fun getRecipe(id: String) = dataSource.get(id)
    suspend fun getAllRecipes() = dataSource.getAll()
    suspend fun findRecipes(title: String) = dataSource.findRecipes(title)
}