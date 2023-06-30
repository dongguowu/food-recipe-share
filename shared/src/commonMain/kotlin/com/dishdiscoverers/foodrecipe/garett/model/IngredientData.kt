package com.dishdiscoverers.foodrecipe.garett.model

import com.dishdiscoverers.foodrecipe.garett.model.Ingredient

data class IngredientData(
    var id: String? = null,
    var name: String = "",
    var category: String = "",
    var unit: String = "",
    var imageUrl: String = "",
    var ingredient: Ingredient?)