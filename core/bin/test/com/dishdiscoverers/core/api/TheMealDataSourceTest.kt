package com.dishdiscoverers.core.api

import com.dishdiscoverers.core.data.api.TheMealAPIService
import com.dishdiscoverers.core.data.model.FoodRecipe
import com.dishdiscoverers.core.data.repository.dataSource.RecipeDataSource
import com.dishdiscoverers.core.data.repository.dataSourceImpl.TheMealRemoteRecipeDataSourceImpl
import com.dishdiscoverers.core.data.utility.Resource
import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
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

class TheMealDataSourceTest {
    private lateinit var service: TheMealAPIService
    private lateinit var server: MockWebServer
    private lateinit var dataSource: RecipeDataSource


    @Before
    fun setUp() {
        server = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(server.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TheMealAPIService::class.java)
        enqueueMockResponse("theMealAPIRecipeResponse.json")
        dataSource = TheMealRemoteRecipeDataSourceImpl(service)
    }

    private fun enqueueMockResponse(
        fileName: String
    ) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        mockResponse.setBody(source.readString(Charsets.UTF_8))
        server.enqueue(mockResponse)
    }

    @Test
    fun searchRecipes_sentRequest_receivedExpected() {
        runBlocking {
            var result = dataSource.searchRecipes("egg")

            assertThat(result).isNotNull()
            assertThat(result).isInstanceOf(Resource.Success::class.java)

            // Extract the value from Resource.Success
            val successResult = result as Resource.Success
            val foodRecipes: List<FoodRecipe> = successResult.data // Assuming data is a List<FoodRecipe>

            // Perform assertions on the content of foodRecipes
            assertThat(foodRecipes).isNotEmpty() // For example, check if the list is not empty

            val recipe = foodRecipes[0]
            assertThat(recipe.id).isEqualTo("52955")
            assertThat(recipe.title).isEqualTo("Egg Drop Soup")
            assertThat(recipe.servings).isEqualTo(1)
            assertWithMessage(recipe.ingredients).that(recipe.ingredients).contains("3 cups  Chicken Stock, 1/4 tsp Salt, 1/4 tsp Sugar, pinch Pepper, 1 tsp  Sesame Seed Oil, 1/3 cup Peas, 1/3 cup Mushrooms, 1 tbs Cornstarch, 2 tbs Water, 1/4 cup Spring Onions,")
            assertThat(recipe.instructions).contains("In a wok add chicken broth and wait for it to boil")
            assertThat(recipe.imageUrl).isEqualTo("https://www.themealdb.com/images/media/meals/1529446137.jpg")
        }
    }



    @After
    fun tearDown() {
        server.shutdown()
    }

}
