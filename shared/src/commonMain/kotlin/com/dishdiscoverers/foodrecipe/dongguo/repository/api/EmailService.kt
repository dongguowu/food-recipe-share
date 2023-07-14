package com.dishdiscoverers.foodrecipe.dongguo.repository.api

import com.dishdiscoverers.foodrecipe.dongguo.repository.api.email.Content
import com.dishdiscoverers.foodrecipe.dongguo.repository.api.email.From
import com.dishdiscoverers.foodrecipe.dongguo.repository.api.email.GridEmail
import com.dishdiscoverers.foodrecipe.dongguo.repository.api.email.Personalization
import com.dishdiscoverers.foodrecipe.dongguo.repository.api.email.To
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
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
import okio.IOException

object EmailService {
    private val EMAIL_SERVER = "https://api.sendgrid.com/v3/mail/send"
    private val SENDGRID_API_KEY =
        ""

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
        subject: String,
        body: String
    ) {


        try {
            val response = clientSingleton.request(EMAIL_SERVER) {
                method = HttpMethod.Post
                headers {
                    append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    append("Authorization", "Bearer $SENDGRID_API_KEY")
                }
                setBody(
                    GridEmail(
                        content = listOf(Content(type = "text/plain", value = body)),
                        from = From("dongguo.wu@johnabbottcollege.net"),
                        personalizations = listOf(
                            Personalization(
                                to = listOf(
                                    To("david.dong.guo@gmail.com"),
                                    To("dongguo.wu@johnabbottcollege.net")
                                )
                            )
                        ),
                        subject = subject
                    )
                )
            }
            Napier.i(response.toString())
            Napier.i(response.status.toString())
            Napier.i(response.body())
            Napier.i(response.headers.toString())
        } catch (ex: IOException) {
            Napier.e { ex.toString() }
        }

    }

}

@Serializable
data class Email(
    val from: String,
    val to: String,
    val subject: String,
    val text: String,
    val html: String
)

//            setBody(
//                "{\"personalizations\":
//                [{\"to\": [{\"email\": \"dongguo.wu@johnabbottcollege.net\"}]}],i
//                \"from\": {\"email\": \"dongguo.wu@johnabbottcollege.net\"},\"subject\": \"Sending with SendGrid is Fun\",\"content\": [{\"type\": \"text/plain\", \"value\": \"and easy to do anywhere, even with cURL\"}]}"
//            )