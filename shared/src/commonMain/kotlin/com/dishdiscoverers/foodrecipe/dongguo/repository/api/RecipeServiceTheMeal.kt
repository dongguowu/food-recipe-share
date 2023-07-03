package com.dishdiscoverers.foodrecipe.dongguo.repository.api

import com.dishdiscoverers.foodrecipe.dongguo.repository.MealsResponse
import com.dishdiscoverers.foodrecipe.dongguo.repository.Recipe
import com.dishdiscoverers.foodrecipe.dongguo.repository.Resource
import com.dishdiscoverers.foodrecipe.dongguo.repository.json.convertMealRecipe
import com.dishdiscoverers.foodrecipe.dongguo.repository.json.getIngredientsFromTheMealRecipe
import com.dishdiscoverers.foodrecipe.dongguo.repository.json.getRecipesFromTheMealJson
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class RecipeServiceTheMeal(private val client: HttpClient) {
    private val THE_MEAL_API_SERVER = "https://www.themealdb.com/api/json/v1/1/"

    suspend fun getRecipe(recipeId: String): Resource<Recipe> {
        val urlString = "${THE_MEAL_API_SERVER}lookup.php?i=$recipeId"
        val result: MealsResponse =
            client.get(urlString).body() ?: return Resource.Failure(Exception("Not found"))
        val recipeTheMeal =
            result.meals?.firstOrNull() ?: return Resource.Failure(Exception("Not found"))
        val ingredients = getIngredientsFromTheMealRecipe(recipeTheMeal)
        val recipe = convertMealRecipe(recipeTheMeal, ingredients)

        return if (recipe != null) {
            Resource.Success(recipe)
        } else {
            Resource.Failure(Exception("Conversion failed"))
        }
    }

    /**
     * Retrieves recipes from the Meal API based on the provided title.
     * @param title The title to search for in the Meal API.
     * @return A list of [Recipe] retrieved from the Meal API matching the provided title, or null if an error occurs or no recipes are found.
     */
    suspend fun getRecipeByTitle(title: String): Resource<List<Recipe>> {
        val urlString = "${THE_MEAL_API_SERVER}search.php?s=$title"
//        val results: MealsResponse =
//            client.get(urlString).body() ?: return Resource.Failure(Exception("Not found"))
//
//        val mutableList: MutableList<Recipe> = mutableListOf()
//        for (item in results.meals ?: emptyList()) {
//            val ingredients = getIngredientsFromTheMealRecipe(item)
//            convertMealRecipe(item, ingredients)?.let { mutableList.add(it) }
//        }
//
//        return Resource.Success(mutableList)

        return try {
            Resource.Success(getRecipesFromTheMealJson(client.get(urlString).body()))
        }catch (e: Exception) {
            Resource.Failure(e)
        }
    }
}

