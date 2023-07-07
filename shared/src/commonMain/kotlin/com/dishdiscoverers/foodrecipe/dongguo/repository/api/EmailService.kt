package com.dishdiscoverers.foodrecipe.dongguo.repository.api

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

object EmailService {
    private val EMAIL_SERVER = "https://api.postmarkapp.com/email"
    private val POSTMARK_API_TOKEN = "c3f7dbac-b5fa-496b-910f-08a3e4dbe9cb"

    private val clientSingleton = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            level = LogLevel.NONE
            logger = object : Logger {
                override fun log(message: String) {
                    Napier.v(tag = "HTTP Client", message = message)
                }
            }
        }
    }

    suspend fun sendEmail(
        to: String,
        subject: String,
        body: String
    ) {

        val response = clientSingleton.request(EMAIL_SERVER) {
            method = HttpMethod.Post
            headers {
                append(HttpHeaders.Accept, ContentType.Application.Json.toString())
                append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                append("X-Postmark-Server-Token", POSTMARK_API_TOKEN)
            }
            setBody(Email("dongguo.wu@johnabbottcollege.net", "dongguo.wu@johnabbottcollege.net", subject, body))

        }

        Napier.i(response.toString())
    }
}

@Serializable
data class Email(
    val from: String,
    val to: String,
    val subject: String,
    val body: String,
    val MessageStream: String = "outbound"
)