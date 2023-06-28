package com.dishdiscoverers.foodrecipe.dongguo

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

const val MEAL_URL = "https://www.themealdb.com/api/json/v1/1/"

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
    val category: String? = null,
    val unit: String,
    val imageUrl: String? = null,
    val nutrientId: String? = null
)

data class RecipeIngredients(
    val recipeId: String,
    val ingredientId: String,
    val quantityOfIngredient: Int
)

@Serializable
data class Recipe(
    val id: String,
    val title: String,
    val servings: Int,
    val instructions: String,
    val imageUrl: String,
    val ingredients: String? = null,
)

@Serializable
data class Category(
    val id: String,
    val title: String,
    val imageUrl: String,
    val description: String,
)


interface RecipeRepository {

    suspend fun getAllCategory(): Resource<List<Category>>
    suspend fun filterByCategory(category: String): List<Recipe>
    suspend fun filterByMainIngredient(ingredient: String): List<Recipe>
    suspend fun findRecipeById(id: String): Recipe?

    //TODO: delete getAll, only for debugging
    suspend fun getAllRecipe(): List<Recipe>
    suspend fun findAddRecipesByIds(ids: List<String>): List<Recipe>

    suspend fun searchRecipesByTitle(title: String): List<Recipe>

    suspend fun addRecipe(recipe: Recipe): String?
    suspend fun deleteRecipeById(id: String)
    suspend fun updateRecipeById(id: String, recipeToUpdate: Recipe)

    // Return List of Id
    suspend fun searchRecipesByIngredient(ingredientName: String): List<Recipe>
    suspend fun searchIngredientsByRecipe(recipeName: String): List<String>

    suspend fun findIngredientById(id: String): Ingredient?
    suspend fun findIngredientByName(name: String): List<Ingredient>
    suspend fun findIngredientByIds(ids: List<String>): List<Ingredient>

    suspend fun addIngredient(ingredient: Ingredient): String?
    suspend fun updateIngredientById(id: String, ingredientToUpdate: Ingredient)
    // no delete function
}

class RecipeRepositoryTheMealAPI : RecipeRepositoryJsonTheMeal() {
    private val _recipes: MutableList<Recipe> = emptyList<Recipe>().toMutableList()
    private val _categories: MutableList<Category> = mutableListOf()
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

