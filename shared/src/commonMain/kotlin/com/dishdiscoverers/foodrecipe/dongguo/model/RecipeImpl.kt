package com.dishdiscoverers.foodrecipe.dongguo.model


class RecipeRepositoryImpl : RecipeRepository {
    private val recipes: MutableList<Recipe> = mutableListOf()
    private val ingredients: MutableList<Ingredient> = mutableListOf()
    private val recipeIngredients: MutableList<RecipeIngredients> = mutableListOf()

    override suspend fun findRecipeById(id: String): Recipe? {
        return recipes.find { it.id == id }
    }

    override suspend fun findAddRecipesByIds(ids: List<String>): List<Recipe> {
        return recipes.filter { it.id in ids }
    }

    override suspend fun searchRecipesByTitle(title: String): List<Recipe> {
        return recipes.filter { it.title.contains(title, ignoreCase = true) }
    }

    override suspend fun addRecipe(recipe: Recipe): String? {
        recipes.add(recipe)
        return recipe.id
    }

    override suspend fun deleteRecipeById(id: String) {
        val recipe = recipes.find { it.id == id }
        recipes.remove(recipe)
    }

    override suspend fun updateRecipeById(id: String, recipeToUpdate: Recipe) {
        val index = recipes.indexOfFirst { it.id == id }
        if (index != -1) {
            recipes[index] = recipeToUpdate.copy(id = id)
        }
    }

    override suspend fun searchRecipesByIngredient(ingredientName: String): List<String> {

        return recipes.filter { recipe ->
            ingredients.any { it.name.contains(ingredientName, ignoreCase = true) }
        }.map { it.id }
    }

    override suspend fun searchIngredientsByRecipe(recipeName: String): List<String> {
        val recipe = recipes.firstOrNull() { it.title == recipeName } ?: return emptyList()
        val filteredIngredients: List<RecipeIngredients> =
            recipeIngredients.filter { it.recipeId == recipe.id }
        return filteredIngredients.map { it.ingredientId }
    }
}

class IngredientRepositoryImpl : IngredientRepository {
    private val ingredients: MutableList<Ingredient> = mutableListOf()

    override suspend fun findIngredientById(id: String): Ingredient? {
        return ingredients.find { it.id == id }
    }

    override suspend fun findIngredientByName(name: String): List<Ingredient> {
        return ingredients.filter { it.name.contains(name, ignoreCase = true) }
    }

    override suspend fun findIngredientByIds(ids: List<String>): List<Ingredient> {
        return ingredients.filter { it.id in ids }
    }

    override suspend fun addIngredient(ingredient: Ingredient): String? {
        ingredients.add(ingredient)
        return ingredient.id
    }

    override suspend fun updateIngredientById(id: String, ingredientToUpdate: Ingredient) {
        val index = ingredients.indexOfFirst { it.id == id }
        if (index != -1) {
            ingredients[index] = ingredientToUpdate.copy(id = id)
        }
    }
}


