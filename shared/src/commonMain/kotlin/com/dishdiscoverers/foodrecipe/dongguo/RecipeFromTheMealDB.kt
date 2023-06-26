package com.dishdiscoverers.foodrecipe.dongguo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TheMealResponse(
    @SerialName("meals")
    val meals: List<RecipeFromTheMealDB>
)

@Serializable
data class RecipeFromTheMealDB(
    @SerialName("dateModified")
    val dateModified: String? = null,
    @SerialName("idMeal")
    val idMeal: String,
    @SerialName("strArea")
    val strArea: String,
    @SerialName("strCategory")
    val strCategory: String,
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
    val strMeal: String,
    @SerialName("strMealThumb")
    val strMealThumb: String,
    @SerialName("strMeasure1")
    val strMeasure1: String,
    @SerialName("strMeasure10")
    val strMeasure10: String,
    @SerialName("strMeasure11")
    val strMeasure11: String,
    @SerialName("strMeasure12")
    val strMeasure12: String,
    @SerialName("strMeasure13")
    val strMeasure13: String,
    @SerialName("strMeasure14")
    val strMeasure14: String,
    @SerialName("strMeasure15")
    val strMeasure15: String,
    @SerialName("strMeasure16")
    val strMeasure16: String,
    @SerialName("strMeasure17")
    val strMeasure17: String,
    @SerialName("strMeasure18")
    val strMeasure18: String,
    @SerialName("strMeasure19")
    val strMeasure19: String,
    @SerialName("strMeasure2")
    val strMeasure2: String,
    @SerialName("strMeasure20")
    val strMeasure20: String,
    @SerialName("strMeasure3")
    val strMeasure3: String,
    @SerialName("strMeasure4")
    val strMeasure4: String,
    @SerialName("strMeasure5")
    val strMeasure5: String,
    @SerialName("strMeasure6")
    val strMeasure6: String,
    @SerialName("strMeasure7")
    val strMeasure7: String,
    @SerialName("strMeasure8")
    val strMeasure8: String,
    @SerialName("strMeasure9")
    val strMeasure9: String,
    @SerialName("strSource")
    val strSource: String,
    @SerialName("strTags")
    val strTags: String? = null,
    @SerialName("strYoutube")
    val strYoutube: String
)