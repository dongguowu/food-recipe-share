package com.dishdiscoverers.foodrecipe.garett.router.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dishdiscoverers.foodrecipe.dongguo.AuthRepository
import com.dishdiscoverers.foodrecipe.dongguo.Recipe
import com.dishdiscoverers.foodrecipe.dongguo.RecipeRepositoryTheMealAPI
import com.dishdiscoverers.foodrecipe.dongguo.RecipeScreenModel
import com.dishdiscoverers.foodrecipe.dongguo.UserRecipeCommentRepositoryFirebase
import com.dishdiscoverers.foodrecipe.garett.layout.MyBottomBar
import com.dishdiscoverers.foodrecipe.garett.layout.MyTopBar
import com.dishdiscoverers.foodrecipe.garett.router.Route
import com.dishdiscoverers.foodrecipe.garett.router.screenRouter
import com.lduboscq.appkickstarter.ui.Image

internal class RecipeScreen(var feature: String = "Super!", val title: String = "Recipes") :
    Screen {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        // Insert repository
        val screenModel = rememberScreenModel() {
            RecipeScreenModel(
                apiRepository = RecipeRepositoryTheMealAPI(),
                authRepository = AuthRepository(),
                commentRepository = UserRecipeCommentRepositoryFirebase(AuthRepository())

            )
        }
        val state by screenModel.state.collectAsState()

        // Load  data
        LaunchedEffect(true) {
            screenModel.getAllRecipe()
        }
        var recipeList: List<Recipe>? = null
        if (state is RecipeScreenModel.State.Result) {
            recipeList =
                (state as RecipeScreenModel.State.Result).list
        }


        // Load shopping cart data
        LaunchedEffect(true) {
            screenModel.getAllRecipe()
        }


        // Layout - Scaffold
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        Scaffold(
            topBar = { MyTopBar(currentScreen = Route.Recipe(feature, title)) },
            bottomBar = { MyBottomBar(currentScreen = Route.Recipe(feature, title)) },
            content = { paddingValues ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(paddingValues),
                ) {
//                   SearchBook()


                    // list
                    if (state is RecipeScreenModel.State.Result) {
                        LazyColumn {
                            item {
                                Text("Here's some freshly made recipes just for you!")
                            }
                            for (item in (state as RecipeScreenModel.State.Result).list) {
                                item {
                                    RecipeCard(
                                        recipe = item
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

Card component that displays a recipe object
 */
@Composable
fun RecipeCard(
    recipe: Recipe,
) {
    val navigator = LocalNavigator.currentOrThrow
    val string = "I am a recipe"
    val title = "Recipe Details"
    Card(
        modifier = Modifier.size(width = 400.dp, height = 200.dp).padding(15.dp),
    ) {
        Row {
            Image(
                url = recipe.imageUrl,
                modifier = Modifier.size(width = 120.dp, height = 180.dp).padding(15.dp)
                    .clickable(onClick = {
                        navigator.push(screenRouter(Route.Detail(string, title)))
                    }
                    )
            )
            Column(
                modifier = Modifier.padding(9.dp, 15.dp, 9.dp, 9.dp),
            ) {
                Text(
                    text = recipe.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Start,
                )

                Spacer(modifier = Modifier.height(60.dp).width(60.dp))

                Row {
                    // Favorite icon
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
}