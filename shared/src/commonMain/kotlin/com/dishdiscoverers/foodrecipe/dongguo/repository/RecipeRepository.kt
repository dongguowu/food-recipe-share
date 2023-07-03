package com.dishdiscoverers.foodrecipe.dongguo.repository

import com.dishdiscoverers.foodrecipe.dongguo.repository.api.RecipeServiceTheMealSingleton
import com.dishdiscoverers.foodrecipe.dongguo.repository.json.RecipeRepositoryTheMealJson
import kotlinx.serialization.Serializable

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

    /**
     * Retrieves recipes filtered by containing the title.
     * @param title The title to be contained.
     * @return A list of [Recipe] matching the provided title.
     */
    suspend fun findRecipesByTitle(title: String): Resource<List<Recipe>>

    /**
     * Retrieve recipe by id.
     * @param recipeId a string.
     * @return A [Resource] containing a [Recipe].
     */
    suspend fun findRecipeById(recipeId: String): Resource<Recipe>

    /**
     * Retrieve recipes by a list of id.
     * @param ids a list of string.
     * @return A [Resource] containing a list of [Recipe].
     */
    suspend fun findRecipesByIds(ids: List<String>): Resource<List<Recipe>>

    suspend fun findIngredientByIds(ids: List<String>): List<Ingredient>

    //TODO: below, not implemented.
    suspend fun filterByCategory(category: String): Resource<List<Recipe>>
    suspend fun filterByMainIngredient(ingredient: String): Resource<List<Recipe>>

    suspend fun addRecipe(recipe: Recipe): String?
    suspend fun deleteRecipeById(id: String)
    suspend fun updateRecipeById(id: String, recipeToUpdate: Recipe)
    suspend fun searchRecipesByIngredient(ingredientName: String): List<Recipe>
    suspend fun searchIngredientsByRecipe(recipeName: String): List<String>

    suspend fun findIngredientById(id: String): Ingredient?
    suspend fun findIngredientByName(name: String): List<Ingredient>

    suspend fun addIngredient(ingredient: Ingredient): String?
    suspend fun updateIngredientById(id: String, ingredientToUpdate: Ingredient)
    // no delete ingredient function
}

/**
 * Implementation of the [RecipeRepository] interface for retrieving recipes from TheMealAPI.
 */

class RecipeRepositoryTheMealAPIJson  :
    RecipeRepositoryTheMealJson() {

    private val _recipes: MutableList<Recipe> = emptyList<Recipe>().toMutableList()
    private val _categories: MutableList<Category> = mutableListOf()

    override suspend fun getAllCategory(): Resource<List<Category>> {
        return RecipeServiceTheMealSingleton.getAllCategory()
    }

    override suspend fun getAllIngredient(): Resource<List<Ingredient>> {
        return RecipeServiceTheMealSingleton.getAllIngredient()
    }
    override suspend fun findRecipeById(recipeId: String): Resource<Recipe> {
        return RecipeServiceTheMealSingleton.getRecipe(recipeId)
    }

    override suspend fun findRecipesByTitle(title: String): Resource<List<Recipe>> {
        return RecipeServiceTheMealSingleton.getRecipeByTitle(title)
    }

}


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
 * @property description Description. Can be null.
 */
data class Ingredient(
    val id: String,
    val name: String,
    val category: String? = null,
    val unit: String? = null,
    val imageUrl: String? = null,
    val description: String? = null,
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

