package com.lduboscq.appkickstarter.main.screen


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.lduboscq.appkickstarter.main.data.Ingredient
import com.lduboscq.appkickstarter.main.data.RecipeRepositoryJson
import com.lduboscq.appkickstarter.main.router.Route
import com.lduboscq.appkickstarter.main.screenModel.RecipeScreenModel
import com.lduboscq.appkickstarter.mains.model.User
import com.lduboscq.appkickstarter.ui.Image
import com.lduboscq.appkickstarter.ui.MyBottomBar
import com.lduboscq.appkickstarter.ui.MyTopBar

internal class IngredientScreen(var user: User? = null, val title: String = "Your Ingredients") : Screen {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        // Insert repository
        val screenModel = rememberScreenModel() {
            RecipeScreenModel(
                repository = RecipeRepositoryJson()
            )
        }
        val state by screenModel.state.collectAsState()

        // Load  data
        LaunchedEffect(true) {
            screenModel.getAllIngredient()
        }
        var ingredientList: List<Ingredient>? = null
        if (state is RecipeScreenModel.State.Result.IngredientResult) {
            ingredientList =
                (state as RecipeScreenModel.State.Result.IngredientResult).ingredientList
        }

        val selectedIngredients =
            remember { mutableStateListOf<String>() }
        val checkedState =
            remember { mutableStateListOf<Boolean>()
                .apply {
                    if (ingredientList != null) {
                        repeat(ingredientList.size) { add(false) }
                    }
                } }


        // Load shopping cart data
        LaunchedEffect(true) {
            screenModel.getAllIngredient()
        }

        // Layout - Scaffold
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        Scaffold(
            topBar = { MyTopBar(currentScreen = Route.Ingredient(user, title)) },
            bottomBar = { MyBottomBar(currentScreen = Route.Ingredient(user, title)) },
            content = { paddingValues ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(paddingValues),
                ) {
                    // list
                    if (state is RecipeScreenModel.State.Result.IngredientResult) {
                        LazyColumn {
                            item {
                                Text ("Select the ingredients in your kitchen!")
                            }
                            for (item in (state as RecipeScreenModel.State.Result.IngredientResult).ingredientList) {
                                item {
                                    IngredientCard(
                                        ingredient = item
                                    )
                                }
                            }
                        }
                    }
                }
            },
        )
    }
}


/**

Represents a card component for displaying book information including title,
picture and favorite icon button , add to shopping cart icon button.
@param book The book object to display.
@param addToCart A callback function to handle adding the book to the shopping cart.
 */
@Composable
fun IngredientCard(
    ingredient: Ingredient,
//    selectedIngredients:
//    addIngredient: (ingredient: Ingredient) -> Unit,
//    deleteIngredientById: (ingredientId: String) -> Unit
) {
    val navigator = LocalNavigator.currentOrThrow
    Card(
        modifier = Modifier.size(width = 150.dp, height = 100.dp).padding(15.dp),
    ) {
        Row {
            Column(
                modifier = Modifier.padding(9.dp, 15.dp, 9.dp, 9.dp),
            ) {
                Text(

                    text = ingredient.name,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start
                )
                var checked by remember { mutableStateOf(false) }
                IconToggleButton(checked = checked, onCheckedChange = { checked = it }) {
                    if (checked) {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = "Favorite icon",
                            tint = Color.Red
                        )
                    } else {
                        Icon(
                            Icons.Outlined.Favorite,
                            contentDescription = "Favorite icon"
                        )
                    }
                }
            }
        }
    }
}