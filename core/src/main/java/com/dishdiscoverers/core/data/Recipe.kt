package com.dishdiscoverers.core.data

data class Recipe(
    val id: String,
    val title: String,
    val servings: Int = 1,
    val instructions: String = "",
    val imageUrl: String = "",
    val ingredients: String = "",
)