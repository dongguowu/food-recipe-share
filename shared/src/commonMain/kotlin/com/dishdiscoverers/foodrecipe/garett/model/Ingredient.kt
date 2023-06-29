package com.dishdiscoverers.foodrecipe.garett.model


import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey

data class Ingredient(
    val id: String,
    val name: String,
    val category: String,
    val unit: String,
    val imageUrl: String,
    val nutrientId: String?
)