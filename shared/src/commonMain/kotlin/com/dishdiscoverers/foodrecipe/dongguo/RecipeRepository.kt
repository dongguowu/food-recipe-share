package com.dishdiscoverers.foodrecipe.dongguo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

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
data class RecipeFromNinjas(
    @SerialName("title")
    val title: String,
    @SerialName("ingredients")
    var ingredients: String,
    @SerialName("servings")
    val servings: String,
    @SerialName("instructions")
    val instructions: String,
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
    suspend fun searchRecipesByIngredient(ingredientName: String): List<Recipe>
    suspend fun searchIngredientsByRecipe(recipeName: String): List<String>

    suspend fun findIngredientById(id: String): Ingredient?
    suspend fun findIngredientByName(name: String): List<Ingredient>
    suspend fun findIngredientByIds(ids: List<String>): List<Ingredient>

    suspend fun addIngredient(ingredient: Ingredient): String?
    suspend fun updateIngredientById(id: String, ingredientToUpdate: Ingredient)
    // no delete function
}


class RecipeRepositoryAPI : RecipeRepository {
    private val recipes: MutableList<Recipe> = mutableListOf(
    )
    private val ingredients: MutableList<Ingredient> = mutableListOf()
    private val recipeIngredients: MutableList<RecipeIngredients> = mutableListOf()


    override suspend fun getAllRecipe(): List<Recipe> {
        return recipes
    }

    override suspend fun searchRecipesByTitle(title: String): List<Recipe> {
        FetchRecipe().search(title)

        return recipes
    }

    override suspend fun searchRecipesByIngredient(ingredientName: String): List<Recipe> {
        return recipes.filter { recipe ->
            recipe.ingredients?.contains(ingredientName, ignoreCase = true) ?: false
        }
    }

    override suspend fun findRecipeById(id: String): Recipe? {
        return recipes.find { it.id == id }
    }

