package com.lduboscq.appkickstarter

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.dishdiscoverers.foodrecipe.dongguo.ContactUsScreen
import com.dishdiscoverers.foodrecipe.dongguo.RecipeListScreen
import com.dishdiscoverers.foodrecipe.xiaowei.PreLoginScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
public fun MainApp() {
//    Navigator(PersonsListScreen()) { navigator ->
//    Navigator(PreLoginScreen()) { navigator ->
//    Navigator(RecipeScreen()) { navigator ->
//    Navigator(RecipeListScreen()) { navigator ->
        Navigator(ContactUsScreen()) { navigator ->
        SlideTransition(navigator)
    }
}