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
    val servings: String,
    val instructions: String,
    val imageUrl: String
)

data class DongguoUser(
    val name: String,
)

data class UserRecipe(
    val userId: String,
    val recipeId: String
)

interface RecipeRepository {
    suspend fun findRecipeById(id: String): Recipe?
    suspend fun findAddRecipesByIds(ids: List<String>): List<Recipe>

    suspend fun searchRecipesByTitle(title: String): List<Recipe>

    suspend fun addRecipe(recipe: Recipe): String?
    suspend fun deleteRecipeById(id: String)
    suspend fun updateRecipeById(id: String, recipeToUpdate : Recipe)

    // Return List of Id
    suspend fun searchRecipesByIngredient(ingredientName: String): List<String>
    suspend fun searchIngredientsByRecipe(recipeName: String): List<String>
}

interface IngredientRepository {
    suspend fun findIngredientById(id: String): Ingredient?
    suspend fun findIngredientByName(name: String): List<Ingredient>
    suspend fun findIngredientByIds(ids: List<String>): List<Ingredient>

    suspend fun addIngredient(ingredient: Ingredient): String?
    suspend fun updateIngredientById(id: String, ingredientToUpdate: Ingredient)
    // no delete function
}

interface UserRepository {
    suspend fun findUserById(id: String): DongguoUser?
    suspend fun findUserByEmail(email: String): DongguoUser?

    suspend fun addUser(dongguoUser: DongguoUser): String?
    suspend fun updateUserById(id: String, dongguoUserToUpdate: DongguoUser)
    suspend fun deleteUserById(id: String)

    // Return List of Id
    suspend fun getFavoriteRecipesByUserId(userId: String): List<String>
    suspend fun addFavoriteRecipesByUserId(userId: String, recipeId: String)
    suspend fun addAllFavoriteRecipesByUserId(userId: String, recipeIds: List<String>)
    suspend fun deleteFavoriteRecipeByUserId(userId: String, recipeId: String)
    suspend fun deleteAllFavoriteRecipesByUserId(userId: String)
}
