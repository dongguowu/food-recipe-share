package com.dishdiscoverers.foodrecipe.garett.layout

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dishdiscoverers.foodrecipe.garett.router.Route
import com.dishdiscoverers.foodrecipe.garett.router.screenRouter
import com.dishdiscoverers.foodrecipe.garett.model.User



@Composable
fun MyBottomBar(currentScreen: Route) {
    val navigator = LocalNavigator.currentOrThrow
    BottomAppBar(
        Modifier.fillMaxWidth()){
        IconButton(
            onClick = {
                if (currentScreen !is Route.Ingredient) {
                    val user = User("Garett")
                    val title = "Your Ingredients"
                    navigator.push(screenRouter(Route.Ingredient(user, title)))
                }
            },
            enabled = currentScreen !is Route.Ingredient
        ) {
            Icon(
                imageVector = Icons.Outlined.Home,
                contentDescription = "Ingredient",
            )
        }
        IconButton(
            onClick = {
                if (currentScreen !is Route.Recipe) {
                    val feature = "Super!"
                    val title = "Recipes"
                    navigator.push(screenRouter(Route.Recipe(feature, title)))
                }
            },
            enabled = currentScreen !is Route.Recipe
        ) {
            Icon(
                imageVector = Icons.Outlined.Call,
                contentDescription = "Recipe",
            )
        }
        IconButton(
            onClick = {
                if (currentScreen !is Route.Detail) {
                    val recipeId = "1"
                    val title = "Recipe Details"
                    navigator.push(screenRouter(Route.Detail(recipeId, title)))
                }
            },
            enabled = currentScreen !is Route.Detail
        ) {
            Icon(
                imageVector = Icons.Outlined.AccountBox,
                contentDescription = "Detail",
            )
        }
    }
}