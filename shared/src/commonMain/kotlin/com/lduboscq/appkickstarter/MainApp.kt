package com.lduboscq.appkickstarter

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.dishdiscoverers.foodrecipe.data.HomeScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
public fun MainApp() {
//    Navigator(PersonsListScreen()) { navigator ->
        Navigator(HomeScreen()) { navigator ->
        SlideTransition(navigator)
    }
}
