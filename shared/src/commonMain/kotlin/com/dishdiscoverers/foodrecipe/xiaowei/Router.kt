package com.dishdiscoverers.foodrecipe.xiaowei

import cafe.adriel.voyager.core.screen.Screen

sealed class AllScreens {

    object Login : AllScreens()

    object Register : AllScreens()
    object PreLogin : AllScreens()
//    object Administrator : AllScreens()

    data class Profile(val email: String?) : AllScreens()

    //
    data class Edit(val email: String?) : AllScreens()


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
//        is AllScreens.Administrator ->
//            AdministratorScreen()
//
        is AllScreens.Profile ->
            ProfileScreen(screen.email ?: "")

    }
}