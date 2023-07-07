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
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Singleton object that provides methods for interacting with the Meal API to retrieve recipes, categories, and ingredients.
 */
object RecipeServiceTheMealSingleton {
    private val THE_MEAL_API_SERVER = "https://www.themealdb.com/api/json/v1/1/"
    private val clientSingleton = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            level = LogLevel.NONE
            logger = object : Logger {
                override fun log(message: String) {
                    Napier.v(tag = "HTTP Client", message = message)
                }
            }
        }
    }

    /**
     * Retrieves a recipe from the Meal API based on the provided recipe ID.
     *
     * @param recipeId The ID to search for in the Meal API.
     * @return A [Resource] containing a [Recipe] retrieved from the Meal API matching the provided ID, or a [Resource.Failure] if an error occurs or no recipe is found.
     */
    suspend fun getRecipe(recipeId: String): Resource<Recipe> {
        val urlString = "${THE_MEAL_API_SERVER}lookup.php?i=$recipeId"
        return try {
            Resource.Success(
                getRecipesFromTheMealJson(
                    clientSingleton.get(urlString).body()
                ).first()
            )
        } catch (e: RedirectResponseException) {
            // 3xx - responses
            Napier.e { "Error: ${e.response.status.description}" }
            Resource.Failure(e)
        } catch (e: ClientRequestException) {
            // 4xx - responses
            Napier.e { "Error: ${e.response.status.description}" }
            Resource.Failure(e)
        } catch (e: ServerResponseException) {
            // 5xx - response
            Napier.e { "Error: ${e.response.status.description}" }
            Resource.Failure(e)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    /**
     * Retrieves recipes from the Meal API based on the provided title.
     *
     * @param title The title to search for in the Meal API.
     * @return A [Resource] containing a list of [Recipe] retrieved from the Meal API matching the provided title, or a [Resource.Failure] if an error occurs or no recipes are found.
     */
    suspend fun getRecipeByTitle(title: String): Resource<List<Recipe>> {
        val urlString = "${THE_MEAL_API_SERVER}search.php?s=$title"
        return try {
            Resource.Success(getRecipesFromTheMealJson(clientSingleton.get(urlString).body()))
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    /**
     * Retrieves all categories from the Meal API.
     *
     * @return A [Resource] containing a list of [Category] retrieved from the Meal API, or a [Resource.Failure] if an error occurs.
     */
    suspend fun getAllCategory(): Resource<List<Category>> {
        val urlString = "${THE_MEAL_API_SERVER}categories.php"
        return try {
            val categoriesResponse: CategoriesResponse = clientSingleton.get(urlString).body()
            val categories =
                categoriesResponse.categories?.mapNotNull { convertMealCategory(it) } ?: emptyList()
            Resource.Success(categories)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    /**
     * Retrieves all ingredients from the Meal API.
     *
     * @return A [Resource] containing a list of [Ingredient] retrieved from the Meal API, or a [Resource.Failure] if an error occurs.
     */
    suspend fun getAllIngredient(): Resource<List<Ingredient>> {
        val urlString = "${THE_MEAL_API_SERVER}list.php?i=list"
        return try {
            val response: IngredientsResponse = clientSingleton.get(urlString).body()
            val ingredients =
                response.meals?.mapNotNull { convertMealIngredient(it) } ?: emptyList()
            Resource.Success(ingredients)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    /**
     * Closes the underlying HTTP client and releases any allocated resources.
     */
    fun close() {
        clientSingleton.close()
    }
}

