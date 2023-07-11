package com.dishdiscoverers.foodrecipe.xiaowei

import cafe.adriel.voyager.core.screen.Screen
import com.dishdiscoverers.foodrecipe.dongguo.RecipeListScreen
import com.dishdiscoverers.foodrecipe.garett.screen.DetailScreen

sealed class AllScreens {

    object Login : AllScreens()

    object Register : AllScreens()
    object PreLogin : AllScreens()

    data class Profile(val email: String?) : AllScreens()

    data class Edit(val email: String?) : AllScreens()

    data class Detail(val recipe: String?, val title: String?) : AllScreens()
    data class Home(val email: String?) : AllScreens()

}

fun ScreenRouter(screen: AllScreens): Screen {
    return when (screen) {

        is AllScreens.Login ->
            LoginScreen()

        is AllScreens.Edit ->
            EditScreen(screen.email ?: "")

        is AllScreens.Register ->
            RegisterScreen()

        is AllScreens.PreLogin ->
            PreLoginScreen()

        is AllScreens.Detail ->
            DetailScreen(screen.recipe ?: "", screen.title ?: "")

        is AllScreens.Home ->
            RecipeListScreen(screen.email ?: "")
        is AllScreens.Profile ->
            ProfileScreen(screen.email ?:"")

        else -> {
            PreLoginScreen()
        }

    }
}