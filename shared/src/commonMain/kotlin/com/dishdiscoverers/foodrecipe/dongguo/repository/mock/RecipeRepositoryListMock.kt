package com.dishdiscoverers.foodrecipe.dongguo.repository.mock

import com.dishdiscoverers.foodrecipe.dongguo.repository.Category
import com.dishdiscoverers.foodrecipe.dongguo.repository.Ingredient
import com.dishdiscoverers.foodrecipe.dongguo.repository.Recipe
import com.dishdiscoverers.foodrecipe.dongguo.repository.RecipeIngredients
import com.dishdiscoverers.foodrecipe.dongguo.repository.RecipeRepository
import com.dishdiscoverers.foodrecipe.dongguo.repository.Resource

class RecipeRepositoryListMock : RecipeRepository {

    private var _recipes: MutableList<Recipe> = recipesMock.toList() as MutableList<Recipe>
    private val _ingredients: MutableList<Ingredient> =
        ingredients.toList() as MutableList<Ingredient>
    private val _recipeIngredients: MutableList<RecipeIngredients> =
        recipeIngredients.toList() as MutableList<RecipeIngredients>

    fun getAllRecipe(): List<Recipe> {
        return recipesMock
    }

    override suspend fun getAllCategory(): Resource<List<Category>> {
        return Resource.Failure(Exception("Not yet implemented"))
    }

    override suspend fun getAllIngredient(): Resource<List<Ingredient>> {
        return Resource.Failure(Exception("Not yet implemented"))
    }

    override suspend fun filterByCategory(category: String): Resource<List<Recipe>> {
        return Resource.Failure(Exception("Not yet implemented"))
    }

    override suspend fun filterByMainIngredient(ingredient: String): Resource<List<Recipe>> {
        return Resource.Failure(Exception("Not yet implemented"))
    }

    override suspend fun findRecipeById(recipeId: String): Resource<Recipe> {
        return Resource.Failure(Exception("Not yet implemented"))
    }

    override suspend fun findRecipesByIds(ids: List<String>): Resource<List<Recipe>> {
        return Resource.Success(_recipes.filter { it.id in ids })
    }

    override suspend fun findRecipesByTitle(title: String): Resource<List<Recipe>> {
        return Resource.Success(_recipes.filter { it.title.contains(title, ignoreCase = true) })
    }

    override suspend fun addRecipe(recipe: Recipe): String? {
        _recipes.add(recipe)
        return recipe.id
    }

    override suspend fun deleteRecipeById(id: String) {
        val recipe = _recipes.find { it.id == id }
        _recipes.remove(recipe)
    }

    override suspend fun updateRecipeById(id: String, recipeToUpdate: Recipe) {
        val index = _recipes.indexOfFirst { it.id == id }
        if (index != -1) {
            _recipes[index] = recipeToUpdate.copy(id = id)
        }
    }

    override suspend fun searchRecipesByIngredient(ingredientName: String): List<Recipe> {
        return _recipes
    }

    override suspend fun searchIngredientsByRecipe(recipeName: String): List<String> {
        val recipe = _recipes.firstOrNull() { it.title == recipeName } ?: return emptyList()
        val filteredIngredients: List<RecipeIngredients> =
            _recipeIngredients.filter { it.recipeId == recipe.id }
        return filteredIngredients.map { it.ingredientId }
    }


    override suspend fun findIngredientById(id: String): Ingredient? {
        return _ingredients.find { it.id == id }
    }

    override suspend fun findIngredientByName(name: String): List<Ingredient> {
        return _ingredients.filter { it.name.contains(name, ignoreCase = true) }
    }

    override suspend fun findIngredientByIds(ids: List<String>): List<Ingredient> {
        return _ingredients.filter { it.id in ids }
    }

    override suspend fun addIngredient(ingredient: Ingredient): String? {
        _ingredients.add(ingredient)
        return ingredient.id
    }

    override suspend fun updateIngredientById(id: String, ingredientToUpdate: Ingredient) {
        val index = _ingredients.indexOfFirst { it.id == id }
        if (index != -1) {
            _ingredients[index] = ingredientToUpdate.copy(id = id)
        }
    }
}

val ingredients: List<Ingredient> = emptyList()

val recipeIngredients: List<RecipeIngredients> = emptyList()

val recipesMock: List<Recipe> = listOf(
    Recipe(
        id = "1",
        title = "Ella\'s Vegetable and Meat Egg Rolls",
        servings = 14,
        instructions = "Fry ground beef, drain, set aside for now. Heat wok, add oil, heat until hot, but not smoking, put celery, onions, bean sprouts and waterchestnuts. fry 2 minutes. Add salt, sugar, and soy sauce, cook 1 minute more. Add ground beef and mix well. Mix cornstarch and water well. Add to mixture in wok. set aside and cool. When cool add to egg roll wrappers, wrapping diagonaly then fry in deep fat for 3 to 5 minutes. Serve with a mixture of mustard and ketchup. Did egg rolls in this. Use 7 egg roll wrappers and cut in half and this will make 15 egg rolls. NOTES : Very good.",
        imageUrl = "https://www.alisonspantry.com/uploads/new-products/4078-2.jpg"
    ),
    Recipe(
        id = "2",
        title = "Emeril\'s Crab Meat Deviled Eggs",
        servings = 1,
        instructions = "Fry ground beef, drain, set aside for now. Heat wok, add oil, heat until hot, but not smoking, put celery, onions, bean sprouts and waterchestnuts. fry 2 minutes. Add salt, sugar, and soy sauce, cook 1 minute more. Add ground beef and mix well. Mix cornstarch and water well. Add to mixture in wok. set aside and cool. When cool add to egg roll wrappers, wrapping diagonaly then fry in deep fat for 3 to 5 minutes. Serve with a mixture of mustard and ketchup. Did egg rolls in this. Use 7 egg roll wrappers and cut in half and this will make 15 egg rolls. NOTES : Very good.",
        imageUrl = "https://imagesvc.meredithcorp.io/v3/mm/image?url=https%3A%2F%2Fimages.media-allrecipes.com%2Fuserphotos%2F8293743.jpg&q=60&c=sc&orient=true&poi=auto&h=512"
    ),
    Recipe(
        id = "3",
        title = "English Muffin W/ham Egg",
        servings = 14,
        instructions = "Fry ground beef, drain, set aside for now. Heat wok, add oil, heat until hot, but not smoking, put celery, onions, bean sprouts and waterchestnuts. fry 2 minutes. Add salt, sugar, and soy sauce, cook 1 minute more. Add ground beef and mix well. Mix cornstarch and water well. Add to mixture in wok. set aside and cool. When cool add to egg roll wrappers, wrapping diagonaly then fry in deep fat for 3 to 5 minutes. Serve with a mixture of mustard and ketchup. Did egg rolls in this. Use 7 egg roll wrappers and cut in half and this will make 15 egg rolls. NOTES : Very good.",
        imageUrl = "https://imagesvc.meredithcorp.io/v3/mm/image?url=https%3A%2F%2Fstatic.onecms.io%2Fwp-content%2Fuploads%2Fsites%2F43%2F2022%2F11%2F04%2FHam-Egg-and-Cheese-Breakfast-Sandwiches-France-C-2000.jpg&q=60&c=sc&orient=true&poi=auto&h=512"
    )
)

val categoriesMock: List<Category> = listOf()
