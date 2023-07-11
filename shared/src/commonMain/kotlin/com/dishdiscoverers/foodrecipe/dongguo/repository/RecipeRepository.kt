package com.dishdiscoverers.foodrecipe.dongguo.repository

import com.dishdiscoverers.foodrecipe.dongguo.repository.json.CategoryTheMeal
import com.dishdiscoverers.foodrecipe.dongguo.repository.json.RecipeRepositoryJsonTheMeal
import com.dishdiscoverers.foodrecipe.dongguo.repository.json.RecipeTheMeal
import com.dishdiscoverers.foodrecipe.dongguo.repository.json.convertMealCategory
import com.dishdiscoverers.foodrecipe.dongguo.repository.json.convertMealRecipe
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

const val MEAL_URL = "https://www.themealdb.com/api/json/v1/1/"

/**
 * Interface for accessing recipe-related data.
 */
interface RecipeRepository {
    /**
     * Retrieves all categories.
     * @return A [Resource] containing a list of [Category].
     */
    suspend fun getAllCategory(): Resource<List<Category>>

    /**
     * Retrieves all ingredients.
     * @return A [Resource] containing a list of [Ingredient].
     */
    suspend fun getAllIngredient(): Resource<List<Ingredient>>

    //TODO: update to Resource version
    /**
     * Retrieves recipes filtered by containing the title.
     * @param title The title to be contained.
     * @return A list of [Recipe] matching the provided title.
     */
    suspend fun searchRecipesByTitle(title: String): List<Recipe>

    //TODO: delete getAll, only for debugging
    suspend fun getAllRecipe(): List<Recipe>

    //TODO: below, not implemented.
    suspend fun filterByCategory(category: String): Resource<List<Recipe>>
    suspend fun filterByMainIngredient(ingredient: String): Resource<List<Recipe>>
    suspend fun findRecipeById(id: String): Resource<Recipe>

    suspend fun findAllRecipesByIds(ids: List<String>): List<Recipe>
    suspend fun addRecipe(recipe: Recipe): String?
    suspend fun deleteRecipeById(id: String)
    suspend fun updateRecipeById(id: String, recipeToUpdate: Recipe)
    suspend fun searchRecipesByIngredient(ingredientName: String): List<Recipe>
    suspend fun searchIngredientsByRecipe(recipeName: String): List<String>

    suspend fun findIngredientById(id: String): Ingredient?
    suspend fun findIngredientByName(name: String): List<Ingredient>
    suspend fun findIngredientByIds(ids: List<String>): List<Ingredient>

    suspend fun addIngredient(ingredient: Ingredient): String?
    suspend fun updateIngredientById(id: String, ingredientToUpdate: Ingredient)
    // no delete ingredient function
}

/**
 * Implementation of the [RecipeRepository] interface for retrieving recipes from TheMealAPI.
 */
class RecipeRepositoryTheMealAPI : RecipeRepositoryJsonTheMeal() {
    private val _recipes: MutableList<Recipe> = emptyList<Recipe>().toMutableList()
    private val _categories: MutableList<Category> = mutableListOf()

