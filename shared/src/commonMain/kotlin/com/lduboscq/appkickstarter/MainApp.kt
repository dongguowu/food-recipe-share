package com.lduboscq.appkickstarter

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.dishdiscoverers.foodrecipe.dongguo.TestScreen
import com.dishdiscoverers.foodrecipe.xiaowei.PreLoginScreen
import com.dishdiscoverers.foodrecipe.z_showList.HomeScreen
import com.lduboscq.appkickstarter.main.screen.IngredientScreen
import com.lduboscq.appkickstarter.main.screen.RecipeScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
public fun MainApp() {
//    Navigator(PersonsListScreen()) { navigator ->
    Navigator(HomeScreen()) { navigator ->
//    Navigator(HomeScreen()) { navigator ->
        SlideTransition(navigator)
    }
}
