package com.dishdiscoverers.foodrecipe.dongguo

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class TheMealRepositoryImpl() : TheMealRepository {

        private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    override suspend fun getRecipes(title: String): Resource<List<RecipeFromTheMealDB>> {
        val urlString = "https://www.themealdb.com/api/json/v1/1/search.php?s=$title"
        return try {
            Resource.Success(
                (httpClient.get(urlString).body() as TheMealResponse).meals
            )

        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)

        }
    }
}