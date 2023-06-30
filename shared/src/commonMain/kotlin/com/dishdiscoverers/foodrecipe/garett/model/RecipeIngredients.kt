package com.dishdiscoverers.foodrecipe.garett.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey

data class RecipeIngredients(
    val recipeId: String,
    val ingredientId: String,
    val quantityOfIngredient: Int
)