package com.dishdiscoverers.core.repository

import com.dishdiscoverers.core.data.RecipeResource

interface RecipeDataSource {
    suspend fun add(recipeResource: RecipeResource)
    suspend fun get(id: String): RecipeResource?
    suspend fun getAll(): List<RecipeResource>
    suspend fun findRecipes(title: String): List<RecipeResource>
}