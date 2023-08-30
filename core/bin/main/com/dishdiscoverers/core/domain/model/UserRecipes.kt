package com.dishdiscoverers.core.domain.model

class UserRecipes internal constructor(
    val id: String,
    val title: String,
    val isSaved: Boolean,
){
    constructor(foodRecipe: FoodRecipe, userData: UserData): this(
        id = foodRecipe.id,
        title = foodRecipe.title,
        isSaved = userData.bookmarkedFoodRecipes.contains(foodRecipe.id)
    )

    fun List<FoodRecipe>.mapToUserRecipes(userData: UserData): List<UserRecipes> {
        return map { UserRecipes(it, userData) }
    }
}