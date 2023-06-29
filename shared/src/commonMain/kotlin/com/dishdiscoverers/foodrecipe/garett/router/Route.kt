package com.dishdiscoverers.foodrecipe.garett.router

import com.dishdiscoverers.foodrecipe.garett.model.User


sealed class Route {
    data class Ingredient(val user: User?, val title: String) : Route()
    data class Recipe(val feature: String, val title: String) : Route()
    data class Detail(val recipe: String, val title: String) : Route()
}