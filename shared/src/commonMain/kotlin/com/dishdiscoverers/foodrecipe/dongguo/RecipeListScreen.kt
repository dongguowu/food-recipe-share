package com.dishdiscoverers.foodrecipe.dongguo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositeKeyHash
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
import com.dishdiscoverers.foodrecipe.dongguo.repository.AuthRepository
import com.dishdiscoverers.foodrecipe.dongguo.repository.Recipe
import com.dishdiscoverers.foodrecipe.dongguo.repository.RecipeRepositoryTheMealAPI
import com.dishdiscoverers.foodrecipe.dongguo.repository.Resource
import com.dishdiscoverers.foodrecipe.dongguo.repository.UserFavoriteRecipeRepositoryFirebase
import com.dishdiscoverers.foodrecipe.dongguo.repository.UserRecipeCommentRepositoryFirebase
import com.dishdiscoverers.foodrecipe.dongguo.screenModel.RecipeScreenModel
import com.dishdiscoverers.foodrecipe.xiaowei.MyBottomBar

class RecipeListScreen(val email: String? = "dongguo@wu.com") : Screen {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        // Insert repository
        val screenModel = rememberScreenModel() {
            RecipeScreenModel(
                apiRepository = RecipeRepositoryTheMealAPI(),
                authRepository = AuthRepository(),
                commentRepository = UserRecipeCommentRepositoryFirebase(AuthRepository()),
                favoriteRepository = UserFavoriteRecipeRepositoryFirebase(AuthRepository()),
            )
        }

        // State and Message info
        val state by screenModel.state.collectAsState()
        var message by remember { mutableStateOf(email ?: "") }
        message = when (val result = state) {
            is RecipeScreenModel.State.Init -> "Just initialized"
            is RecipeScreenModel.State.Loading -> "Loading"
            is RecipeScreenModel.State.Result -> email ?: ""
        }

        // query title for search food recipe
        var queryTitle by remember { mutableStateOf("fish") }

        // Load  data
        LaunchedEffect(currentCompositeKeyHash) {
            screenModel.searchRecipeInternet("fish")
        }

        var list: MutableList<Recipe> = mutableListOf()
        if (state is RecipeScreenModel.State.Result) {
            list =
                (state as? RecipeScreenModel.State.Result)?.recipeList?.toMutableList()
                    ?: mutableListOf()
        }


