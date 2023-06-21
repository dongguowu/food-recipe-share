package com.lduboscq.appkickstarter.main.router

import cafe.adriel.voyager.core.screen.Screen
import com.lduboscq.appkickstarter.main.view.AboutScreen
import com.lduboscq.appkickstarter.main.view.BookStoreHomeScreen
import com.lduboscq.appkickstarter.main.view.DetailScreen
import com.lduboscq.appkickstarter.main.view.ShoppingCartScreen

fun screenRouter(screen: Route): Screen {
    return when (screen) {
        is Route.Home -> {
            BookStoreHomeScreen(user = screen.user)
        }

        is Route.About -> {
            AboutScreen(feature = screen.feature)
        }

        is Route.Detail -> {
            DetailScreen(book = screen.book)
        }

        is Route.ShoppingCart -> {
            ShoppingCartScreen()
        }
    }
}