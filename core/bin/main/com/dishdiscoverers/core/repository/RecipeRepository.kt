package com.dishdiscoverers.core.repository

import com.dishdiscoverers.core.data.Recipe

class RecipeRepository(private val dataSource: RecipeDataSource) {
    suspend fun addRecipe(recipe: Recipe) = dataSource.add(recipe)
    suspend fun getRecipe(id: String) = dataSource.get(id)
    suspend fun getAllRecipes() = dataSource.getAll()
    suspend fun findRecipes(title: String) = dataSource.findRecipes(title)
}