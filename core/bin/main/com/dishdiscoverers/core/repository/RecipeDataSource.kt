package com.dishdiscoverers.core.repository

import com.dishdiscoverers.core.data.Recipe

interface RecipeDataSource {
    suspend fun add(recipe: Recipe)
    suspend fun get(id: String): Recipe?
    suspend fun getAll(): List<Recipe>
    suspend fun findRecipes(title: String): List<Recipe>
}