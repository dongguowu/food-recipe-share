package com.dishdiscoverers.foodrecipe.dongguo

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.screen.Screen
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
        var list by remember { mutableStateOf<List<RecipeFromNinjas>?>(null) }
        LaunchedEffect(true) {
            list = getFriendListFromApi("steak")
        }

        Scaffold(
            topBar = { Text(message) },
            content = {
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


        )
    }

    private @Composable
    fun BookCard(book: RecipeFromNinjas?) {
        Text(book?.title ?: "no title")
    }

    private suspend fun getFriendListFromApi(title: String): List<RecipeFromNinjas>? {
        val apiKey: String = "VNueMdiaDcaQ4gJtjvzcCA==zcDShReg3uMAqtAG"
        val urlString = "https://api.api-ninjas.com/v1/recipe?query=$title"

        val results: List<RecipeFromNinjas> =
            ktorClient.get(urlString) {
                header("X-Api-Key", apiKey)
            }.body()
        return results
    }

}