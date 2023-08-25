package com.dishdiscoverers.core.usecase

import com.dishdiscoverers.core.repository.RecipeRepository

class GetAllRecipes(private val recipeRepository: RecipeRepository) {
    suspend fun invoke() = recipeRepository.getAllRecipes()
}