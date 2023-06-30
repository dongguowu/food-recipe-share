package com.dishdiscoverers.foodrecipe.z_showList

import com.dishdiscoverers.foodrecipe.dongguo.repository.RecipeTheMeal
import com.dishdiscoverers.foodrecipe.dongguo.repository.Resource

interface TheMealRepository {
    suspend fun getRecipes(title: String): Resource<List<RecipeTheMeal>>
}