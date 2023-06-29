//package com.lduboscq.appkickstarter.mains.data
//
//import com.dishdiscoverers.foodrecipe.garett.model.Ingredient
//import com.dishdiscoverers.foodrecipe.garett.model.Recipe
//
//interface IngredientRepository {
//
//    //TODO: delete getAll, only for debugging
//    suspend fun getAllRecipe(): List<Recipe>
//    suspend fun findRecipeById(id: String): Recipe?
//    suspend fun findAddRecipesByIds(ids: List<String>): List<Recipe>
//
//    suspend fun searchRecipesByTitle(title: String): List<Recipe>
//
//    suspend fun addRecipe(recipe: Recipe): String?
//    suspend fun deleteRecipeById(id: String)
//    suspend fun updateRecipeById(id: String, recipeToUpdate: Recipe)
//
//    // Return List of Id
//    suspend fun searchRecipesByIngredient(ingredientName: String): List<String>
//    suspend fun searchIngredientsByRecipe(recipeName: String): List<String>
//
//    suspend fun findIngredientById(id: String): Ingredient?
//    suspend fun findIngredientByName(name: String): List<Ingredient>
//    suspend fun findIngredientByIds(ids: List<String>): List<Ingredient>
//
//    suspend fun addIngredient(ingredient: Ingredient): String?
//    suspend fun updateIngredientById(id: String, ingredientToUpdate: Ingredient)
//    suspend fun getAllIngredient(): List<Ingredient>
//    suspend fun deleteIngredientById(id: String)
//}
//
