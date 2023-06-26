package com.dishdiscoverers.foodrecipe.z_showList

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
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.dishdiscoverers.foodrecipe.dongguo.Recipe
import com.dishdiscoverers.foodrecipe.dongguo.RecipeRepositoryAPI
import com.dishdiscoverers.foodrecipe.dongguo.RecipeRepositoryJsonTheMeal

internal class HomeScreen() : Screen {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        // Insert repository
        val screenModel = rememberScreenModel() {
            RecipeScreenModel(
                localRepository = RecipeRepositoryJsonTheMeal(),
                apiRepository = RecipeRepositoryAPI(),
            )
        }
        val state by screenModel.state.collectAsState()


        var queryTitle by remember { mutableStateOf("fish") }

        // Load  data
        LaunchedEffect(true) {
            screenModel.getAllRecipe()

        }

        var list: MutableList<Recipe> = mutableListOf()
        if (state is RecipeScreenModel.State.Result) {
            list = (state as RecipeScreenModel.State.Result).list as MutableList<Recipe>
        }

        var message by remember { mutableStateOf("") }
        message = when (val result = state) {
            is RecipeScreenModel.State.Init -> "Just initialized"
            is RecipeScreenModel.State.Loading -> "Loading"
            is RecipeScreenModel.State.Result -> "Success"
        }

        // Layout - Scaffold
        Scaffold(
            topBar = { Text(message) },
            content = { paddingValues ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(paddingValues),
                ) {
                    SearchBook(
                        description = "Search by recipe title",
                        search = { screenModel.searchRecipe(it) },
                        getAll = { screenModel.getAllRecipe() })

                    SearchBook(
                        description = "Search by ingredient name",
                        search = { screenModel.searchRecipeByIngredient(it) },
                        getAll = { screenModel.getAllRecipe() })

                    SearchBookByInternet(
                        description = "Search on internet",
                        search = { queryTitle = it },
                        getAll = { screenModel.getAllRecipe() })
                    // list
                    if (state is RecipeScreenModel.State.Result) {
                        LazyColumn {
                            val list = (state as RecipeScreenModel.State.Result).list
                            if (list.isEmpty()) {
                                item {
                                    BookCard(
                                        book = null,
                                    )
                                }
                            } else {
                                items(list) { recipe ->
                                    BookCard(
                                        book = recipe
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBook(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBookByInternet(
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
            if (text.length <= 2) {
                getAll()
            } else {
                search(text)
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

/**

Represents a card component for displaying book information including title,
picture and favorite icon button , add to shopping cart icon button.
@param book The book object to display.
@param addToCart A callback function to handle adding the book to the shopping cart.
 */
@Composable
fun BookCard(
    book: Recipe? = null,
) {
    Card(
        modifier = Modifier.size(width = 400.dp, height = 200.dp).padding(15.dp),
    ) {
        if (book == null) {
            Row {
                Text("Not Found!")
            }
        } else {

            Row {
                Image(
                    url = book.imageUrl,
                    modifier = Modifier.size(width = 160.dp, height = 180.dp).padding(15.dp)

                )
                Column(
                    modifier = Modifier.padding(9.dp, 15.dp, 9.dp, 9.dp),
                ) {
                    Text(
                        text = book.title,
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
                                    contentDescription = "Localized description",
                                    tint = Color.Red
                                )
                            } else {
                                Icon(
                                    Icons.Outlined.Favorite,
                                    contentDescription = "Localized description"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

