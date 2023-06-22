package com.dishdiscoverers.foodrecipe.dongguo.model

data class Nutrient(
    val id: String,
    val unit: String,
    val calories: Int,
    val protein: Int,
    val fiber: Int
)

data class Ingredient(
    val id: String,
    val name: String,
    val category: String,
    val unit: String,
    val imageUrl: String,
    val nutrientId: String?
)

data class RecipeIngredients(
    val recipeId: String,
    val ingredientId: String,
    val quantityOfIngredient: Int
)

data class Recipe(
    val id: String,
    val title: String,
    val servings: Int,
    val instructions: String,
    val imageUrl: String
)

interface RecipeRepository {

    //TODO: delete getAll, only for debugging
    suspend fun getAllRecipe(): List<Recipe>
    suspend fun findRecipeById(id: String): Recipe?
    suspend fun findAddRecipesByIds(ids: List<String>): List<Recipe>

    suspend fun searchRecipesByTitle(title: String): List<Recipe>

    suspend fun addRecipe(recipe: Recipe): String?
    suspend fun deleteRecipeById(id: String)
    suspend fun updateRecipeById(id: String, recipeToUpdate: Recipe)

    // Return List of Id
    suspend fun searchRecipesByIngredient(ingredientName: String): List<String>
    suspend fun searchIngredientsByRecipe(recipeName: String): List<String>

    suspend fun findIngredientById(id: String): Ingredient?
    suspend fun findIngredientByName(name: String): List<Ingredient>
    suspend fun findIngredientByIds(ids: List<String>): List<Ingredient>

    suspend fun addIngredient(ingredient: Ingredient): String?
    suspend fun updateIngredientById(id: String, ingredientToUpdate: Ingredient)
    // no delete function
}




