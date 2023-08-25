package com.dishdiscoverers.core.data.model

data class SaveableRecipeResource(
    val recipeResource: RecipeResource,
    val isBookmarked: Boolean
)