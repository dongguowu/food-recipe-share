package com.lduboscq.appkickstarter.main.model

import com.lduboscq.appkickstarter.mains.model.Ingredient

data class IngredientData(
    var id: String? = null,
    var name: String = "",
    var category: String = "",
    var unit: String = "",
    var imageUrl: String = "",
    var ingredient: Ingredient?)