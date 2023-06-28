package com.lduboscq.appkickstarter.mains.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey

data class Recipe(
    val id: String,
    val title: String,
    val servings: Int,
    val instructions: String,
    val imageUrl: String
)