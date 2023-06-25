package com.dishdiscoverers.foodrecipe.dongguo

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.todayIn
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

fun daysUntilNewYear(): Int {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val closestNewYear = LocalDate(today.year + 1, 1, 1)
    return today.daysUntil(closestNewYear)
}

class Greeting {

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    private val apiKey: String = "VNueMdiaDcaQ4gJtjvzcCA==zcDShReg3uMAqtAG"

    @Throws(Exception::class)
    suspend fun greet(title: String): String {
        val urlString = "https://api.api-ninjas.com/v1/recipe?query=$title"
        val headers = mapOf("X-Api-Key" to apiKey)

        val list: List<RecipeRepositoryJson.RecipeFromNinjas> =
            httpClient.get(urlString){
                header("X-Api-Key", apiKey)
            }.body()
        for(item in list) {
            println(item.title)
        }
        return "Guess ${list[0].title}"
    }
}

@Serializable
data class RocketLaunch(
    @SerialName("flight_number")
    val flightNumber: Int,
    @SerialName("name")
    val missionName: String,
    @SerialName("date_utc")
    val launchDateUTC: String,
    @SerialName("success")
    val launchSuccess: Boolean?,
)