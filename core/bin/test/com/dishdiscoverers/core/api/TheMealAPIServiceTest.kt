package com.dishdiscoverers.core.api;

import com.dishdiscoverers.core.data.remote.TheMealAPIService
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.common.truth.Truth.assertThat


class TheMealAPIServiceTest {
    private lateinit var retrofit: TheMealAPIService
    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        retrofit = Retrofit.Builder()
            .baseUrl(server.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TheMealAPIService::class.java)
    }

    private fun enqueueMockResponse(
        fileName: String
    ){
        val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
        val  source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        mockResponse.setBody(source.readString(Charsets.UTF_8))
        server.enqueue(mockResponse)
    }

    @Test
    fun searchRecipes_sentRequest_receivedExpected() {
        runBlocking {
            enqueueMockResponse("theMealAPIRecipeResponse.json")
            val responseBody = retrofit.searchRecipes("egg").body()
            val request = server.takeRequest()

            assertThat(responseBody).isNotNull()

            val expectedPath = "/api/json/v1/1/search.php?s=egg"
            assertThat(request.path).isEqualTo(expectedPath)
        }
    }

    @Test
    fun searchRecipes_sentRequest_correctRecipeSize() {
        runBlocking {
            enqueueMockResponse("theMealAPIRecipeResponse.json")
            val responseBody = retrofit.searchRecipes("egg").body()

            val theMealRecipesList = responseBody!!.meals
            assertThat(theMealRecipesList!!.size).isEqualTo(8)
        }
    }

    @Test
    fun searchRecipes_sentRequest_correctContent() {
        runBlocking {
            enqueueMockResponse("theMealAPIRecipeResponse.json")
            val responseBody = retrofit.searchRecipes("egg").body()

            val theMealRecipesList = responseBody!!.meals!!
            val theMealRecipe = theMealRecipesList[0]!!

            assertThat(theMealRecipe.strMeal).isEqualTo("Egg Drop Soup")
            assertThat(theMealRecipe.strCategory).isEqualTo("Vegetarian")
            assertThat(theMealRecipe.strMealThumb).isEqualTo("https://www.themealdb.com/images/media/meals/1529446137.jpg")
        }
    }
    @After
    fun tearDown() {
        server.shutdown()
    }


}