    private suspend fun getRecipeFromTheMealApi(title: String): List<Recipe>? {

        val urlString = "https://www.themealdb.com/api/json/v1/1/search.php?s=$title"
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

open class RecipeRepositoryJsonTheMeal : RecipeRepository {
    private val _recipes: MutableList<Recipe> = emptyList<Recipe>().toMutableList()
    private val _ingredients: MutableList<Ingredient> = mutableListOf()
    private val _recipeIngredients: MutableList<RecipeIngredients> = mutableListOf()
    private val _categories: MutableList<Category> = mutableListOf()

    override suspend fun getAllRecipe(): List<Recipe> {
        _recipes.clear()
        _recipes.addAll(getRecipesFromTheMealJson(jsonStringTheMeal))
        return _recipes
    }

    private fun getRecipesFromTheMealJson(json: String): List<Recipe> {
        val list = Json.decodeFromString<List<RecipeTheMeal>>(json)
        var mutableList: MutableList<Recipe> = mutableListOf()
        for (item in list) {
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
        return mutableList.toList()
    }

    internal fun getIngredientField(item: RecipeTheMeal, index: Int): String? {
        return when (index) {
            1 -> item.strIngredient1
            2 -> item.strIngredient2
            3 -> item.strIngredient3
            4 -> item.strIngredient4
            5 -> item.strIngredient5
            6 -> item.strIngredient6
            7 -> item.strIngredient7
            8 -> item.strIngredient8
            9 -> item.strIngredient9
            10 -> item.strIngredient10
            11 -> item.strIngredient11
            12 -> item.strIngredient12
            13 -> item.strIngredient13
            14 -> item.strIngredient14
            15 -> item.strIngredient15
            16 -> item.strIngredient16
            17 -> item.strIngredient17
            18 -> item.strIngredient18
            19 -> item.strIngredient19
            20 -> item.strIngredient20
            else -> null
        }
    }

    internal fun getMeasureField(item: RecipeTheMeal, index: Int): String? {
        return when (index) {
            1 -> item.strMeasure1
            2 -> item.strMeasure2
            3 -> item.strMeasure3
            4 -> item.strMeasure4
            5 -> item.strMeasure5
            6 -> item.strMeasure6
            7 -> item.strMeasure7
            8 -> item.strMeasure8
            9 -> item.strMeasure9
            10 -> item.strMeasure10
            11 -> item.strMeasure11
            12 -> item.strMeasure12
            13 -> item.strMeasure13
            14 -> item.strMeasure14
            15 -> item.strMeasure15
            16 -> item.strMeasure16
            17 -> item.strMeasure17
            18 -> item.strMeasure18
            19 -> item.strMeasure19
            20 -> item.strMeasure20
            else -> null
        }
    }

    override suspend fun searchRecipesByTitle(title: String): List<Recipe> {
        return _recipes.filter { it.title.contains(title, ignoreCase = true) }
    }

    override suspend fun searchRecipesByIngredient(ingredientName: String): List<Recipe> {
        return _recipes.filter { recipe ->
            recipe.ingredients?.contains(ingredientName, ignoreCase = true) ?: false
        }
    }

    override suspend fun getAllCategory(): Resource<List<Category>> {
        return Resource.Success(_categories)
    }

    override suspend fun filterByCategory(category: String): List<Recipe> {
        return _recipes
    }

    override suspend fun filterByMainIngredient(ingredient: String): List<Recipe> {
        return _recipes
    }

    override suspend fun findRecipeById(id: String): Recipe? {
        return _recipes.find { it.id == id }
    }

    override suspend fun findAddRecipesByIds(ids: List<String>): List<Recipe> {
        return _recipes.filter { it.id in ids }
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

class RecipeRepositoryListMock : RecipeRepository {

    private var _recipes: MutableList<Recipe> = recipesMock.toList() as MutableList<Recipe>
    private val _ingredients: MutableList<Ingredient> =
        ingredients.toList() as MutableList<Ingredient>
    private val _recipeIngredients: MutableList<RecipeIngredients> =
        recipeIngredients.toList() as MutableList<RecipeIngredients>

    override suspend fun getAllRecipe(): List<Recipe> {
        return recipesMock
    }

    override suspend fun getAllCategory(): Resource<List<Category>> {
        TODO("Not yet implemented")
    }

    override suspend fun filterByCategory(category: String): List<Recipe> {
        TODO("Not yet implemented")
    }

    override suspend fun filterByMainIngredient(ingredient: String): List<Recipe> {
        TODO("Not yet implemented")
    }

    override suspend fun findRecipeById(id: String): Recipe? {
        return _recipes.find { it.id == id }
    }

    override suspend fun findAddRecipesByIds(ids: List<String>): List<Recipe> {
        return _recipes.filter { it.id in ids }
    }

    override suspend fun searchRecipesByTitle(title: String): List<Recipe> {
        return _recipes.filter { it.title.contains(title, ignoreCase = true) }
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

fun convertMealRecipe(item: RecipeTheMeal, ingredients: String = ""): Recipe? {
    return item?.let {
        if (it.strMeal == null || it.idMeal == null) {
            return null
        }
        Recipe(
            id = it.idMeal,
            title = it.strMeal,
            servings = 1,
            instructions = it.strInstructions ?: "",
            imageUrl = it.strMealThumb ?: "",
            ingredients = ingredients
        )
    }
}

fun convertMealCategory(item: CategoryTheMeal): Category? {
    return item?.let {
        if (it.idCategory == null || it.strCategory == null) {
            return null
        }
        Category(
            id = it.idCategory,
            title = it.strCategory,
            imageUrl = it.strCategoryThumb ?: "",
            description = it.strCategoryDescription ?: ""
        )
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

val jsonStringTheMeal = """
       [
           {
      "idMeal": "52955",
      "strMeal": "Egg Drop Soup",
      "strDrinkAlternate": null,
      "strCategory": "Vegetarian",
      "strArea": "Chinese",
      "strInstructions": "In a wok add chicken broth and wait for it to boil.\r\nNext add salt, sugar, white pepper, sesame seed oil.\r\nWhen the chicken broth is boiling add the vegetables to the wok.\r\nTo thicken the sauce, whisk together 1 Tablespoon of cornstarch and 2 Tablespoon of water in a bowl and slowly add to your soup until it's the right thickness.\r\nNext add 1 egg slightly beaten with a knife or fork and add it to the soup slowly and stir for 8 seconds\r\nServe the soup in a bowl and add the green onions on top.",
      "strMealThumb": "https://www.themealdb.com/images/media/meals/1529446137.jpg",
      "strTags": "Soup,Baking,Calorific",
      "strYoutube": "https://www.youtube.com/watch?v=9XpzHm9QpZg",
      "strIngredient1": "Chicken Stock",
      "strIngredient2": "Salt",
      "strIngredient3": "Sugar",
      "strIngredient4": "Pepper",
      "strIngredient5": "Sesame Seed Oil",
      "strIngredient6": "Peas",
      "strIngredient7": "Mushrooms",
      "strIngredient8": "Cornstarch",
      "strIngredient9": "Water",
      "strIngredient10": "Spring Onions",
      "strIngredient11": "",
      "strIngredient12": "",
      "strIngredient13": "",
      "strIngredient14": "",
      "strIngredient15": "",
      "strIngredient16": "",
      "strIngredient17": "",
      "strIngredient18": "",
      "strIngredient19": "",
      "strIngredient20": "",
      "strMeasure1": "3 cups ",
      "strMeasure2": "1/4 tsp",
      "strMeasure3": "1/4 tsp",
      "strMeasure4": "pinch",
      "strMeasure5": "1 tsp ",
      "strMeasure6": "1/3 cup",
      "strMeasure7": "1/3 cup",
      "strMeasure8": "1 tbs",
      "strMeasure9": "2 tbs",
      "strMeasure10": "1/4 cup",
      "strMeasure11": "",
      "strMeasure12": "",
      "strMeasure13": "",
      "strMeasure14": "",
      "strMeasure15": "",
      "strMeasure16": "",
      "strMeasure17": "",
      "strMeasure18": "",
      "strMeasure19": "",
      "strMeasure20": "",
      "strSource": "https://sueandgambo.com/pages/egg-drop-soup",
      "strImageSource": null,
      "strCreativeCommonsConfirmed": null,
      "dateModified": null
    },
    {
      "idMeal": "53073",
      "strMeal": "Eggplant Adobo",
      "strDrinkAlternate": null,
      "strCategory": "Vegetarian",
      "strArea": "Filipino",
      "strInstructions": "1.\tSlice 1 lb. small Japanese or Italian eggplant (about 3) into quarters lengthwise, then cut crosswise into 2\"-wide pieces. Place in a medium bowl. Add 1 Tbsp. sugar, 1 tsp. Diamond Crystal or \u00bd tsp. Morton kosher salt, and \u00bd tsp. freshly ground black pepper. Toss to evenly coat eggplant and let sit at room temperature at least 20 minutes and up to 2 hours.\r\n2.\tPeel and thinly slice 8 garlic cloves. Add 3 Tbsp. vegetable oil and half of garlic to a medium Dutch oven or other heavy pot. Cook over medium-high heat, stirring constantly with a wooden spoon, until light golden and crisp, about 5 minutes. Using a slotted spoon, transfer garlic chips to a plate; season lightly with salt.\r\n3. Place 4 oz. ground pork in same pot and break up into small pieces with wooden spoon. Season with \u00bc tsp. Diamond Crystal or Morton kosher salt and cook, undisturbed, until deeply browned underneath, about 5 minutes. Using a slotted spoon, transfer to another plate, leaving fat behind in the pot.\r\n4. Place eggplant on a clean kitchen towel and blot away any moisture the salt has drawn out.\r\n5. Working in batches and adding more oil if needed, cook eggplant in the same pot until lightly browned, about 3 minutes per side. Transfer to a plate with pork.\r\n6. Pour 1\u00bd cups of water into the pot and scrape up browned bits from the bottom with a wooden spoon. Add remaining garlic, 3 Tbsp. coconut vinegar or unseasoned rice vinegar, 2 Tbsp. soy sauce, 2 bay leaves, 1 tsp. freshly ground black pepper, and remaining 1 Tbsp. sugar. Bring to a simmer, then return pork and eggplant to pot. Reduce heat to medium-low, partially cover, and simmer until eggplant is tender and silky and sauce is reduced by half, 20\u201325 minutes. Taste and season with more salt and pepper and add a little more sugar if needed.\r\n7. Top with garlic chips and serve with cooked white rice.\r\n",
      "strMealThumb": "https://www.themealdb.com/images/media/meals/y7h0lq1683208991.jpg",
      "strTags": null,
      "strYoutube": "https://www.youtube.com/watch?v=pKXACYjwMns",
      "strIngredient1": "Egg Plants",
      "strIngredient2": "Sugar",
      "strIngredient3": "Salt",
      "strIngredient4": "Pepper",
      "strIngredient5": "Garlic",
      "strIngredient6": "Olive Oil",
      "strIngredient7": "Ground Pork",
      "strIngredient8": "Rice Vinegar",
      "strIngredient9": "Soy Sauce",
      "strIngredient10": "Bay Leaf",
      "strIngredient11": "",
      "strIngredient12": "",
      "strIngredient13": "",
      "strIngredient14": "",
      "strIngredient15": "",
      "strIngredient16": "",
      "strIngredient17": "",
      "strIngredient18": "",
      "strIngredient19": "",
      "strIngredient20": "",
      "strMeasure1": "1 lb",
      "strMeasure2": "2 tbs",
      "strMeasure3": "1 tsp ",
      "strMeasure4": "1 tsp ",
      "strMeasure5": "1 whole",
      "strMeasure6": "3 tbs",
      "strMeasure7": "4 oz ",
      "strMeasure8": "3 tbs",
      "strMeasure9": "2 tbs",
      "strMeasure10": "2",
      "strMeasure11": " ",
      "strMeasure12": " ",
      "strMeasure13": " ",
      "strMeasure14": " ",
      "strMeasure15": " ",
      "strMeasure16": " ",
      "strMeasure17": " ",
      "strMeasure18": " ",
      "strMeasure19": " ",
      "strMeasure20": " ",
      "strSource": "https://salu-salo.com/eggplant-adobo/",
      "strImageSource": null,
      "strCreativeCommonsConfirmed": null,
      "dateModified": null
    },
    {
      "idMeal": "53072",
      "strMeal": "Crispy Eggplant",
      "strDrinkAlternate": null,
      "strCategory": "Vegetarian",
      "strArea": "Filipino",
      "strInstructions": "Slice eggplant into 1 cm (0.4-inch) slices. Place them in a bowl and sprinkle them with salt. allow them to sit for 30 minutes to render some of their liquid and bitterness.\r\n2. After 30 minutes wash eggplant slices from salt and pat dry with a kitchen towel.\r\n3. In a large bowl/plate place breadcrumbs and sesame seeds. In another bowl beat 2 eggs with pinch salt and pepper.\r\n4. Heal oil in a large skillet over high heat.\r\n5. Dip eggplant slices in egg, then in crumbs, and place in hot oil. Fry 2 to 3 minutes on each side, or until golden brown. Drain on a paper towel. \r\n",
      "strMealThumb": "https://www.themealdb.com/images/media/meals/c7lzrl1683208757.jpg",
      "strTags": null,
      "strYoutube": "https://www.youtube.com/watch?v=4mINk5d2hto",
      "strIngredient1": "Egg Plants",
      "strIngredient2": "Breadcrumbs",
      "strIngredient3": "Sesame Seed",
      "strIngredient4": "Eggs",
      "strIngredient5": "Salt",
      "strIngredient6": "Pepper",
      "strIngredient7": "Vegetable Oil",
      "strIngredient8": "",
      "strIngredient9": "",
      "strIngredient10": "",
      "strIngredient11": "",
      "strIngredient12": "",
      "strIngredient13": "",
      "strIngredient14": "",
      "strIngredient15": "",
      "strIngredient16": "",
      "strIngredient17": "",
      "strIngredient18": "",
      "strIngredient19": "",
      "strIngredient20": "",
      "strMeasure1": "1 large",
      "strMeasure2": "1 cup ",
      "strMeasure3": "50g",
      "strMeasure4": "2",
      "strMeasure5": "To taste",
      "strMeasure6": "To taste",
      "strMeasure7": "For frying",
      "strMeasure8": " ",
      "strMeasure9": " ",
      "strMeasure10": " ",
      "strMeasure11": " ",
      "strMeasure12": " ",
      "strMeasure13": " ",
      "strMeasure14": " ",
      "strMeasure15": " ",
      "strMeasure16": " ",
      "strMeasure17": " ",
      "strMeasure18": " ",
      "strMeasure19": " ",
      "strMeasure20": " ",
      "strSource": "https://yummyfood.ph/recipe/crispiest-fried-eggplant/",
      "strImageSource": null,
      "strCreativeCommonsConfirmed": null,
      "dateModified": null
    },
    {
      "idMeal": "52975",
      "strMeal": "Tuna and Egg Briks",
      "strDrinkAlternate": null,
      "strCategory": "Seafood",
      "strArea": "Tunisian",
      "strInstructions": "Heat 2 tsp of the oil in a large saucepan and cook the spring onions over a low heat for 3 minutes or until beginning to soften. Add the spinach, cover with a tight-fitting lid and cook for a further 2\u20133 minutes or until tender and wilted, stirring once or twice. Tip the mixture into a sieve or colander and leave to drain and cool.\r\nUsing a saucer as a guide, cut out 24 rounds about 12.5 cm (5 in) in diameter from the filo pastry, cutting 6 rounds from each sheet. Stack the filo rounds in a pile, then cover with cling film to prevent them from drying out.\r\nWhen the spinach mixture is cool, squeeze out as much excess liquid as possible, then transfer to a bowl. Add the tuna, eggs, hot pepper sauce, and salt and pepper to taste. Mix well.\r\nPreheat the oven to 200\u00b0C (400\u00b0F, gas mark 6). Take one filo round and very lightly brush with some of the remaining oil. Top with a second round and brush with a little oil, then place a third round on top and brush with oil.\r\nPlace a heaped tbsp of the filling in the middle of the round, then fold the pastry over to make a half-moon shape. Fold in the edges, twisting them to seal, and place on a non-stick baking sheet. Repeat with the remaining pastry and filling to make 8 briks in all.\r\nLightly brush the briks with the remaining oil. Bake for 12\u201315 minutes or until the pastry is crisp and golden brown.\r\nMeanwhile, combine the tomatoes and cucumber in a bowl and sprinkle with the lemon juice and seasoning to taste. Serve the briks hot with this salad and the chutney.",
      "strMealThumb": "https://www.themealdb.com/images/media/meals/2dsltq1560461468.jpg",
      "strTags": null,
      "strYoutube": "https://www.youtube.com/watch?v=C5n1fN8TGHs",
      "strIngredient1": "Olive Oil",
      "strIngredient2": "Spring Onions",
      "strIngredient3": "Spinach",
      "strIngredient4": "Filo Pastry",
      "strIngredient5": "Tuna",
      "strIngredient6": "Eggs",
      "strIngredient7": "Hotsauce",
      "strIngredient8": "Tomatoes",
      "strIngredient9": "Cucumber",
      "strIngredient10": "Lemon Juice",
      "strIngredient11": "Apricot Jam",
      "strIngredient12": "",
      "strIngredient13": "",
      "strIngredient14": "",
      "strIngredient15": "",
      "strIngredient16": "",
      "strIngredient17": "",
      "strIngredient18": "",
      "strIngredient19": "",
      "strIngredient20": "",
      "strMeasure1": "2 tbs",
      "strMeasure2": "8",
      "strMeasure3": "200g",
      "strMeasure4": "4",
      "strMeasure5": "1 can ",
      "strMeasure6": "2",
      "strMeasure7": "Dash",
      "strMeasure8": "4 Chopped",
      "strMeasure9": "1/4 ",
      "strMeasure10": "1 tbs",
      "strMeasure11": "4 tbs",
      "strMeasure12": " ",
      "strMeasure13": " ",
      "strMeasure14": " ",
      "strMeasure15": " ",
      "strMeasure16": " ",
      "strMeasure17": " ",
      "strMeasure18": " ",
      "strMeasure19": " ",
      "strMeasure20": " ",
      "strSource": "http://allrecipes.co.uk/recipe/3096/tunisian-tuna-and-egg-briks.aspx",
      "strImageSource": null,
      "strCreativeCommonsConfirmed": null,
      "dateModified": null
    },
    {
      "idMeal": "52962",
      "strMeal": "Salmon Eggs Eggs Benedict",
      "strDrinkAlternate": null,
      "strCategory": "Breakfast",
      "strArea": "American",
      "strInstructions": "First make the Hollandaise sauce. Put the lemon juice and vinegar in a small bowl, add the egg yolks and whisk with a balloon whisk until light and frothy. Place the bowl over a pan of simmering water and whisk until mixture thickens. Gradually add the butter, whisking constantly until thick \u2013 if it looks like it might be splitting, then whisk off the heat for a few mins. Season and keep warm.\r\n\r\nTo poach the eggs, bring a large pan of water to the boil and add the vinegar. Lower the heat so that the water is simmering gently. Stir the water so you have a slight whirlpool, then slide in the eggs one by one. Cook each for about 4 mins, then remove with a slotted spoon.\r\n\r\nLightly toast and butter the muffins, then put a couple of slices of salmon on each half. Top each with an egg, spoon over some Hollandaise and garnish with chopped chives.",
      "strMealThumb": "https://www.themealdb.com/images/media/meals/1550440197.jpg",
      "strTags": "Bun,Brunch",
      "strYoutube": "https://www.youtube.com/watch?v=Woiiet4vQ58",
      "strIngredient1": "Eggs",
      "strIngredient2": "White Wine Vinegar",
      "strIngredient3": "English Muffins",
      "strIngredient4": "Butter",
      "strIngredient5": "Smoked Salmon",
      "strIngredient6": "Lemon Juice",
      "strIngredient7": "White Wine Vinegar",
      "strIngredient8": "Egg",
      "strIngredient9": "Unsalted Butter",
      "strIngredient10": "",
      "strIngredient11": "",
      "strIngredient12": "",
      "strIngredient13": "",
      "strIngredient14": "",
      "strIngredient15": "",
      "strIngredient16": "",
      "strIngredient17": "",
      "strIngredient18": "",
      "strIngredient19": "",
      "strIngredient20": "",
      "strMeasure1": "4",
      "strMeasure2": "2 tbs",
      "strMeasure3": "2",
      "strMeasure4": "To serve",
      "strMeasure5": "8 slices",
      "strMeasure6": "2 tsp",
      "strMeasure7": "2 tsp",
      "strMeasure8": "3 Yolkes",
      "strMeasure9": "125g",
      "strMeasure10": " ",
      "strMeasure11": " ",
      "strMeasure12": " ",
      "strMeasure13": " ",
      "strMeasure14": " ",
      "strMeasure15": " ",
      "strMeasure16": " ",
      "strMeasure17": " ",
      "strMeasure18": " ",
      "strMeasure19": " ",
      "strMeasure20": " ",
      "strSource": "https://www.bbcgoodfood.com/recipes/73606/eggs-benedict-with-smoked-salmon-and-chives",
      "strImageSource": null,
      "strCreativeCommonsConfirmed": null,
      "dateModified": null
    },
    {
      "idMeal": "53074",
      "strMeal": "Grilled eggplant with coconut milk",
      "strDrinkAlternate": null,
      "strCategory": "Vegetarian",
      "strArea": "Filipino",
      "strInstructions": ".  Prepare the eggplants for grilling by pricking them all over with a fork.  This is so it won\u2019t burst during the grilling process as the natural water in it heats up.\r\n2.  Grill the eggplants, turning them over frequently to ensure even cooking.  Grill until the skins are dark brown, even black and the eggplant is soft when you touch it.\r\n3.  Soak the grilled eggplant in a bowl of water to cool it down.  Peel the skin off the eggplant.  Place the whole eggplants in a shallow dish (my mom actually cuts the eggplant into small, bite-sized pieces).\r\n 4.  In a small mixing bowl, mix together the coconut milk or cream, lemon powder, salt and hot pepper.  Mix until the lemon powder and salt dissolve.  Taste, then adjust the amount of lemon powder, salt and hot pepper to your liking.  Pour the mixture over the eggplant.  Sprinkle the green onions over the eggplant and coconut milk.  Stir gently to combine. \r\n",
      "strMealThumb": "https://www.themealdb.com/images/media/meals/bopa2i1683209167.jpg",
      "strTags": null,
      "strYoutube": "https://www.youtube.com/watch?v=ourC5IhljB4",
      "strIngredient1": "Egg Plants",
      "strIngredient2": "Coconut Milk",
      "strIngredient3": "Lemon Juice",
      "strIngredient4": "Salt",
      "strIngredient5": "Red Pepper Flakes",
      "strIngredient6": "Onions",
      "strIngredient7": "",
      "strIngredient8": "",
      "strIngredient9": "",
      "strIngredient10": "",
      "strIngredient11": "",
      "strIngredient12": "",
      "strIngredient13": "",
      "strIngredient14": "",
      "strIngredient15": "",
      "strIngredient16": "",
      "strIngredient17": "",
      "strIngredient18": "",
      "strIngredient19": "",
      "strIngredient20": "",
      "strMeasure1": "6",
      "strMeasure2": "1 can ",
      "strMeasure3": "1 tbs",
      "strMeasure4": "1 tsp ",
      "strMeasure5": "To taste",
      "strMeasure6": "4 Sticks",
      "strMeasure7": " ",
      "strMeasure8": " ",
      "strMeasure9": " ",
      "strMeasure10": " ",
      "strMeasure11": " ",
      "strMeasure12": " ",
      "strMeasure13": " ",
      "strMeasure14": " ",
      "strMeasure15": " ",
      "strMeasure16": " ",
      "strMeasure17": " ",
      "strMeasure18": " ",
      "strMeasure19": " ",
      "strMeasure20": " ",
      "strSource": "https://simplybakings.com/grilled-eggplant-with-coconut-milk/",
      "strImageSource": null,
      "strCreativeCommonsConfirmed": null,
      "dateModified": null
    },
    {
      "idMeal": "52816",
      "strMeal": "Roasted Eggplant With Tahini, Pine Nuts, and Lentils",
      "strDrinkAlternate": null,
      "strCategory": "Vegetarian",
      "strArea": "American",
      "strInstructions": "\r\nFor the Lentils: Adjust oven rack to center position and preheat oven to 450\u00b0F to prepare for roasting eggplant. Meanwhile, heat 2 tablespoons olive oil in a medium saucepan over medium heat until shimmering. Add carrots, celery, and onion and cook, stirring, until softened but not browned, about 4 minutes. Add garlic and cook, stirring, until fragrant, about 30 seconds. Add lentils, bay leaves, stock or water, and a pinch of salt. Bring to a simmer, cover with the lid partially ajar, and cook until lentils are tender, about 30 minutes. (Top up with water if lentils are at any point not fully submerged.) Remove lid, stir in vinegar, and reduce until lentils are moist but not soupy. Season to taste with salt and pepper, cover, and keep warm until ready to serve.\r\n\r\n2.\r\nFor the Eggplant: While lentils cook, cut each eggplant in half. Score flesh with the tip of a paring knife in a cross-hatch pattern at 1-inch intervals. Transfer to a foil-lined rimmed baking sheet, cut side up, and brush each eggplant half with 1 tablespoon oil, letting each brushstroke be fully absorbed before brushing with more. Season with salt and pepper. Place a rosemary sprig on top of each one. Transfer to oven and roast until completely tender and well charred, 25 to 35 minutes. Remove from oven and discard rosemary.\r\n\r\n3.\r\nTo Serve: Heat 2 tablespoons olive oil and pine nuts in a medium skillet set over medium heat. Cook, tossing nuts frequently, until golden brown and aromatic, about 4 minutes. Transfer to a bowl to halt cooking. Stir half of parsley and rosemary into lentils and transfer to a serving platter. Arrange eggplant halves on top. Spread a few tablespoons of tahini sauce over each eggplant half and sprinkle with pine nuts. Sprinkle with remaining parsley and rosemary, drizzle with additional olive oil, and serve.",
      "strMealThumb": "https://www.themealdb.com/images/media/meals/ysqrus1487425681.jpg",
      "strTags": "Vegetarian,Pulse,Nutty",
      "strYoutube": "https://www.youtube.com/watch?v=HkywCtna9t0",
      "strIngredient1": "Olive Oil",
      "strIngredient2": "Carrots",
      "strIngredient3": "Celery",
      "strIngredient4": "Onion",
      "strIngredient5": "Garlic",
      "strIngredient6": "Brown Lentils",
      "strIngredient7": "Bay Leaves",
      "strIngredient8": "Water",
      "strIngredient9": "Salt",
      "strIngredient10": "Apple Cider Vinegar",
      "strIngredient11": "Pepper",
      "strIngredient12": "Egg Plants",
      "strIngredient13": "Rosemary",
      "strIngredient14": "Pine nuts",
      "strIngredient15": "Parsley",
      "strIngredient16": "",
      "strIngredient17": "",
      "strIngredient18": "",
      "strIngredient19": "",
      "strIngredient20": "",
      "strMeasure1": "2 tablespoons",
      "strMeasure2": "2 small cut into chunks",
      "strMeasure3": "2 small stalks",
      "strMeasure4": "1 medium finely diced",
      "strMeasure5": "6 medium cloves sliced",
      "strMeasure6": "12 ounces (340g)",
      "strMeasure7": "2",
      "strMeasure8": "4 cups",
      "strMeasure9": "Pinch",
      "strMeasure10": "2 teaspoons (10ml)",
      "strMeasure11": "Pinch",
      "strMeasure12": "2 large",
      "strMeasure13": "4 sprigs",
      "strMeasure14": "1/4 cup ",
      "strMeasure15": "2 tablespoons",
      "strMeasure16": "",
      "strMeasure17": "",
      "strMeasure18": "",
      "strMeasure19": "",
      "strMeasure20": "",
      "strSource": "http://www.seriouseats.com/recipes/2016/03/roasted-eggplant-tahini-pine-nut-lentil-vegan-experience-recipe.html",
      "strImageSource": null,
      "strCreativeCommonsConfirmed": null,
      "dateModified": null
    },
    {
      "idMeal": "52817",
      "strMeal": "Stovetop Eggplant With Harissa, Chickpeas, and Cumin Yogurt",
      "strDrinkAlternate": null,
      "strCategory": "Vegetarian",
      "strArea": "American",
      "strInstructions": "Heat the oil in a 12-inch skillet over high heat until shimmering. Add the eggplants and lower the heat to medium. Season with salt and pepper as you rotate the eggplants, browning them on all sides. Continue to cook, turning regularly, until a fork inserted into the eggplant meets no resistance (you may have to stand them up on their fat end to finish cooking the thickest parts), about 20 minutes, lowering the heat and sprinkling water into the pan as necessary if the eggplants threaten to burn or smoke excessively.\r\n\r\n2.\r\nMix the harissa, chickpeas and tomatoes together, then add to the eggplants. Cook until the tomatoes have blistered and broken down, about 5 minutes more. Season with salt and pepper and add water as necessary to thin to a saucy consistency. Meanwhile, combine the yogurt and cumin in a serving bowl. Season with salt and pepper.\r\n\r\n3.\r\nTop the eggplant mixture with the parsley, drizzle with more extra virgin olive oil, and serve with the yogurt on the side.",
      "strMealThumb": "https://www.themealdb.com/images/media/meals/yqwtvu1487426027.jpg",
      "strTags": "Vegetarian",
      "strYoutube": "https://www.youtube.com/watch?v=uYB-1xJp4lg",
      "strIngredient1": "Olive Oil",
      "strIngredient2": "Egg Plants",
      "strIngredient3": "Harissa",
      "strIngredient4": "Chickpeas",
      "strIngredient5": "Cherry Tomatoes",
      "strIngredient6": "Greek yogurt",
      "strIngredient7": "Ground cumin",
      "strIngredient8": "Parsley",
      "strIngredient9": "",
      "strIngredient10": "",
      "strIngredient11": "",
      "strIngredient12": "",
      "strIngredient13": "",
      "strIngredient14": "",
      "strIngredient15": "",
      "strIngredient16": "",
      "strIngredient17": "",
      "strIngredient18": "",
      "strIngredient19": "",
      "strIngredient20": "",
      "strMeasure1": "4 tablespoons",
      "strMeasure2": "6 small",
      "strMeasure3": "\u00bd tablespoon",
      "strMeasure4": "1 can",
      "strMeasure5": "2 cups halved",
      "strMeasure6": "1 1/2 cups",
      "strMeasure7": "1 tablespoon",
      "strMeasure8": "\u00bd cup ",
      "strMeasure9": "",
      "strMeasure10": "",
      "strMeasure11": "",
      "strMeasure12": "",
      "strMeasure13": "",
      "strMeasure14": "",
      "strMeasure15": "",
      "strMeasure16": "",
      "strMeasure17": "",
      "strMeasure18": "",
      "strMeasure19": "",
      "strMeasure20": "",
      "strSource": "http://www.seriouseats.com/2014/09/one-pot-wonders-stovetop-eggplant-harissa-chickpeas-cumin.html",
      "strImageSource": null,
      "strCreativeCommonsConfirmed": null,
      "dateModified": null
    }
        ]
""".trimIndent()

@Serializable
data class MealsResponse(
    @SerialName("meals")
    val meals: List<RecipeTheMeal>? = null
)

@Serializable
data class RecipeTheMeal(
    @SerialName("dateModified")
    val dateModified: String? = null,
    @SerialName("idMeal")
    val idMeal: String? = null,
    @SerialName("strArea")
    val strArea: String? = null,
    @SerialName("strCategory")
    val strCategory: String? = null,
    @SerialName("strCreativeCommonsConfirmed")
    val strCreativeCommonsConfirmed: String? = null,
    @SerialName("strDrinkAlternate")
    val strDrinkAlternate: String? = null,
    @SerialName("strImageSource")
    val strImageSource: String? = null,
    @SerialName("strIngredient1")
    val strIngredient1: String? = null,
    @SerialName("strIngredient10")
    val strIngredient10: String? = null,
    @SerialName("strIngredient11")
    val strIngredient11: String? = null,
    @SerialName("strIngredient12")
    val strIngredient12: String? = null,
    @SerialName("strIngredient13")
    val strIngredient13: String? = null,
    @SerialName("strIngredient14")
    val strIngredient14: String? = null,
    @SerialName("strIngredient15")
    val strIngredient15: String? = null,
    @SerialName("strIngredient16")
    val strIngredient16: String? = null,
    @SerialName("strIngredient17")
    val strIngredient17: String? = null,
    @SerialName("strIngredient18")
    val strIngredient18: String? = null,
    @SerialName("strIngredient19")
    val strIngredient19: String? = null,
    @SerialName("strIngredient2")
    val strIngredient2: String? = null,
    @SerialName("strIngredient20")
    val strIngredient20: String? = null,
    @SerialName("strIngredient3")
    val strIngredient3: String? = null,
    @SerialName("strIngredient4")
    val strIngredient4: String? = null,
    @SerialName("strIngredient5")
    val strIngredient5: String? = null,
    @SerialName("strIngredient6")
    val strIngredient6: String? = null,
    @SerialName("strIngredient7")
    val strIngredient7: String? = null,
    @SerialName("strIngredient8")
    val strIngredient8: String? = null,
    @SerialName("strIngredient9")
    val strIngredient9: String? = null,
    @SerialName("strInstructions")
    val strInstructions: String? = null,
    @SerialName("strMeal")
    val strMeal: String? = null,
    @SerialName("strMealThumb")
    val strMealThumb: String? = null,
    @SerialName("strMeasure1")
    val strMeasure1: String? = null,
    @SerialName("strMeasure10")
    val strMeasure10: String? = null,
    @SerialName("strMeasure11")
    val strMeasure11: String? = null,
    @SerialName("strMeasure12")
    val strMeasure12: String? = null,
    @SerialName("strMeasure13")
    val strMeasure13: String? = null,
    @SerialName("strMeasure14")
    val strMeasure14: String? = null,
    @SerialName("strMeasure15")
    val strMeasure15: String? = null,
    @SerialName("strMeasure16")
    val strMeasure16: String? = null,
    @SerialName("strMeasure17")
    val strMeasure17: String? = null,
    @SerialName("strMeasure18")
    val strMeasure18: String? = null,
    @SerialName("strMeasure19")
    val strMeasure19: String? = null,
    @SerialName("strMeasure2")
    val strMeasure2: String? = null,
    @SerialName("strMeasure20")
    val strMeasure20: String? = null,
    @SerialName("strMeasure3")
    val strMeasure3: String? = null,
    @SerialName("strMeasure4")
    val strMeasure4: String? = null,
    @SerialName("strMeasure5")
    val strMeasure5: String? = null,
    @SerialName("strMeasure6")
    val strMeasure6: String? = null,
    @SerialName("strMeasure7")
    val strMeasure7: String? = null,
    @SerialName("strMeasure8")
    val strMeasure8: String? = null,
    @SerialName("strMeasure9")
    val strMeasure9: String? = null,
    @SerialName("strSource")
    val strSource: String? = null,
    @SerialName("strTags")
    val strTags: String? = null,
    @SerialName("strYoutube")
    val strYoutube: String? = null
)

@Serializable
data class CategoriesResponse(
    @SerialName("categories")
    val categories: List<CategoryTheMeal>? = null
)

@Serializable
data class CategoryTheMeal(
    @SerialName("idCategory")
    val idCategory: String,
    @SerialName("strCategory")
    val strCategory: String,
    @SerialName("strCategoryDescription")
    val strCategoryDescription: String,
    @SerialName("strCategoryThumb")
    val strCategoryThumb: String
)
