package com.dishdiscoverers.core.usecase

import com.dishdiscoverers.core.repository.RecipeRepository

class getSearchedRecipesUseCase(private val recipeRepository: RecipeRepository) {
    suspend fun execute(searchQuery: String) = recipeRepository.getsearchedRecipes(searchQuery)
}