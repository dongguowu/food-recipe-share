package com.dishdiscoverers.foodrecipe.garett.data//import com.lduboscq.appkickstarter.mains.data.IngredientRepository
//import com.lduboscq.appkickstarter.mains.model.Ingredient
//import com.lduboscq.appkickstarter.mains.model.Recipe
//import com.lduboscq.appkickstarter.mains.model.RecipeIngredients
//
//class RecipeDummyData : IngredientRepository {
//    private val recipes: MutableList<Recipe> = mutableListOf(
//        Recipe(
//            id = "1",
//            title = "Ella\'s Vegetable and Meat Egg Rolls",
//            servings = 14,
//            instructions = "Fry ground beef, drain, set aside for now. Heat wok, add oil, heat until hot, but not smoking, put celery, onions, bean sprouts and waterchestnuts. fry 2 minutes. Add salt, sugar, and soy sauce, cook 1 minute more. Add ground beef and mix well. Mix cornstarch and water well. Add to mixture in wok. set aside and cool. When cool add to egg roll wrappers, wrapping diagonaly then fry in deep fat for 3 to 5 minutes. Serve with a mixture of mustard and ketchup. Did egg rolls in this. Use 7 egg roll wrappers and cut in half and this will make 15 egg rolls. NOTES : Very good.",
//            imageUrl = "https://www.alisonspantry.com/uploads/new-products/4078-2.jpg"
//        ),
//        Recipe(
//            id = "2",
//            title = "Emeril\'s Crab Meat Deviled Eggs",
//            servings = 1,
//            instructions = "Fry ground beef, drain, set aside for now. Heat wok, add oil, heat until hot, but not smoking, put celery, onions, bean sprouts and waterchestnuts. fry 2 minutes. Add salt, sugar, and soy sauce, cook 1 minute more. Add ground beef and mix well. Mix cornstarch and water well. Add to mixture in wok. set aside and cool. When cool add to egg roll wrappers, wrapping diagonaly then fry in deep fat for 3 to 5 minutes. Serve with a mixture of mustard and ketchup. Did egg rolls in this. Use 7 egg roll wrappers and cut in half and this will make 15 egg rolls. NOTES : Very good.",
//            imageUrl = "https://imagesvc.meredithcorp.io/v3/mm/image?url=https%3A%2F%2Fimages.media-allrecipes.com%2Fuserphotos%2F8293743.jpg&q=60&c=sc&orient=true&poi=auto&h=512"
//        ),
//        Recipe(
//            id = "3",
//            title = "English Muffin W/ham Egg",
//            servings = 14,
//            instructions = "Fry ground beef, drain, set aside for now. Heat wok, add oil, heat until hot, but not smoking, put celery, onions, bean sprouts and waterchestnuts. fry 2 minutes. Add salt, sugar, and soy sauce, cook 1 minute more. Add ground beef and mix well. Mix cornstarch and water well. Add to mixture in wok. set aside and cool. When cool add to egg roll wrappers, wrapping diagonaly then fry in deep fat for 3 to 5 minutes. Serve with a mixture of mustard and ketchup. Did egg rolls in this. Use 7 egg roll wrappers and cut in half and this will make 15 egg rolls. NOTES : Very good.",
//            imageUrl = "https://imagesvc.meredithcorp.io/v3/mm/image?url=https%3A%2F%2Fstatic.onecms.io%2Fwp-content%2Fuploads%2Fsites%2F43%2F2022%2F11%2F04%2FHam-Egg-and-Cheese-Breakfast-Sandwiches-France-C-2000.jpg&q=60&c=sc&orient=true&poi=auto&h=512"
//        )
//    )
//
//
//    private val ingredients: MutableList<Ingredient> = mutableListOf(
//        Ingredient(
//            id = "1",
//            name = "Salt",
//            category = "Basics",
//            unit = "tsp",
//            imageUrl = "https://en.wikipedia.org/wiki/Salt#/media/File:Comparison_of_Table_Salt_with_Kitchen_Salt.png",
//            nutrientId = "1"
//        ),
//        Ingredient(
//            id = "2",
//            name = "Pepper",
//            category = "Basics",
//            unit = "tsp",
//            imageUrl = "https://en.wikipedia.org/wiki/Black_pepper#/media/File:Dried_Peppercorns.jpg",
//            nutrientId = "2"
//        ),
//        Ingredient(
//            id = "3",
//            name = "Chicken",
//            category = "Meat",
//            unit = "g",
//            imageUrl = "https://en.wikipedia.org/wiki/Chicken#/media/File:Male_and_female_chicken_sitting_together.jpg",
//            nutrientId = "3"
//        ),
//    )
//    private val recipeIngredients: MutableList<RecipeIngredients> = mutableListOf()
//    override suspend fun getAllRecipe(): List<Recipe> {
//        return recipes
//    }
//
//    override suspend fun findRecipeById(id: String): Recipe? {
//        return recipes.find { it.id == id }
//    }
//
//    override suspend fun findAddRecipesByIds(ids: List<String>): List<Recipe> {
//        return recipes.filter { it.id in ids }
//    }
//
//    override suspend fun searchRecipesByTitle(title: String): List<Recipe> {
//        return recipes.filter { it.title.contains(title, ignoreCase = true) }
//    }
//
//    override suspend fun addRecipe(recipe: Recipe): String? {
//        recipes.add(recipe)
//        return recipe.id
//    }
//
//    override suspend fun deleteRecipeById(id: String) {
//        val recipe = recipes.find { it.id == id }
//        recipes.remove(recipe)
//    }
//
//    override suspend fun updateRecipeById(id: String, recipeToUpdate: Recipe) {
//        val index = recipes.indexOfFirst { it.id == id }
//        if (index != -1) {
//            recipes[index] = recipeToUpdate.copy(id = id)
//        }
//    }
//
//    override suspend fun searchRecipesByIngredient(ingredientName: String): List<String> {
//
//        return recipes.filter { recipe ->
//            ingredients.any { it.name.contains(ingredientName, ignoreCase = true) }
//        }.map { it.id }
//    }
//
//    override suspend fun searchIngredientsByRecipe(recipeName: String): List<String> {
//        val recipe = recipes.firstOrNull() { it.title == recipeName } ?: return emptyList()
//        val filteredIngredients: List<RecipeIngredients> =
//            recipeIngredients.filter { it.recipeId == recipe.id }
//        return filteredIngredients.map { it.ingredientId }
//    }
//
//
//    override suspend fun findIngredientById(id: String): Ingredient? {
//        return ingredients.find { it.id == id }
//    }
//
//    override suspend fun findIngredientByName(name: String): List<Ingredient> {
//        return ingredients.filter { it.name.contains(name, ignoreCase = true) }
//    }
//
//    override suspend fun findIngredientByIds(ids: List<String>): List<Ingredient> {
//        return ingredients.filter { it.id in ids }
//    }
//
//    override suspend fun addIngredient(ingredient: Ingredient): String? {
//        ingredients.add(ingredient)
//        return ingredient.id
//    }
//
//    override suspend fun updateIngredientById(id: String, ingredientToUpdate: Ingredient) {
//        val index = ingredients.indexOfFirst { it.id == id }
//        if (index != -1) {
//            ingredients[index] = ingredientToUpdate.copy(id = id)
//        }
//    }
//
//    override suspend fun getAllIngredient(): List<Ingredient> {
//        return ingredients
//    }
//
//    override suspend fun deleteIngredientById(id: String) {
//        val ingredient = ingredients.find { it.id == id }
//        ingredients.remove(ingredient)
//    }
//}