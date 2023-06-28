package com.lduboscq.appkickstarter.main.screen


import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.dishdiscoverers.foodrecipe.dongguo.RecipeRepositoryJsonTheMeal
import com.dishdiscoverers.foodrecipe.dongguo.RecipeRepositoryNinjasJson
import com.dishdiscoverers.foodrecipe.dongguo.RecipeRepositoryTheMealAPI
import com.dishdiscoverers.foodrecipe.garett.theme.ui.md_theme_dark_primaryContainer
import com.lduboscq.appkickstarter.main.data.Ingredient
import com.lduboscq.appkickstarter.main.data.RecipeRepository
import com.lduboscq.appkickstarter.main.data.RecipeRepositoryJson
import com.lduboscq.appkickstarter.main.router.Route
import com.lduboscq.appkickstarter.main.screenModel.RecipeScreenModel
import com.lduboscq.appkickstarter.main.screenModel.RecipeScreenModel.*
import com.lduboscq.appkickstarter.mains.model.User
import com.lduboscq.appkickstarter.ui.MyBottomBar
import com.lduboscq.appkickstarter.ui.MyTopBar


internal class IngredientScreen(var user: User? = null, val title: String = "Your Ingredients") : Screen {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        // Insert repository
        val screenModel = rememberScreenModel() {
            com.dishdiscoverers.foodrecipe.z_showList.RecipeScreenModel(
                localRepository = RecipeRepositoryJsonTheMeal(),
                secondLocalRepository = RecipeRepositoryNinjasJson(),
                apiRepository = RecipeRepositoryTheMealAPI(),
            )
        }
        val state by screenModel.state.collectAsState()

        // Load  data
        LaunchedEffect(true) {
            screenModel.getAllRecipe()
        }
        var ingredientList: List<Ingredient>? = null
//        if (state is State.Result) {
//            ingredientList =
//                (state as State.Result).ingredientList
//        }

        var enabled by remember { mutableStateOf(true)}

        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()

        var ingredients =
            arrayOf("chicken", "beef", "eggs", "onions", "tomatoes", "flour", "olive oil",
                "vegetable oil", "oregano", "turmeric")
        val selectedIngredients =
            remember { mutableStateListOf<String>() }
        val checkedState =
            remember { mutableStateListOf<Boolean>()
                .apply { repeat(ingredients.size) { add(false) } } }
        val ingredientButton = { mutableStateOf(true) }

        // Layout - Scaffold
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

//            Scaffold(
//                topBar = { MyTopBar(currentScreen = Route.Ingredient(user, title)) },
//                bottomBar = { MyBottomBar(currentScreen = Route.Ingredient(user, title)) },
//                content = { paddingValues ->
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier.padding(paddingValues),
//                    ) {
//                        // list
//                        if (state is State.Result) {
//                            LazyColumn {
//                                item {
//                                    Text("Select the ingredients in your kitchen!")
//                                }
//                                for (item in (state as State.Result).ingredientList) {
//                                    item {
//                                        IngredientButton(ingredient = item)
//                                    }
//                                }
//                                items(ingredientList!!.size) { index ->
//                                    val isChecked = checkedState[index]
//                                    // Adapted from https://semicolonspace.com/jetpack-compose-checkbox/
//                                    Row {
//                                        Checkbox(
//                                            checked = isChecked,
//                                            onCheckedChange = { checked ->
//                                                // Update the checked state of the ingredient
//                                                checkedState[index] = checked
//                                                // Add or remove the ingredient from the list of selected ingredients
//                                                if (checked) {
//                                                    selectedIngredients.add(ingredientList[index].toString())
//                                                } else {
//                                                    selectedIngredients.remove(ingredientList[index].toString())
//                                                }
//                                            }
//                                        )
//                                        // Display the name of the ingredient
//                                        androidx.compose.material.Text(
//                                            text = ingredientList[index].toString(),
//                                            style = MaterialTheme.typography.bodyMedium,
//                                            modifier = Modifier.padding(start = 16.dp)
//                                        )
//                                    }
//                                }
//                            }
//                        }
//                    }
//                },
//            )
    }
}
@Composable
fun IngredientButton(ingredient: Ingredient) {
    Button(onClick = { /* Do something! */ }, colors = ButtonDefaults.textButtonColors(
        containerColor = md_theme_dark_primaryContainer
    )) {
        Text(ingredient.name)
    }
}


@Composable
fun IngredientCardList(
    ingredientList: List<Ingredient>?,

) {
    val navigator = LocalNavigator.currentOrThrow
    Card(
        modifier = Modifier.size(width = 150.dp, height = 100.dp).padding(15.dp),
    ) {
        Row {
            Column(
                modifier = Modifier.padding(9.dp, 15.dp, 9.dp, 9.dp),
            ) {

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchIngredient(
    description: String,
    search: (title: String) -> Unit,
    getAll: () -> Unit
) {
    Text(description)
    var text by remember { mutableStateOf("") }
    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            if (it.length >= 3) {
                search(it)
            } else {
                getAll()
            }

        },
        label = {
            Icon(
                Icons.Outlined.Search,
                contentDescription = description,
            )
        }
    )
}