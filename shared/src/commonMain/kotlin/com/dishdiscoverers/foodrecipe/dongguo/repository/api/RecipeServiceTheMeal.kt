package com.dishdiscoverers.foodrecipe.dongguo.repository.api

import com.dishdiscoverers.foodrecipe.dongguo.repository.Category
import com.dishdiscoverers.foodrecipe.dongguo.repository.Ingredient
import com.dishdiscoverers.foodrecipe.dongguo.repository.Recipe
import com.dishdiscoverers.foodrecipe.dongguo.repository.Resource
import com.dishdiscoverers.foodrecipe.dongguo.repository.json.CategoriesResponse
import com.dishdiscoverers.foodrecipe.dongguo.repository.json.IngredientsResponse
import com.dishdiscoverers.foodrecipe.dongguo.repository.json.convertMealCategory
import com.dishdiscoverers.foodrecipe.dongguo.repository.json.convertMealIngredient
import com.dishdiscoverers.foodrecipe.dongguo.repository.json.getRecipesFromTheMealJson
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class RecipeServiceTheMeal(private val client: HttpClient) {
    private val THE_MEAL_API_SERVER = "https://www.themealdb.com/api/json/v1/1/"

    /**
     * Retrieve recipe from the Meal API based on the provided title.
     * @param recipeId The id to search for in the Meal API.
     * @return A [Recipe] retrieved from the Meal API matching the provided id, or fail if an error occurs or no recipe are found.
     */
    suspend fun getRecipe(recipeId: String): Resource<Recipe> {
        val urlString = "${THE_MEAL_API_SERVER}lookup.php?i=$recipeId"
        return try {
            Resource.Success(getRecipesFromTheMealJson(client.get(urlString).body()).first())
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    /**
     * Retrieves recipes from the Meal API based on the provided title.
     * @param title The title to search for in the Meal API.
     * @return A list of [Recipe] retrieved from the Meal API matching the provided title, or null if an error occurs or no recipes are found.
     */
    suspend fun getRecipeByTitle(title: String): Resource<List<Recipe>> {
        val urlString = "${THE_MEAL_API_SERVER}search.php?s=$title"
        return try {
            Resource.Success(getRecipesFromTheMealJson(client.get(urlString).body()))
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    suspend fun getAllCategory(): Resource<List<Category>> {
        val urlString = "${THE_MEAL_API_SERVER}categories.php"
        return try {
            val categoriesResponse: CategoriesResponse = client.get(urlString).body()
            val categories =
                categoriesResponse.categories?.mapNotNull { convertMealCategory(it) } ?: emptyList()
            Resource.Success(categories)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    suspend fun getAllIngredient(): Resource<List<Ingredient>> {
        val urlString = "${THE_MEAL_API_SERVER}list.php?i=list"
        return try {
            val response: IngredientsResponse = client.get(urlString).body()
            val ingredients =
                response.meals?.mapNotNull { convertMealIngredient(it) } ?: emptyList()
            Resource.Success(ingredients)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }
}

