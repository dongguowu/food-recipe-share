package com.dishdiscoverers.foodrecipe.dongguo

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import cafe.adriel.voyager.core.screen.Screen
import com.dishdiscoverers.foodrecipe.z_showList.Image
import com.dishdiscoverers.foodrecipe.z_showList.SearchBook
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class TestScreen() : Screen {
    private val ktorClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            level = LogLevel.INFO
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        var message by remember { mutableStateOf("init") }
        var title by remember { mutableStateOf("egg") }
        var list by remember { mutableStateOf(emptyList<Recipe>()) }
        LaunchedEffect(Unit) {
            list = getRefreshRecipeList(title)
        }

        Scaffold(
            topBar = { Text(message) },
            content = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    SearchBook(
                        description = "search recipe on the meal",
                        search = {
                            title = it
                            message = title
                        },
                        getAll = {}
                    )

                    LazyColumn {
                        if (list == null || list!!.isEmpty()) {
                            item {
                                BookCard(
                                    book = null,
                                )
                            }
                        } else {
                            items(list!!) { recipe ->
                                BookCard(
                                    book = recipe
                                )
                            }
                        }
                    }
                }
            }


        )
    }


    private suspend fun getRefreshRecipeList(title: String): List<Recipe> {
        return getRecipeFromTheMealApi(title) ?: emptyList()

    }


    private suspend fun getRecipeFromTheMealApi(title: String): List<Recipe>? {
        val urlString = "https://www.themealdb.com/api/json/v1/1/search.php?s=$title"

        val results: TheMealResponse = ktorClient.get(urlString).body()
        val recipes: MutableList<Recipe> = mutableListOf()
        for (item in results.meals ?: emptyList()) {
            val recipe = Recipe(
                id = item.idMeal ?: "",
                title = item.strMeal ?: "",
                servings = 1,
                instructions = item?.strInstructions ?: "",
                imageUrl = item.strMealThumb ?: "",
                ingredients = "",
            )
            recipes.add(recipe)
        }
        return recipes
    }
}

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
                        IconToggleButton(
                            checked = checked,
                            onCheckedChange = { checked = it }) {
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
