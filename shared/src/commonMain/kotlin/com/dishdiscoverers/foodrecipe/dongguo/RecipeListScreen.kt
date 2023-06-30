package com.dishdiscoverers.foodrecipe.dongguo

import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.dishdiscoverers.foodrecipe.dongguo.repository.AuthRepository
import com.dishdiscoverers.foodrecipe.dongguo.repository.Recipe
import com.dishdiscoverers.foodrecipe.dongguo.repository.RecipeRepositoryTheMealAPI
import com.dishdiscoverers.foodrecipe.dongguo.repository.Resource
import com.dishdiscoverers.foodrecipe.dongguo.repository.UserRecipeCommentRepositoryFirebase
import com.dishdiscoverers.foodrecipe.dongguo.screenModel.RecipeScreenModel
import kotlinx.coroutines.launch

class HomeScreen(email: String? = null) : Screen {

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

        // State
        var message by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("dongguo@wu.com") }
        var password by remember { mutableStateOf("dongguo") }
        val state by screenModel.state.collectAsState()
        var categories = screenModel.categories.collectAsState()
        var comments = screenModel.comments.collectAsState()
        message = when (val result = state) {
            is RecipeScreenModel.State.Init -> "Just initialized"
            is RecipeScreenModel.State.Loading -> "Loading"
            is RecipeScreenModel.State.Result -> "Success"
        }

        var queryTitle by remember { mutableStateOf("fish") }
        // Load  data
        LaunchedEffect(currentCompositeKeyHash) {
            screenModel.getAllRecipe()
        }
        var list: MutableList<Recipe> = mutableListOf()
        if (state is RecipeScreenModel.State.Result) {
            list =
                (state as? RecipeScreenModel.State.Result)?.list?.toMutableList() ?: mutableListOf()
        }


        // Layout - Scaffold
        Scaffold(
            topBar = { Text(message) },

            content = { paddingValues ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(paddingValues),
                ) {

                    // Category
                    categories.value?.let {
                        when (it) {
                            is Resource.Failure -> {
                                Text(it.exception.message!!)
                            }

                            Resource.Loading -> {
                                Text("loading....")
                            }

                            is Resource.Success -> {
                                val items: MutableList<String> = mutableListOf()
                                for (item in it.result) {
                                    items.add(item.title)
                                }
//                                val drawerState = rememberDrawerState(DrawerValue.Closed)
//                                val scope = rememberCoroutineScope()
//
//                                val selectedItem = remember { mutableStateOf(items[0]) }
//                                Box() {
//                                    ModalNavigationDrawer(
//                                        drawerState = drawerState,
//                                        drawerContent = {
//                                            ModalDrawerSheet {
//                                                Spacer(Modifier.height(12.dp))
//                                                items.forEach { item ->
//                                                    NavigationDrawerItem(
//                                                        label = { Text(item) },
//                                                        selected = item == selectedItem.value,
//                                                        onClick = {
//                                                            scope.launch { drawerState.close() }
//                                                            selectedItem.value = item
//                                                        },
//                                                        modifier = Modifier.padding(
//                                                            NavigationDrawerItemDefaults.ItemPadding
//                                                        )
//                                                    )
//                                                }
//                                            }
//                                        },
//                                        content = {
//                                            Column(
//                                                modifier = Modifier
//                                                    .padding(16.dp),
//                                                horizontalAlignment = Alignment.CenterHorizontally
//                                            ) {
//                                                Spacer(Modifier.height(20.dp))
//                                                Button(onClick = { scope.launch { drawerState.open() } }) {
//                                                    Text("Category")
//                                                }
//                                            }
//                                        }
//                                    )
//                                }

                            }

                            else -> {
                                Text("some error happens")
                            }
                        }
                    }

                    // User Auth
                    Text("User")
                    Button(
                        onClick = {
                            screenModel.loginUser(email, password)
                        },
                        content = {
                            Text("Login ")
                        }
                    )
                    Button(
                        onClick = {
                            screenModel.signupUser("test", "dongguo@wu.com.3", password)
                        },
                        content = {
                            Text("Add new user")
                        }
                    )
                    Button(
                        onClick = {
                            screenModel.addComment(
                                "IYoAhbQbYIfxMg9IieZRG5F5ThA3",
                                "test",
                                "dongguo@wu.com.3",
                                ""
                            )
                        },
                        content = {
                            Text("Add comment")
                        }
                    )
                    Button(
                        onClick = {
                            screenModel.getComments("test")
                        },
                        content = {
                            Text("get all  comment")
                        }
                    )
                    comments.value?.let {
                        when (it) {
                            is Resource.Failure -> {
                                Text(it.exception.message!!)
                            }

                            Resource.Loading -> {
                                Text("loading....")
                            }

                            is Resource.Success -> {
                                var str = StringBuilder()
                                for (item in it.result) {
                                    str.append(item.id)
                                    str.append("; ")
                                }
                                Text(str.toString())
                            }

                            else -> {
                                Text("some error happens")
                            }
                        }
                    }
//                    SearchRecipe(
//                        description = "Search by recipe title",
//                        search = { screenModel.searchRecipe(it) },
//                        getAll = { screenModel.getAllRecipe() })
//
//                    SearchRecipe(
//                        description = "Search by ingredient name",
//                        search = { screenModel.searchRecipeByIngredient(it) },
//                        getAll = { screenModel.getAllRecipe() })
//
//                    SearchRecipeByInternet(
//                        description = "Search on internet",
//                        search = {
//                            queryTitle = it
//                            screenModel.searchRecipeInternet(it)
//                        },
//                        getAll = {
//                            screenModel.getAllRecipe()
//                        })
                    // list
                    if (state is RecipeScreenModel.State.Result) {
                        LazyColumn {
                            val list =
                                (state as? RecipeScreenModel.State.Result)?.list?.toMutableList()
                                    ?: mutableListOf()

                            if (list.isEmpty()) {
                                item {
                                    RecipeCard(
                                        recipe = null,
                                    )
                                }
                            } else {
                                items(list) { recipe ->
                                    RecipeCard(
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchRecipe(
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
fun SearchRecipeByInternet(
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


@Composable
fun RecipeCard(
    recipe: Recipe? = null,
) {
    Card(
        modifier = Modifier.size(width = 400.dp, height = 800.dp).padding(15.dp),
    ) {
        if (recipe == null) {
            Row {
                Text("No related recipe found. ")
            }
        } else {

            Row {
                Image(
                    url = recipe.imageUrl,
                    modifier = Modifier.size(width = 160.dp, height = 180.dp).padding(15.dp)

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
                    Text(
                        text = recipe.ingredients ?: "",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start,
                    )
                    Spacer(modifier = Modifier.height(60.dp).width(60.dp))
                    Text(
                        text = recipe.instructions ?: "",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start,
                    )
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

