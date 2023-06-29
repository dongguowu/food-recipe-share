package com.dishdiscoverers.foodrecipe.garett.router

import cafe.adriel.voyager.core.screen.Screen
import com.dishdiscoverers.foodrecipe.garett.router.screen.DetailScreen
import com.dishdiscoverers.foodrecipe.garett.router.screen.IngredientScreen
import com.dishdiscoverers.foodrecipe.garett.router.screen.RecipeScreen

fun screenRouter(screen: Route): Screen {
    return when (screen) {
        is Route.Ingredient -> {
            IngredientScreen(user = screen.user, title = screen.title)
        }

        is Route.Recipe -> {
            RecipeScreen(feature = screen.feature, title = screen.title)
        }

        is Route.Detail -> {
            DetailScreen(recipe = screen.recipe, title = screen.title)
        }
    }
}