    override suspend fun findAddRecipesByIds(ids: List<String>): List<Recipe> {
        return recipes.filter { it.id in ids }
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

    override suspend fun searchIngredientsByRecipe(recipeName: String): List<String> {
        val recipe = recipes.firstOrNull() { it.title == recipeName } ?: return emptyList()
        val filteredIngredients: List<RecipeIngredients> =
            recipeIngredients.filter { it.recipeId == recipe.id }
        return filteredIngredients.map { it.ingredientId }
    }


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


class RecipeRepositoryJsonTheMeal : RecipeRepository {
    private val recipes: MutableList<Recipe> = mutableListOf(
        Recipe(
            id = "1",
            title = "Ella\'s Vegetable and Meat Egg Rolls",
            servings = 14,
            instructions = "Fry ground beef, drain, set aside for now. Heat wok, add oil, heat until hot, but not smoking, put celery, onions, bean sprouts and waterchestnuts. fry 2 minutes. Add salt, sugar, and soy sauce, cook 1 minute more. Add ground beef and mix well. Mix cornstarch and water well. Add to mixture in wok. set aside and cool. When cool add to egg roll wrappers, wrapping diagonaly then fry in deep fat for 3 to 5 minutes. Serve with a mixture of mustard and ketchup. Did egg rolls in this. Use 7 egg roll wrappers and cut in half and this will make 15 egg rolls. NOTES : Very good.",
            imageUrl = "https://www.alisonspantry.com/uploads/new-products/4078-2.jpg"
        ),
    )
    private val ingredients: MutableList<Ingredient> = mutableListOf()
    private val recipeIngredients: MutableList<RecipeIngredients> = mutableListOf()

    private var jsonString = """
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

    override suspend fun getAllRecipe(): List<Recipe> {
        val list = Json.decodeFromString<List<RecipeFromTheMealDB>>(jsonString)
        recipes.clear()
        for (item in list) {
            val recipe = Recipe(
                id = item.idMeal,
                title = item.strMeal,
                servings = 1,
                instructions = item.strInstructions,
                imageUrl = item.strMealThumb,
                ingredients = "",
            )
            recipes.add(recipe)
        }
        return recipes
    }

    override suspend fun searchRecipesByTitle(title: String): List<Recipe> {
        return recipes.filter { it.title.contains(title, ignoreCase = true) }
    }

    override suspend fun searchRecipesByIngredient(ingredientName: String): List<Recipe> {
        return recipes.filter { recipe ->
            recipe.ingredients?.contains(ingredientName, ignoreCase = true) ?: false
        }
    }

    override suspend fun findRecipeById(id: String): Recipe? {
        return recipes.find { it.id == id }
    }

    override suspend fun findAddRecipesByIds(ids: List<String>): List<Recipe> {
        return recipes.filter { it.id in ids }
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

    override suspend fun searchIngredientsByRecipe(recipeName: String): List<String> {
        val recipe = recipes.firstOrNull() { it.title == recipeName } ?: return emptyList()
        val filteredIngredients: List<RecipeIngredients> =
            recipeIngredients.filter { it.recipeId == recipe.id }
        return filteredIngredients.map { it.ingredientId }
    }


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

class RecipeRepositoryJson : RecipeRepository {
    private val recipes: MutableList<Recipe> = mutableListOf(
        Recipe(
            id = "1",
            title = "Ella\'s Vegetable and Meat Egg Rolls",
            servings = 14,
            instructions = "Fry ground beef, drain, set aside for now. Heat wok, add oil, heat until hot, but not smoking, put celery, onions, bean sprouts and waterchestnuts. fry 2 minutes. Add salt, sugar, and soy sauce, cook 1 minute more. Add ground beef and mix well. Mix cornstarch and water well. Add to mixture in wok. set aside and cool. When cool add to egg roll wrappers, wrapping diagonaly then fry in deep fat for 3 to 5 minutes. Serve with a mixture of mustard and ketchup. Did egg rolls in this. Use 7 egg roll wrappers and cut in half and this will make 15 egg rolls. NOTES : Very good.",
            imageUrl = "https://www.alisonspantry.com/uploads/new-products/4078-2.jpg"
        ),
    )
    private val ingredients: MutableList<Ingredient> = mutableListOf()
    private val recipeIngredients: MutableList<RecipeIngredients> = mutableListOf()

    private var jsonStringNinjas = """
   [
  {
    "title": "Ella's Vegetable and Meat Egg Rolls",
    "ingredients": "1 lb Ground beef|3 Stalks celery; chopped|5 Green onions; chopped|1 cn Bean sprouts; washed and drained|1 cn Waterchestnuts; chopped|2 tb Oil|1 ts Salt|2 tb Sugar|1/4 c Soy sauce|2 ts Cornstarch|1 tb Cold water|7 Egg roll wrappers; cut in half|Mustard|Ketchup",
    "servings": "14 Servings",
    "instructions": "Fry ground beef, drain, set aside for now. Heat wok, add oil, heat until hot, but not smoking, put celery, onions, bean sprouts and waterchestnuts. fry 2 minutes. Add salt, sugar, and soy sauce, cook 1 minute more. Add ground beef and mix well. Mix cornstarch and water well. Add to mixture in wok. set aside and cool. When cool add to egg roll wrappers, wrapping diagonaly then fry in deep fat for 3 to 5 minutes. Serve with a mixture of mustard and ketchup. Did egg rolls in this. Use 7 egg roll wrappers and cut in half and this will make 15 egg rolls. NOTES : Very good."
  },
  {
    "title": "Emeril's Beggar Purses Wrapped in a Truffle Crepe",
    "ingredients": "1 1/2 c Milk|1 c Truffle flour|2 Eggs|Salt; to taste|Freshly-ground white pepper; to taste|2 tb Olive oil|12 Quail eggs|12 lg Fresh scallops; cleaned|1 c Truffle Mashed Potatoes; hot, see * Note|12 Whole Chives|1 Recipe Truffle Salsify Relish|1 Recipe Spinach And Black Truffle Reduction; hot, see * Note|Chopped fresh parsley; for garnish",
    "servings": "4 Servings",
    "instructions": "* Note: See the \"Truffle Mashed Potatoes\", \"Truffle Salsify Relish\", and \"Spinach And Black Truffle Reduction\" recipes which are included in this collection. For the crepes: In a mixing bowl, combine the milk, truffle flour, and eggs together. Whisk until smooth. Season the batter with salt and white pepper. Heat a lightly-greased 6-inch pan. Remove from the heat and add 2 tablespoons of the batter. Lift and tilt the pan to spread the batter evenly. Return to the heat; brown on one side only. Flip the crepe, remove from the pan onto parchment paper. Crack the quail eggs into individual ramekins. In a non-stick pan, heat 1 tablespoon of olive oil. When the oil is hot, carefully slide the ramekins with the quail eggs, into the pan. Season the eggs with salt and pepper. Fry the eggs for about 1 minute and remove from the pan and set aside. Season the scallops with salt and pepper. In a large saute pan, heat 1 tablespoon of olive oil. When the oil is hot, add the scallops and sear for 2 to 3 minutes on each side. Remove the scallops and set aside. Place the scallops in the center of each crepe. Place a tablespoon of the Truffle Mashed Potatoes on top of the scallops. Lay a fried quail egg on top of the mashed potatoes. Pull the edges of the crepe together and tie with a long chive, forming a purse-like shape. To serve, spoon the Spinach And Black Truffle Reduction sauce in the center of four plates. Place three scallop purees in the center of the sauce. Spoon the Truffle Salsify Relish around the scallops. Garnish with parsley. This recipe yields 4 main course servings or 12 appetizer servings. Comments: The original recipe title as listed is \"Emeril's Beggar Purses Consisting Of Seared Scallops, Truffle Mash, And Fried Quail Eggs Wrapped In A Truffle Crepe\"."
  },
  {
    "title": "Emeril's Crab Meat Deviled Eggs",
    "ingredients": "1 Dozen hard boiled eggs; shelled and cut into half|1/4 c Mayonnaise|1/2 lb Lump crab meat; shelled and picked for cartilage|1 ts Minced garlic|1 tb Minced capers|2 tb Caper juice|Salt|Freshly ground white pepper|1 tb Finely chopped fresh parsley leaves",
    "servings": "1 Servings",
    "instructions": "Remove the yolks from the whites and place in a mixing bowl. Using the back of a fork, break the yolks into small pieces. Add the mayonnaise, crab meat, garlic, capers and caper juice. Mix well. Season the mixture with salt and pepper. Spoon the mixture into the white halves. Chill the eggs completely and garnish with parsley. Yields: 2 dozen eggs What else could Emeril Lagasse cook up for Easter besides some delicious eggs?"
  },
  {
    "title": "English Muffin W/ham Egg",
    "ingredients": "6 1/4 lb HAM SECTIONED CURED|6 1/4 lb CHEESE AMER/SLICE|100 EGGS SHELL|100 BREAD ENG MFFN 12OZ #95",
    "servings": "100 Servings",
    "instructions": "YIELD: 100 PORTIONS (6 PANS) EACH PORTION: 1 SANDWICH PAN SIZE: 18 BY 26-INCH SHEET PAN TEMPERATURE: 375 F. OVEN 325 F. GRIDDLE 1. GRILL HAM UNTIL LIGHLY BROWNED. USE IN STEP 4. 2. PLACE ENGLISH MUFFIN HALVES ON SHEET PANS IN ROWS, 5 BY 7 ; TOAST 6 TO 8 MINUTES IN OVEN. SET ASIDE FOR USE IN STEP 6. 3. BREAK ONE EGG AT A TIME IN SMALL BOWL; POUR ON GREASED GRIDDLE. FRY 2 MINUTES; TURN. 4. PLACE I SLICE HAM ON EGG. 5. PLACE 1 SLICE CHEESE ON HAM. CONTINUE TO COOK UNTIL CHEESE MELTS. 6. PLACE I CHEESE AND HAM TOPPED FRIED EGG ON BOTTOM HALF OF EACH SPLIT MUFFIN. TOP WITH SECOND HALF OF MUFFIN. SERVE IMMEDIATELY. NOTE: 1. IN STEP 2, ONE RECIPE ENGLISH MUFFINS (RECIPE NO. D-21) MAY BE USED. SERVING SIZE: 1 SANDWICH"
  },
  {
    "title": "Fresh Mushrooms with Eggplant and Tomato",
    "ingredients": "1 lb Eggplant|Salt|3 tb Olive oil|1 md Onion, chopped|1 lg Celery stalk, chopped|2 md Firm mushrooms, chopped|1 1/2 ts Garlic, chopped|1 lg Tomato, chopped|2 tb Breadcrumbs|1 tb Tomato paste|5 tb Fresh parsley, chopped|1 ts Basil|Fresh lemon juice|30 md Whole mushrooms",
    "servings": "6 Servings",
    "instructions": "Cut eggplant in half lengthwise. Make crisscross patterns in the pulp & sprinkle with salt. Let stand for 3o minutes. Rinse thoroughly & drain well, drying with paper towels. Peel & coarsely chop. Heat 2 tb oil in a skillet over a medium heat. Cook eggplant till softened. Let cool. Heat remining oil & cook onion, celery, choped mushrooms & garlic for 4 minutes. Add tomatoes & return eggplant to skillet. Stir in breadcrumbs, tomato paste & 3 tb parsley. Add basil. Continue to cook for 4 minutes. Just before serving, wipe mushrooms with cloth dampened in lemon juice. Carefully remove the stems. Fill each cap with some of the eggplant mixture. Sprinkle with remaining parsley & serve. You can serve the mushrooms hot by baking briefly until watm & then sprinkling with parsley. Joel Rapp, \"Mother Earth's Vegetarian Feasts\""
  },
  {
    "title": "Fresh Veggie Basil Salad",
    "ingredients": "4 md Zucchini -- haved and|Sliced|Do not use the baseball bat|Ones|4 md Cucumbers -- halved and|Sliced|4 Tomatoes -- diced|6 Basil leaved -- chopped|Minced garlic -- to your|Liking|1/4 c Pine nuts -- chopped|1/4 c Parmesian cheese --|Shredded|Dressing:|1/4 c Water|1/4 c White vinegar|1/4 c Sugar|1/2 ts Salt",
    "servings": "8 Servings",
    "instructions": "Toss salad ingredients together. Make dressing: Heat all ingredients until sugar is dissolved. Let cool. Pour on salad right before you serve. I used what I had available in the garden. Substitute with what you have. Merry eating, Tina : CookNGrl "
  },
  {
    "title": "Fresh Veggie Pizza Bars",
    "ingredients": "2 cn Crescent rolls (8oz each)|2 Cream cheese (8oz each); softened|1/4 c Mayonnaise or salad dressing|1 Ranch style salad dressing mix; (1oz)|1 md Sweet red pepper; chopped|1 md Green pepper; chopped|1 c Cheddar cheese (4 oz); grated|1 c Broccoli flowerets; coarsely chopped|1 c Fresh mushrooms; coarsely chopped|1 c Carrots; shredded|1 c Cauliflower; chopped|4 Green Onions; thinly sliced",
    "servings": "8 Dozen",
    "instructions": "Unroll dough. Place on lightly greased 15x10 inch pan, pressing edges and perforations together to line bottom of pan. Bake at 350F for 7-8 min or until browned. Cool. Combine cream cheese, mayo, and dressing mix; beat at med speed until smooth. Spread over crust in pan. Combine peppers and remaining ingredients; sprinkle over cheese mixture. Cover and chill 8 hours. Cut into 1 1/4 inch squares. Sent to CFF Nov.97"
  },
  {
    "title": "Fried 100-Year Old Eggs",
    "ingredients": "1 c Peanut oil|3 100 year old eggs|1 Salted egg|3 tb Flour|1/2 ts Sugar|2 ts Wine vinegar|1/2 ts Salt|2 Tomatoes",
    "servings": "1 Batch",
    "instructions": "I. Scrape ashes from B with a knife, wash. Cover B with water, bring to a boil and cook 3 minutes (so that B is firmed). Drain, cover with cold water a few seconds. Shell; quarter each B. II. Break up yolk of C with fork and beat C until fluffy. III. Mix in D gradually; add E, F, G, mix well. IV. Dip H in boiling water a few seconds; remove skin and cut H into slices. COOKING: 1. Heat A to 375F. 2. Dip each B in C-G until well coated. Deep-fry until all sides are golden brown. 3. Place in the middle of dish, garnish with H. Serve hot or cold. From \"An Encyclopedia of Chinese Food and Cooking\" by Wonona W. and Irving B. Chang, Helen W. and Austin H. Kutscher. Crown Publishers, Inc. New York."
  },
  {
    "title": "Fried Egg",
    "ingredients": "2 c MIlk|2 tb Buttermilk|4 Peach Halves",
    "servings": "4 Servings",
    "instructions": "Put milk in jar and add the 2 T buttermilk. Stir well and set aside for 24 hours.It will thicken.To make dessert put a couple of spoonfuls onto a plate and flatten a bit. Put a peach half on top of this, thus the name of fried egg."
  },
  {
    "title": "Fried Egg Sauce",
    "ingredients": "1 Scallion|1 tb Oil|1/2 tb Brown sugar|1 tb Soy sauce|2 tb Water",
    "servings": "4 Servings",
    "instructions": "1. Mince scallion. 2. Heat oil. Add scallion; stir-fry to brown lightly. 3. Stir in brown sugar, soy sauce and water; cook to heat through. Sprinkle sauce over fried eggs and serve."
  },
  {
    "title": "Electronic Gourmet's Marinated Steak",
    "ingredients": "2 c Red Wine|Onion, minced|1 cl Garlic, crushed|1/2 ts Black Pepper|1 tb Butter|8-12 oz Sirloin or|Porterhouse Steak",
    "servings": "1 Servings",
    "instructions": "1) Combine red wine, onion, garlic and black pepper. Marinate steak in this mixture for at least 2 hours turning occasionally. Overnight in fridge is best. 2) Remove the steak, reserving the marinade and pat it dry. Rub the steak with butter. 3) Broil the steak over hot coals, or under broiler until medium rare (or to your liking). 4) Boil the marinade until reduced by two thirds. Discard the garlic clove and pour the sauce over the steak. Goes great with a bottle of dry red table wine from either California or the Rhone region of France."
  },
  {
    "title": "Electronic Gourmet's Steak Diane",
    "ingredients": "Onion or Shallot, chop fine|3 oz Butter|4 sl Sirloin or Rump Steak (thin)|1 ds Worcestershire Sauce|1 ds Brandy|Fresh Parsley, chopped",
    "servings": "4 Servings",
    "instructions": "1) Fry shallots in the hot butter for 2 minutes. Add the thin slices of steak, cook 1 minute on either side. 2) Lift out the meat, and put onto a hot dish. Add the sauce, and the brandy to the butter. 3) Ignite if wished (and insured) and pour over the steaks. Garnish with chopped parsley. This dish requires last minute cooking."
  },
  {
    "title": "Emeril's Cowboy Steaks",
    "ingredients": "1 c Plus 2 tablespoons olive oil|1 1/2 c Essence|4 (12-14 ounce) steaks; Porterhouse, T-bone, Strip Loin, Rib or Tenderloin Steak|2 c Julienne assorted Exotic mushrooms|1 c Julienne onions|1 lg Onion; ring into rings, about 1/8-inch thick|1/4 c Tabasco sauce|1 c Flour|Oil for frying|1 c Mashed roasted root vegetables|2 c Mashed potatoes|Salt and pepper",
    "servings": "1 Servings",
    "instructions": "Preheat the grill In a shallow bowl, combine the olive oil and Essence, to make a paste. Smear each steak with the rub, covering the entire steak completely. Marinate the steaks for about an hour. Place the steaks on the grill and cook for 3 minutes. Remove from the grill and each steak on a cedar plank. Toss the mushrooms and onions with salt and pepper. Mound the vegetables on top of each steak and place back on the grill. Cook the steaks on the plank for 4 minutes for medium rare. Toss the onion rings in the Tabasco. Dredge the onions in the flour. Fry the onions until golden brown, about 3 minutes. Remove from the fryer and season with salt and pepper. In a sauce pan, combine the roasted vegetables and mashed potatoes together. Yield: 4 servings"
  },
  {
    "title": "Emeril's Funky Fried Steak",
    "ingredients": "4 (4-ounce) beef cutlets|Southwest seasoning mix|1 1/2 c Flour|2 Eggs,; slightly beaten|Vegetable oil for frying|2 c Roasted garlic mashed potatoes,; hot|1/2 c Emeril's Worcestershire sauce|1 c White gravy for the steaks|Garnish: 1 tablespoon chopped chives, brunoise peppers",
    "servings": "1 Servings",
    "instructions": "In a cast-iron skillet, preheat the oil. Heavily season each side of the cutlets with the Southwest seasoning, Season the flour with the Southwest seasoning. Dredge the cutlets in the flour. Dip each cutlet in the beaten eggs, letting any excess drip off. Dredge the cutlets back into the flour, coating each side completely. Place the cutlets in the hot oil. Fry the steaks for 3-4 minutes on each side. Remove from the oil and drain on paper-lined plate. Season the steaks with salt and pepper. Mound the potatoes in the center of the plate. Drizzle Worcestershire sauce around the potatoes. Lay the steaks on top of the potatoes. Spoon the gravy over the steak. Garnish chives and brunoise peppers."
  },
  {
    "title": "Emu Steak Au Poivre",
    "ingredients": "1/2 c Black peppercorns|4 Emu steaks; 4oz each|1/4 c Oil|1/3 c Cognac|1 c Heavy cream|2 tb Cognac|Salt to taste",
    "servings": "4 Servings",
    "instructions": "1. Place peppercorns in press-closure baggie. Crush with a hammer. Lightly coat both sides of steaks with peppercorns. 2. Heat oil in large skillet. Saute steaks over medium-high heat until rare or medium rare (3-4 minutes per side). Remove from pan and discard oil. 3. Return steaks to pan, add 1/3 cup cognac. Cook over high heat until cognac is reduced to about 3 tablespoons. Remove steaks to serving platter and keep warm. Add cream to pan; cook and stir over high heat until cream boils and thickens. Add 2 teaspoons cognac and salt. Spoon sauce over steaks and serve."
  },
  {
    "title": "English Steak and Kidney Pie",
    "ingredients": "2 Beef Kidneys*|3/4 lb Round Steak|1 Flour|1 Salt and Pepper|1 tb Oil|1 c Chopped onion|1 Hot water|1 Bay leaf|2 Sprigs parsley|1 Celery leaf top|1/2 c Mushrooms|1/2 c Diced carrots|2 tb Flour (heaping)|1/2 c Cold water|1 Pastry for 1 crust",
    "servings": "6 Servings",
    "instructions": "English Steak & Kidney Pie (British Isles) *4 Veal kidneys may be used for a milder flavor Remove the skin and coarse parts from the kidneys. Wash in salted water. Cut into 1\" squares. Pound flour into round steak and cut into pieces. Combine flour, salt and pepper. Dredge kidney in seasoned flour. Heat oil in heavy skillet and brown beef cubes on all sides, add onion and kidneys and cook slowly until brown. Cover meats with hot water, add bay leaf, parsley and celery tops. Cover tightly and simmer for 1 hour. Remove meat to deep baking dish. Add mushrooms, carrots. Mix 2 T flour with 1/2 cup water and add to pan liquor to thicken it. Pour over meat and vegetables and top with pastry crust. Bake in 350 degree F oven for about 1 hour."
  },
  {
    "title": "Gorgonzola-Grappa Rib Steak",
    "ingredients": "4 Bone-in rib steaks -; (14 to 16 oz ea)|Salt|Freshly ground black pepper|3/4 lb Italian Gorgonzola cheese|3 tb Grappa; or more",
    "servings": "4 servings",
    "instructions": "Preheat oven to 450 degrees. Season steaks with salt and pepper. Using 2 large nonstick skillets, sear steaks on each side over very high heat, a total of 3 minutes. Place steaks on baking sheet and finish cooking in hot oven until desired doneness, approximately 5 minutes for rare. Cut rind off cheese with a sharp knife; cut cheese into small cubes. Place in heavy medium saucepan with 3 tablespoons grappa. Heat slowly to melt cheese, stirring constantly. Continue to cook for several minutes, until sauce is thick. If it's too thick, add a little more grappa or water. Place the steaks on plates; pour cheese sauce over steaks. Serve immediately. Yield: 4 servings."
  },
  {
    "title": "Gravy for Steaks",
    "ingredients": "1 lb FRESH MUSHROOMS; SLICED|4 tb BUTTER|3 tb FLOUR|2/3 c CHICKEN BROTH OR BEEF STOCK|3 tb SHERRY|SALT; PEPPER, RED PEPPER, AND SEASONINGS OF YOUR CHOICE TO TASTE., (I use a couple of squirts of Lea and Perrin.)",
    "servings": "1 servings",
    "instructions": "I made this quite by accident one night and found it very good, even rather \"gourmetish,\" in fact. Try it, you will be surprised. Sauté mushrooms in butter for about 5 minutes. Add flour by sprinkling over mushrooms, stirring all the while for about 2 to 3 minutes. It will glob up but just keep on stirring. Slowly add chicken or beef stock and sherry. Add seasonings. Continue to cook until it is as thick as you want. VERY GOOD! TIP: You can dissolve bouillon or chicken cubes with water for stock or broth. Spoon gravy onto plate and then place steak on top of gravy, saving a few mushrooms to go on top of steak. YIELD: Gravy for 2 - 4 steaks."
  },
  {
    "title": "Great American Steakhouse Balsamic Dijon Dipping Sauce-0 Poi",
    "ingredients": "1/4 c Balsamic vinegar|1 ts Spoonable Equal sweetener|1 ts Water|2 tb Honey mustard|1 ds Salt|1 ds Black pepper",
    "servings": "4 servings",
    "instructions": "1. Combine all ingredients in small bowl and mix well. Chill. Serving Size (1/4 recipe) Per MC Nutritional Analysis: Per Serving: 9.5 Calories, 0.6g Fat, 0g Fiber Weight Watcher Points: 0"
  },
  {
    "title": "Great American Steakhouse Cheese Dipping Sauce - 3 Points",
    "ingredients": "2 tb Brummel and Brown Spread|2 tb All-purpose flour|1 ts Salt|1 c Skim milk|16 tb Reduced fat cheddar cheese|1 ts Mustard; any type|1 ds Seasoned salt|1 ds Black pepper",
    "servings": "4 servings",
    "instructions": "1. In a sauce pan, melt Brummel and Brown. 2. Add flour and salt. Stirring continually, heat until bubbly. 3. Slowly add milk. Continue stirring over medium heat until mixture has thickened. 4. Add cheese and mustard. Stir until cheese is melted. 5. Serve warm. Serving Size (1/4 recipe) Per MC Nutritional Analysis: Per Serving: 110.4 Calories, 4.7g Fat, 0.2g Fiber Weight Watcher Points: 3"
  }
]
 
""".trimIndent()

    override suspend fun getAllRecipe(): List<Recipe> {
        val list = Json.decodeFromString<List<RecipeFromNinjas>>(jsonStringNinjas)
        recipes.clear()
        for (item in list) {
            recipes.add(
                Recipe(
                    id = "4",
                    title = item.title,
                    servings = item.servings.substringBefore(" ").toIntOrNull() ?: 1,
                    instructions = item.instructions,
                    imageUrl = "",
                    ingredients = item.ingredients,
                )
            )
        }
        return recipes
    }

    override suspend fun searchRecipesByTitle(title: String): List<Recipe> {
        return recipes.filter { it.title.contains(title, ignoreCase = true) }
    }

    override suspend fun searchRecipesByIngredient(ingredientName: String): List<Recipe> {
        return recipes.filter { recipe ->
            recipe.ingredients?.contains(ingredientName, ignoreCase = true) ?: false
        }
    }

    override suspend fun findRecipeById(id: String): Recipe? {
        return recipes.find { it.id == id }
    }

    override suspend fun findAddRecipesByIds(ids: List<String>): List<Recipe> {
        return recipes.filter { it.id in ids }
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

    override suspend fun searchIngredientsByRecipe(recipeName: String): List<String> {
        val recipe = recipes.firstOrNull() { it.title == recipeName } ?: return emptyList()
        val filteredIngredients: List<RecipeIngredients> =
            recipeIngredients.filter { it.recipeId == recipe.id }
        return filteredIngredients.map { it.ingredientId }
    }


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


class RecipeRepositoryMock : RecipeRepository {
    private val recipes: MutableList<Recipe> = mutableListOf(
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
    private val ingredients: MutableList<Ingredient> = mutableListOf()
    private val recipeIngredients: MutableList<RecipeIngredients> = mutableListOf()
    override suspend fun getAllRecipe(): List<Recipe> {
        return recipes
    }

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

    override suspend fun searchRecipesByIngredient(ingredientName: String): List<Recipe> {
        return recipes
    }

    override suspend fun searchIngredientsByRecipe(recipeName: String): List<String> {
        val recipe = recipes.firstOrNull() { it.title == recipeName } ?: return emptyList()
        val filteredIngredients: List<RecipeIngredients> =
            recipeIngredients.filter { it.recipeId == recipe.id }
        return filteredIngredients.map { it.ingredientId }
    }


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



