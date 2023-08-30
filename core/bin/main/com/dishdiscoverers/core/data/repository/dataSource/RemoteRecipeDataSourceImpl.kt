package com.dishdiscoverers.core.data.repository.dataSource

import com.dishdiscoverers.core.data.remote.TheMealAPIService
import com.dishdiscoverers.core.domain.model.FoodRecipe
import com.dishdiscoverers.core.data.remote.dto.TheMealAPIMealsResponse
import com.dishdiscoverers.core.common.Resource
import com.dishdiscoverers.core.data.remote.dto.toFoodRecipe
import retrofit2.Response
import java.lang.Exception

class RemoteRecipeDataSourceImpl(
    private val theMealAPIService: TheMealAPIService,
) : RecipeDataSourceInterface {

    override suspend fun searchRecipes(searchQuery: String): List<FoodRecipe> {
        val response = searchTheMealRecipes(searchQuery)
        if (response.isSuccessful) {
            val meals = response.body()?.meals
            if (!meals.isNullOrEmpty()) {
                val parsedRecipes = meals.mapNotNull { meal ->
                    meal.toFoodRecipe()
                }
                return parsedRecipes
            }
        }
        return emptyList()
    }


    private suspend fun searchTheMealRecipes(title: String): Response<TheMealAPIMealsResponse> {
        return theMealAPIService.searchRecipes(title)
    }


}