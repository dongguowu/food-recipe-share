package com.dishdiscoverers.foodrecipe.dongguo.repository.api

import com.dishdiscoverers.foodrecipe.dongguo.repository.MealsResponse
import com.dishdiscoverers.foodrecipe.dongguo.repository.Recipe
import com.dishdiscoverers.foodrecipe.dongguo.repository.Resource
import com.dishdiscoverers.foodrecipe.dongguo.repository.json.convertMealRecipe
import com.dishdiscoverers.foodrecipe.dongguo.repository.json.getIngredientsFromTheMealRecipe
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class TheMealdbApi(private val client: HttpClient) {
    val END_POINT_GET_RECIPE = "https://www.themealdb.com/api/json/v1/1/lookup.php?i="

    suspend fun getRecipe(recipeId: String): Resource<Recipe> {
        val urlString = "$END_POINT_GET_RECIPE$recipeId"
        val result: MealsResponse = client.get(urlString).body() ?: return Resource.Failure(Exception("Not found"))
        val recipeTheMeal = result.meals?.firstOrNull() ?: return Resource.Failure(Exception("Not found"))
        val ingredients = getIngredientsFromTheMealRecipe(recipeTheMeal)
        val recipe = convertMealRecipe(recipeTheMeal, ingredients)

        return if (recipe != null) {
            Resource.Success(recipe)
        } else {
            Resource.Failure(Exception("Conversion failed"))
        }
    }
}

