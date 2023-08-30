package com.dishdiscoverers.core.data.model

data class UserData(
    var userId: String,
    val bookmarkedFoodRecipes: Set<String>
)