    //TODO: insert httpClient to save resource
    private val ktorClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            level = LogLevel.ALL
        }
    }

    override suspend fun getAllCategory(): Resource<List<Category>> {
        try {
            val urlString = MEAL_URL + "categories.php"
            val response: CategoriesResponse =
                ktorClient.get(urlString).body() as CategoriesResponse
            _categories.clear()
            for (item in response.categories ?: emptyList()) {
                convertMealCategory(item)?.let { _categories.add(it) }
            }
            return Resource.Success(_categories)
        } catch (e: Exception) {
            e.printStackTrace()
            return Resource.Failure(e)
        }
    }


    override suspend fun getAllRecipe(): List<Recipe> {
        _recipes.clear()
        _recipes.addAll(getRecipeFromTheMealApi("fish") ?: emptyList())
        return _recipes
    }

    override suspend fun searchRecipesByTitle(title: String): List<Recipe> {
        _recipes.clear()
        _recipes.addAll(getRecipeFromTheMealApi(title) ?: emptyList())
        return _recipes
    }

    /**
     * Retrieves recipes from the Meal API based on the provided title.
     * @param title The title to search for in the Meal API.
     * @return A list of [Recipe] retrieved from the Meal API matching the provided title, or null if an error occurs or no recipes are found.
     */
    private suspend fun getRecipeFromTheMealApi(title: String): List<Recipe>? {

        val urlString = MEAL_URL + "search.php?s=$title"
        val results: MealsResponse = ktorClient.get(urlString).body()
        val mutableList: MutableList<Recipe> = mutableListOf()
        for (item in results.meals ?: emptyList()) {

            val ingredients: StringBuilder = StringBuilder()
            for (i in 1..20) {
                val ingredientField = getIngredientField(item, i)
                val measureField = getMeasureField(item, i)
                if (ingredientField?.isNotEmpty() == true && measureField?.isNotEmpty() == true) {
                    ingredients.append("$measureField $ingredientField; ")
                }
            }
            convertMealRecipe(item, ingredients.toString())?.let { mutableList.add(it) }
        }
        return mutableList
    }

}

/**
 * Represents the response from the Meal API for retrieving meals.
 * @property meals The list of [RecipeTheMeal] objects returned in the response. Can be null if no meals are found.
 */
@Serializable
data class MealsResponse(
    @SerialName("meals") val meals: List<RecipeTheMeal>? = null
)

/**
 * Represents the response from the Meal API for retrieving categories.
 * @property categories The list of [CategoryTheMeal] objects returned in the response. Can be null if no categories are found.
 */
@Serializable
data class CategoriesResponse(
    @SerialName("categories") val categories: List<CategoryTheMeal>? = null
)


/**
 * Represents a nutrient of a food ingredient.
 * @property id The unique identifier of the nutrient.
 * @property unit The unit of measurement for the nutrient.
 * @property calories The amount of calories provided by the nutrient.
 * @property protein The amount of protein provided by the nutrient.
 * @property fiber The amount of fiber provided by the nutrient.
 */
data class Nutrient(
    val id: String, val unit: String, val calories: Int, val protein: Int, val fiber: Int
)

/**
 * Represents a food ingredient.
 * @property id The unique identifier of the ingredient.
 * @property name The name of the ingredient.
 * @property category The category of the ingredient. Can be null.
 * @property unit The unit of measurement for the ingredient.
 * @property imageUrl The URL of the image associated with the ingredient. Can be null.
 * @property nutrientId The unique identifier of the nutrient associated with the ingredient. Can be null.
 */
data class Ingredient(
    val id: String,
    val name: String,
    val category: String? = null,
    val unit: String,
    val imageUrl: String? = null,
    val nutrientId: String? = null
)

/**
 * Represents a relationship between a food recipe and a food ingredient, along with the quantity of the ingredient required.
 * @property recipeId The unique identifier of the recipe.
 * @property ingredientId The unique identifier of the ingredient.
 * @property quantityOfIngredient The quantity of the ingredient required in the recipe.
 */
data class RecipeIngredients(
    val recipeId: String, val ingredientId: String, val quantityOfIngredient: Int
)

/**
 * Represents a food recipe.
 * @property id The unique identifier of the recipe.
 * @property title The title of the recipe.
 * @property servings The number of servings the recipe yields.
 * @property instructions The instructions to prepare the recipe.
 * @property imageUrl The URL of the image associated with the recipe.
 * @property ingredients A string representation of the ingredients used in the recipe. Can be null.
 */
@Serializable
data class Recipe(
    val id: String,
    val title: String,
    val servings: Int,
    val instructions: String,
    val imageUrl: String,
    val ingredients: String? = null,
)

/**
 * Represents a food category.
 * @property id The unique identifier of the category.
 * @property title The title of the category.
 * @property imageUrl The URL of the image associated with the category.
 * @property description The description of the category.
 */
@Serializable
data class Category(
    val id: String,
    val title: String,
    val imageUrl: String,
    val description: String,
)

