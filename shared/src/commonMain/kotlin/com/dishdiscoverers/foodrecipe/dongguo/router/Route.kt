package com.lduboscq.appkickstarter.main.router

import com.dishdiscoverers.foodrecipe.dongguo.data.BookData
import com.dishdiscoverers.foodrecipe.dongguo.data.Message
import com.dishdiscoverers.foodrecipe.dongguo.model.DongguoUser


sealed class Route {
    data class Home(val user: DongguoUser?) : Route()
    data class About(val feature: Message) : Route()
    data class Detail(val book: BookData?) : Route()
    data class ShoppingCart(val quantity: Int) : Route()
}