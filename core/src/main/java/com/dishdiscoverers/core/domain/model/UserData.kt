package com.dishdiscoverers.core.domain.model

data class UserData(
    var userId: String,
    val bookmarkedFoodRecipes: Set<String>
)