package com.dishdiscoverers.foodrecipe.dongguo

interface TheMealRepository {
    suspend fun getRecipes(title: String): Resource<List<RecipeTheMeal>>
}