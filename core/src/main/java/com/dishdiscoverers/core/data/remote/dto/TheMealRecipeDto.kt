package com.dishdiscoverers.core.data.remote.dto

import com.dishdiscoverers.core.domain.model.FoodRecipe
import com.google.gson.annotations.SerializedName

data class TheMealRecipeDto(
    @SerializedName("dateModified")
    val dateModified: String? = null,
    @SerializedName("idMeal")
    val idMeal: String? = null,
    @SerializedName("strArea")
    val strArea: String? = null,
    @SerializedName("strCategory")
    val strCategory: String? = null,
    @SerializedName("strCreativeCommonsConfirmed")
    val strCreativeCommonsConfirmed: String? = null,
    @SerializedName("strDrinkAlternate")
    val strDrinkAlternate: String? = null,
    @SerializedName("strImageSource")
    val strImageSource: String? = null,
    @SerializedName("strIngredient1")
    val strIngredient1: String? = null,
    @SerializedName("strIngredient10")
    val strIngredient10: String? = null,
    @SerializedName("strIngredient11")
    val strIngredient11: String? = null,
    @SerializedName("strIngredient12")
    val strIngredient12: String? = null,
    @SerializedName("strIngredient13")
    val strIngredient13: String? = null,
    @SerializedName("strIngredient14")
    val strIngredient14: String? = null,
    @SerializedName("strIngredient15")
    val strIngredient15: String? = null,
    @SerializedName("strIngredient16")
    val strIngredient16: String? = null,
    @SerializedName("strIngredient17")
    val strIngredient17: String? = null,
    @SerializedName("strIngredient18")
    val strIngredient18: String? = null,
    @SerializedName("strIngredient19")
    val strIngredient19: String? = null,
    @SerializedName("strIngredient2")
    val strIngredient2: String? = null,
    @SerializedName("strIngredient20")
    val strIngredient20: String? = null,
    @SerializedName("strIngredient3")
    val strIngredient3: String? = null,
    @SerializedName("strIngredient4")
    val strIngredient4: String? = null,
    @SerializedName("strIngredient5")
    val strIngredient5: String? = null,
    @SerializedName("strIngredient6")
    val strIngredient6: String? = null,
    @SerializedName("strIngredient7")
    val strIngredient7: String? = null,
    @SerializedName("strIngredient8")
    val strIngredient8: String? = null,
    @SerializedName("strIngredient9")
    val strIngredient9: String? = null,
    @SerializedName("strInstructions")
    val strInstructions: String? = null,
    @SerializedName("strMeal")
    val strMeal: String? = null,
    @SerializedName("strMealThumb")
    val strMealThumb: String? = null,
    @SerializedName("strMeasure1")
    val strMeasure1: String? = null,
    @SerializedName("strMeasure10")
    val strMeasure10: String? = null,
    @SerializedName("strMeasure11")
    val strMeasure11: String? = null,
    @SerializedName("strMeasure12")
    val strMeasure12: String? = null,
    @SerializedName("strMeasure13")
    val strMeasure13: String? = null,
    @SerializedName("strMeasure14")
    val strMeasure14: String? = null,
    @SerializedName("strMeasure15")
    val strMeasure15: String? = null,
    @SerializedName("strMeasure16")
    val strMeasure16: String? = null,
    @SerializedName("strMeasure17")
    val strMeasure17: String? = null,
    @SerializedName("strMeasure18")
    val strMeasure18: String? = null,
    @SerializedName("strMeasure19")
    val strMeasure19: String? = null,
    @SerializedName("strMeasure2")
    val strMeasure2: String? = null,
    @SerializedName("strMeasure20")
    val strMeasure20: String? = null,
    @SerializedName("strMeasure3")
    val strMeasure3: String? = null,
    @SerializedName("strMeasure4")
    val strMeasure4: String? = null,
    @SerializedName("strMeasure5")
    val strMeasure5: String? = null,
    @SerializedName("strMeasure6")
    val strMeasure6: String? = null,
    @SerializedName("strMeasure7")
    val strMeasure7: String? = null,
    @SerializedName("strMeasure8")
    val strMeasure8: String? = null,
    @SerializedName("strMeasure9")
    val strMeasure9: String? = null,
    @SerializedName("strSource")
    val strSource: String? = null,
    @SerializedName("strTags")
    val strTags: String? = null,
    @SerializedName("strYoutube")
    val strYoutube: String? = null
)

fun TheMealRecipeDto.toFoodRecipe(): FoodRecipe? {
    val ingredients = getIngredientsFromTheMealRecipe(this)
    return parseTheMealRecipe(this, ingredients)
}

/**
 * Converts a  [TheMealRecipeDto] from the API response to a [FoodRecipe] object.
 *
 * @param item        The recipe to convert.
 * @param ingredients Additional ingredients for the recipe (optional).
 * @return The converted Recipe object, or null if conversion fails.
 */
fun parseTheMealRecipe(item: TheMealRecipeDto, ingredients: String = ""): FoodRecipe? {
    return item.let {
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
 * Retrieves a string representing the food ingredients from a [TheMealRecipeDto] object.
 * @param recipe The [TheMealRecipeDto] object.
 *  @return A string representing the food ingredients.
 */
fun getIngredientsFromTheMealRecipe(recipe: TheMealRecipeDto): String {
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
 * Retrieves the ingredient field based on the specified index from a [TheMealRecipeDto] object.
 * @param item The RecipeTheMeal object.
 * @param index The index of the ingredient field to retrieve.
 * @return The value of the ingredient field at the specified index, or null if the index is out of bounds.
 */
fun getIngredientField(item: TheMealRecipeDto, index: Int): String? {
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
 * Retrieves the measure field based on the specified index from a [TheMealRecipeDto] object.
 * @param item The RecipeTheMeal object.
 * @param index The index of the ingredient field to retrieve.
 * @return The value of the ingredient field at the specified index, or null if the index is out of bounds.
 */
fun getMeasureField(item: TheMealRecipeDto, index: Int): String? {
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