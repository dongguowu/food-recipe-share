package com.lduboscq.appkickstarter.main.router

import cafe.adriel.voyager.core.screen.Screen
import com.lduboscq.appkickstarter.main.router.screen.DetailScreen
import com.lduboscq.appkickstarter.main.router.screen.IngredientScreen
import com.lduboscq.appkickstarter.main.router.screen.RecipeScreen

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