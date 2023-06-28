package com.lduboscq.appkickstarter

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.dishdiscoverers.foodrecipe.xiaowei.PreLoginScreen
import com.dishdiscoverers.foodrecipe.z_showList.HomeScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
public fun MainApp() {
//    Navigator(PersonsListScreen()) { navigator ->
//    Navigator(PreLoginScreen()) { navigator ->
    Navigator(HomeScreen()) { navigator ->
        SlideTransition(navigator)
    }
}
