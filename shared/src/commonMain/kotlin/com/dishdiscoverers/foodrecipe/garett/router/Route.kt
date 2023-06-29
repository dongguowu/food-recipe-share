package com.lduboscq.appkickstarter.main.router

import com.lduboscq.appkickstarter.mains.model.User


sealed class Route {
    data class Ingredient(val user: User?, val title: String) : Route()
    data class Recipe(val feature: String, val title: String) : Route()
    data class Detail(val recipe: String, val title: String) : Route()
}