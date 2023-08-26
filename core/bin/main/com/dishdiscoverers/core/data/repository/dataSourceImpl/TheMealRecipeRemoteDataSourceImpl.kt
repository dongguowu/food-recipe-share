package com.dishdiscoverers.core.data.repository.dataSourceImpl

import com.dishdiscoverers.core.data.api.TheMealAPIService
import com.dishdiscoverers.core.data.model.FoodRecipe
import com.dishdiscoverers.core.data.model.meal.TheMealAPIMealsResponse
import com.dishdiscoverers.core.data.model.meal.TheMealRecipe
import com.dishdiscoverers.core.data.utility.Resource
import com.dishdiscoverers.core.data.repository.dataSource.RecipeRemoteDataSource
import retrofit2.Response
import java.lang.Exception

class TheMealRecipeRemoteDataSourceImpl(
    private val theMealAPIService: TheMealAPIService,
) : RecipeRemoteDataSource {

    override suspend fun searchRecipes(title: String): Resource<List<FoodRecipe>> {
        val response = searchTheMealRecipes(title)
        if (response.isSuccessful) {
            val meals = response.body()?.meals
            if (!meals.isNullOrEmpty()) {
                val parsedRecipes = meals.mapNotNull { meal ->
                    val ingredients = getIngredientsFromTheMealRecipe(meal)
                    parseTheMealRecipe(meal, ingredients)
                }
                return Resource.Success(parsedRecipes)
            }
        }
        // Handle the error case and return an appropriate Resource.Error if needed
        return Resource.Failure(Exception("Failed to fetch recipes"))
    }


    suspend fun searchTheMealRecipes(title: String): Response<TheMealAPIMealsResponse> {
        return theMealAPIService.searchRecipes(title)
    }

    /**
     * Converts a  [TheMealRecipe] from the API response to a [FoodRecipe] object.
     *
     * @param item        The recipe to convert.
     * @param ingredients Additional ingredients for the recipe (optional).
     * @return The converted Recipe object, or null if conversion fails.
     */
    fun parseTheMealRecipe(item: TheMealRecipe, ingredients: String = ""): FoodRecipe? {
        return item?.let {
            if (it.strMeal == null || it.idMeal == null) {
                return null
            }
            FoodRecipe(
                id = it.idMeal,
                title = it.strMeal,
                servings = 1,
                instructions = it.strInstructions ?: "",
                imageUrl = it.strMealThumb ?: "",
                ingredients = ingredients
            )
        }
    }

    /**
     * Retrieves a string representing the food ingredients from a [TheMealRecipe] object.
     * @param recipe The [TheMealRecipe] object.
     *  @return A string representing the food ingredients.
     */
    fun getIngredientsFromTheMealRecipe(recipe: TheMealRecipe): String {
        val ingredients: StringBuilder = StringBuilder()
        for (i in 1..20) {
            val ingredientField = getIngredientField(recipe, i)
            val measureField = getMeasureField(recipe, i)
            if (ingredientField?.isNotEmpty() == true && measureField?.isNotEmpty() == true) {
                ingredients.append("$measureField $ingredientField, ")
            }
        }
        return ingredients.toString()
    }

    /**
     * Retrieves the ingredient field based on the specified index from a [TheMealRecipe] object.
     * @param item The RecipeTheMeal object.
     * @param index The index of the ingredient field to retrieve.
     * @return The value of the ingredient field at the specified index, or null if the index is out of bounds.
     */
    fun getIngredientField(item: TheMealRecipe, index: Int): String? {
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

    /**
     * Retrieves the measure field based on the specified index from a [TheMealRecipe] object.
     * @param item The RecipeTheMeal object.
     * @param index The index of the ingredient field to retrieve.
     * @return The value of the ingredient field at the specified index, or null if the index is out of bounds.
     */
    fun getMeasureField(item: TheMealRecipe, index: Int): String? {
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
}