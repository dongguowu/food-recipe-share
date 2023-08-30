package com.dishdiscoverers.core.common

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class ResourceTest {

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }



    @Test
    fun `test error response`() {
        val mockResponse = MockResponse()
            .setResponseCode(500)
        mockWebServer.enqueue(mockResponse)

        // Call your API or perform the operation that returns a Resource
        val apiClient = createApiClient2("https://www.themealdb.com/api/json/v1/1/searc")
        val result = apiClient.fetchData("")

        // Assert that the result is a Failure
        assert(result is Resource.Failure)
    }

    private fun createApiClient2(urlString: String): YourApiClient2 {
        val okHttpClient = OkHttpClient()
        return YourApiClient2(urlString, okHttpClient)
    }

    class YourApiClient2(private val urlString: String, private val httpClient: OkHttpClient) {

        @Throws(IOException::class)
        fun fetchData(query: String): Resource<String> {
            val url = urlString.toHttpUrlOrNull()!!.newBuilder()
                .addQueryParameter("query", query)
                .build()

            val request = Request.Builder()
                .url(url)
                .build()

            val response: Response = httpClient.newCall(request).execute()

            return if (response.isSuccessful) {
                val responseData = response.body?.string() ?: ""
                Resource.Success(responseData)
            } else {
                Resource.Failure(IOException("Failed to fetch data"))
            }
        }
    }

}
