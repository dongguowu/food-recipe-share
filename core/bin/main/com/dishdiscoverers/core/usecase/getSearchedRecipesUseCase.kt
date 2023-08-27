package com.dishdiscoverers.core.usecase

import com.dishdiscoverers.core.repository.RecipeRepository

class GetSearchedRecipesUseCase(private val recipeRepository: RecipeRepository) {
    suspend fun execute(searchQuery: String) = recipeRepository.getSearchedRecipes(searchQuery)
}