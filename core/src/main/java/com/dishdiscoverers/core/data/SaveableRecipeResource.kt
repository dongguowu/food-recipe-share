package com.dishdiscoverers.core.data

data class SaveableRecipeResource(
    val recipeResource: RecipeResource,
    val isBookmarked: Boolean
)