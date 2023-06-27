package com.dishdiscoverers.foodrecipe.dongguo

import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.date.*
import kotlinx.coroutines.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class FetchRecipe {

    private val httpClient = HttpClient {
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

    private val apiKey: String = "VNueMdiaDcaQ4gJtjvzcCA==zcDShReg3uMAqtAG"

    @Throws(Exception::class)
    suspend fun search(title: String): List<Recipe> {
        val urlString = "https://api.api-ninjas.com/v1/recipe?query=$title"

        val results: List<RecipeFromNinjas> =
            httpClient.get(urlString) {
                header("X-Api-Key", apiKey)
            }.body()
        val recipes: MutableList<Recipe> = mutableListOf()
        for (item in results) {
            val recipe = Recipe(
                id = "4",
                title = item.title,
                servings = item.servings.substringBefore(" ").toIntOrNull() ?: 1,
                instructions = item.instructions,
                imageUrl = "",
                ingredients = item.ingredients,
            )
            println(recipe.toString())
            recipes.add(recipe)
        }
        return recipes
    }
}