        // Layout - Scaffold
        Scaffold(
            topBar = { Text(message) },
            bottomBar = {
                MyBottomBar()
            },

            content = { paddingValues ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(paddingValues),
                ) {

                    // Category
                    screenModel.categories.collectAsState().value?.let {
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
                    SearchRecipeByInternet(description = "Search on internet", search = {
                        queryTitle = it
                        screenModel.searchRecipeInternet(it)
                    }, getAll = {
                        screenModel.getAllRecipe()
                    })

                    // Recipe List
                    if (state is RecipeScreenModel.State.Result) {
                        LazyColumn {
                            val list =
                                (state as? RecipeScreenModel.State.Result)?.recipeList?.toMutableList()
                                    ?: mutableListOf()

                            if (list.isEmpty()) {
                                item {
                                    RecipeCard(updateFavorite = { },
                                        loadComments = { },
                                        addComment = {},
                                        comments = "",
                                        recipe = null,
                                        addFavorite = {},
                                        removeFavorite = {})
                                }
                            } else {
                                for (recipe in list) {
                                    item {
                                        // Favorite
                                        screenModel.getFavorite(
                                            userId = (email ?: ""), recipeId = recipe.id
                                        )
                                        var favoriteChecked by remember { mutableStateOf(false) }
                                        screenModel.favorite.collectAsState().value?.let {
                                            favoriteChecked = when (it) {
                                                is Resource.Success -> {
                                                    it.result
                                                }

                                                else -> {
                                                    false
                                                }
                                            }
                                        }

                                        // Comments
                                        var commentString by remember { mutableStateOf("") }
                                        screenModel.comments.collectAsState().value?.let {
                                            when (it) {
                                                is Resource.Failure -> {
                                                }

                                                Resource.Loading -> {
                                                }

                                                is Resource.Success -> {
                                                    var str = StringBuilder()
                                                    for (item in it.result) {
                                                        str.append("${item.userId} : ${item.text} \n")
                                                    }
                                                    commentString = str.toString()
                                                }
                                            }
                                        }

                                        RecipeCard(
                                            recipe = recipe,
                                            favoriteChecked = favoriteChecked,
                                            updateFavorite = {
                                                screenModel.getFavorite(
                                                    userId = (email ?: ""), recipeId = recipe.id
                                                )
                                            },
                                            addFavorite = {
                                                screenModel.addFavorite(
                                                    userId = (email ?: ""), recipeId = recipe.id
                                                )
                                            },
                                            removeFavorite = {
                                                screenModel.deleteFavorite(
                                                    userId = (email ?: ""), recipeId = recipe.id
                                                )
                                            },
                                            comments = commentString,
                                            loadComments = {
                                                screenModel.getComments(recipe.id)
                                            },
                                            addComment = {
                                                screenModel.addComment(
                                                    userId = email ?: "",
                                                    recipeId = recipe.id,
                                                    text = it,
                                                    imageUrl = null
                                                )
                                            },
                                        )
                                    }
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
fun SearchRecipeByInternet(
    description: String, search: (title: String) -> Unit, getAll: () -> Unit
) {
    Text(description)
    var text by remember { mutableStateOf("") }

    OutlinedTextField(value = text, onValueChange = {
        text = it
        if (text.length <= 2) {
            getAll()
        } else {
            search(text)
        }
    }, label = {
        Icon(
            Icons.Outlined.Search,
            contentDescription = description,
        )
    })
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeCard(
    recipe: Recipe? = null,
    favoriteChecked: Boolean = false,
    updateFavorite: () -> Unit,
    comments: String,
    loadComments: () -> Unit,
    addComment: (text: String) -> Unit,
    addFavorite: () -> Unit,
    removeFavorite: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        updateFavorite()
        Card(
            modifier = Modifier.padding(8.dp).height(IntrinsicSize.Min).wrapContentHeight(),
        ) {
            if (recipe == null) {
                Row {
                    Text("No related recipe found. ")
                }
            } else {

                Row {
                    Column {
                        Image(
                            url = recipe.imageUrl,
                            modifier = Modifier.size(width = 160.dp, height = 180.dp).padding(15.dp)
                        )
                        // Favorite icon
                        var checked by remember { mutableStateOf(favoriteChecked) }
                        IconToggleButton(checked = checked, onCheckedChange = {
                            checked = it
                            if (checked) {
                                addFavorite()
                            } else {
                                removeFavorite()
                            }
                        }) {
                            if (checked) {
                                Icon(
                                    Icons.Filled.Favorite,
                                    contentDescription = "Favorite icon",
                                    tint = Color.Red
                                )
                            } else {
                                Icon(
                                    Icons.Outlined.Favorite, contentDescription = "Favorite icon"
                                )
                            }
                        }
                        // comments
                        var showComments by remember { mutableStateOf(false) }
                        var commentHint by remember { mutableStateOf("Show  comments") }
                        var commentsText by remember { mutableStateOf("") }
                        if (showComments) {
                            Text(commentsText)
                            var newComment by remember { mutableStateOf("") }
                            TextField(
                                value = newComment,
                                onValueChange = {
                                    newComment = it
                                },
                            )
                            Button(onClick = {
                                addComment(newComment)
                            }) {
                                Text("Add comment")
                            }
                        }
                        Button(onClick = {
                            loadComments()
                            showComments = !showComments
                            if (showComments) {
                                commentsText = comments
                                commentHint = "Show comments"
                            } else {
                                commentsText = ""
                                commentHint = "Hide comments"
                            }
                        }) {
                            Text(
                                commentHint, fontSize = 12.sp
                            )
                        }
                    }
                    Column(
                        modifier = Modifier.padding(9.dp, 15.dp, 9.dp, 9.dp),
                    ) {
                        // Title
                        Text(
                            text = recipe.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            textAlign = TextAlign.Start,
                        )
                        // Id for debugging
                        Text(
                            text = recipe.id,
                            fontSize = 9.sp,
                            textAlign = TextAlign.Start,
                        )
                        Spacer(modifier = Modifier.height(20.dp).width(60.dp))

                        // Ingredient
                        var ingredientStr by remember {
                            mutableStateOf(
                                recipe.ingredients?.substring(
                                    0, 50
                                ) ?: ""
                            )
                        }
                        Text(
                            text = ingredientStr,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Start,
                        )
                        var moreIngredient by remember { mutableStateOf(true) }
                        var IngredientHint by remember { mutableStateOf("More") }
                        Button(onClick = {
                            moreIngredient = !moreIngredient
                            if (moreIngredient) {
                                ingredientStr = recipe.ingredients?.substring(0, 50) ?: ""
                                IngredientHint = "More"
                            } else {
                                IngredientHint = "less"
                                ingredientStr = recipe.ingredients ?: ""
                            }
                        }) {
                            Text(
                                text = IngredientHint, fontSize = 12.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp).width(60.dp))

                        // Instructions
                        var instructionStr by remember {
                            mutableStateOf(
                                recipe.instructions?.substring(
                                    0, 50
                                ) ?: ""
                            )
                        }
                        Text(
                            text = instructionStr,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Start,
                        )
                        var moreInstruction by remember { mutableStateOf(true) }
                        var instructionHint by remember { mutableStateOf("More") }
                        Button(onClick = {
                            moreInstruction = !moreInstruction
                            if (moreInstruction) {
                                instructionStr = recipe.instructions?.substring(0, 50) ?: ""
                                instructionHint = "More"
                            } else {
                                instructionHint = "less"
                                instructionStr = recipe.instructions ?: ""
                            }
                        }) {
                            Text(
                                IngredientHint, fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }

}

