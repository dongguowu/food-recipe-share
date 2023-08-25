package com.dishdiscoverers.core.data

class UserRecipeResource internal constructor(
    val id: String,
    val title: String,
    val isSaved: Boolean,
){
    constructor(recipeResource: RecipeResource, userData: UserData): this(
        id = recipeResource.id,
        title = recipeResource.title,
        isSaved = userData.bookmarkedRecipeResources.contains(recipeResource.id)
    )

    fun List<RecipeResource>.mapToUserRecipeResources(userData: UserData): List<UserRecipeResource> {
        return map {UserRecipeResource(it, userData)}
    }
}