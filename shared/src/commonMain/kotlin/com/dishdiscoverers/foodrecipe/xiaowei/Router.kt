package com.dishdiscoverers.foodrecipe.xiaowei

import cafe.adriel.voyager.core.screen.Screen
import com.dishdiscoverers.foodrecipe.dongguo.HomeScreen
import com.dishdiscoverers.foodrecipe.garett.screen.DetailScreen
import com.dishdiscoverers.foodrecipe.garett.screen.IngredientScreen
import com.dishdiscoverers.foodrecipe.z_showList.HomeScreen

sealed class AllScreens {

    object Login : AllScreens()

    object Register : AllScreens()
    object PreLogin : AllScreens()

    data class Profile(val email: String?) : AllScreens()

    data class Edit(val email: String?) : AllScreens()

    data class DongguoPage(val email: String?) : AllScreens()

    data class Detail(val recipe: String?, val title: String?) : AllScreens()
    object Home : AllScreens()

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

        is AllScreens.Profile ->
            HomeScreen(screen.email ?: "")

        else -> {
            PreLoginScreen()
        }
        is AllScreens.Home ->
            HomeScreen()
    }
}