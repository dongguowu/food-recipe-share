package com.lduboscq.appkickstarter

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
<<<<<<< HEAD
import com.dishdiscoverers.foodrecipe.garett.router.screen.RecipeScreen
=======
import com.dishdiscoverers.foodrecipe.dongguo.TestScreen
>>>>>>> 98751e64c2b7879518628e8d09b577c67a2bd0cf
import com.dishdiscoverers.foodrecipe.xiaowei.PreLoginScreen
import com.dishdiscoverers.foodrecipe.z_showList.HomeScreen
import com.lduboscq.appkickstarter.main.screen.IngredientScreen
import com.lduboscq.appkickstarter.main.screen.RecipeScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
public fun MainApp() {
//    Navigator(PersonsListScreen()) { navigator ->
<<<<<<< HEAD
//    Navigator(PreLoginScreen()) { navigator ->
    Navigator(RecipeScreen()) { navigator ->
//        Navigator(HomeScreen()) { navigator ->
=======
    Navigator(HomeScreen()) { navigator ->
//    Navigator(HomeScreen()) { navigator ->
>>>>>>> 98751e64c2b7879518628e8d09b577c67a2bd0cf
        SlideTransition(navigator)
    }
}
