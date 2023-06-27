package com.lduboscq.appkickstarter

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.lduboscq.appkickstarter.list.ListScreenContent
import com.lduboscq.appkickstarter.list.PersonsListScreen
import com.lduboscq.appkickstarter.xiao_login.PreLoginScreen
import com.dishdiscoverers.foodrecipe.z_showList.HomeScreen
//@OptIn(ExperimentalAnimationApi::class)
//@Composable
//public fun MainApp() {
//    Navigator(PreLoginScreen()) { navigator ->
//        SlideTransition(navigator)
//    }
//}

@OptIn(ExperimentalAnimationApi::class)
@Composable
public fun MainApp() {
//    Navigator(PersonsListScreen()) { navigator ->
    Navigator(HomeScreen()) { navigator ->
        SlideTransition(navigator)
    }
}