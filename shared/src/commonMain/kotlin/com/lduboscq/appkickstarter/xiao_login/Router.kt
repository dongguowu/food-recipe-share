package com.lduboscq.appkickstarter.xiao_login

import cafe.adriel.voyager.core.screen.Screen

sealed class AllScreens {

//    object Login : AllScreens()
//
//    object Register : AllScreens()
    object PreLogin : AllScreens()
//    object Administrator : AllScreens()

//    data class Profile(val email: String?) : AllScreens()
//
//    object AboutUs : AllScreens()


}

fun ScreenRouter(screen: AllScreens): Screen {
    return when (screen) {

//        is AllScreens.Login ->
//            LoginScreen()

//
//        is AllScreens.Register ->
//            RegisterScreen()
        is AllScreens.PreLogin ->
            PreLoginScreen()
//        is AllScreens.Administrator ->
//            AdministratorScreen()
//
//        is AllScreens.Profile ->
//            ProfileScreen(screen.email?:"")
//
//        is AllScreens.AboutUs->
//           AboutUsScreen()
    }
}