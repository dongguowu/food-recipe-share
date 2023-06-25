package com.dishdiscoverers.foodrecipe.dongguo

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

class RecipeRepositoryJson : RecipeRepository {
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

    @Serializable
    data class RecipeFromNinjas(
        val title: String,
        var ingredients: String,
        val servings: String,
        val instructions: String,
    )

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



