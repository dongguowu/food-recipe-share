package com.lduboscq.appkickstarter.main.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.dishdiscoverers.foodrecipe.dongguo.RecipeRepositoryJsonTheMeal
import com.dishdiscoverers.foodrecipe.dongguo.RecipeRepositoryNinjasJson
import com.dishdiscoverers.foodrecipe.dongguo.RecipeRepositoryTheMealAPI
import com.lduboscq.appkickstarter.main.data.Recipe
import com.lduboscq.appkickstarter.main.data.RecipeRepositoryJson
import com.lduboscq.appkickstarter.main.router.Route
import com.lduboscq.appkickstarter.main.router.screenRouter
import com.lduboscq.appkickstarter.main.screenModel.RecipeScreenModel

import com.lduboscq.appkickstarter.ui.Image
import com.lduboscq.appkickstarter.ui.MyBottomBar
import com.lduboscq.appkickstarter.ui.MyTopBar

internal class RecipeScreen(var feature: String, val title: String) : Screen {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel() {
            com.dishdiscoverers.foodrecipe.z_showList.RecipeScreenModel(
                localRepository = RecipeRepositoryJsonTheMeal(),
                secondLocalRepository = RecipeRepositoryNinjasJson(),
                apiRepository = RecipeRepositoryTheMealAPI(),
            )
        }

        // Load shopping cart data
        LaunchedEffect(true) {
            screenModel.getAllRecipe()
        }

        val state by screenModel.state.collectAsState()


        var queryTitle by remember { mutableStateOf("fish") }

        // Load  data
        LaunchedEffect(true) {
            screenModel.getAllRecipe()

        }

        var list: MutableList<com.dishdiscoverers.foodrecipe.dongguo.Recipe> = mutableListOf()
        if (state is com.dishdiscoverers.foodrecipe.z_showList.RecipeScreenModel.State.Result) {
            list =
                (state as? com.dishdiscoverers.foodrecipe.z_showList.RecipeScreenModel.State.Result)?.list?.toMutableList() ?: mutableListOf()
        }

        var message by remember { mutableStateOf("") }


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
                    if (state is com.dishdiscoverers.foodrecipe.z_showList.RecipeScreenModel.State.Result) {
                        LazyColumn {
                            val list =
                                (state as? com.dishdiscoverers.foodrecipe.z_showList.RecipeScreenModel.State.Result)?.list?.toMutableList()
                                    ?: mutableListOf()

                            if (list.isEmpty()) {
                                item {
                                    com.dishdiscoverers.foodrecipe.z_showList.RecipeCard(
                                        recipe = null,
                                    )
                                }
                            } else {
                                items(list) { recipe ->
                                    com.dishdiscoverers.foodrecipe.z_showList.RecipeCard(
                                        recipe = recipe
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