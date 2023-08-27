package com.dishdiscoverers.core.usecase

import com.dishdiscoverers.core.data.model.FoodRecipe
import com.dishdiscoverers.core.data.utility.Resource
import com.dishdiscoverers.core.repository.RecipeRepository
import io.mockk.coEvery
import io.mockk.mockkClass
import kotlinx.coroutines.runBlocking
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import java.lang.Exception

class GetSearchedRecipesUseCaseTest {

    private lateinit var repository: RecipeRepository
    private lateinit var list: List<FoodRecipe>
    private lateinit var title: String

    @Before
    fun setUp() {
        title = "a recipe title"
        repository = mockkClass(RecipeRepository::class)
        list = listOf(
            FoodRecipe(
                id = "1",
                title = title,
                servings = 2,
                instructions = "Instructions for Recipe 1",
                imageUrl = "https://example.com/image1.jpg",
                ingredients = "Ingredient 1, Ingredient 2"
            ),
            FoodRecipe(
                id = "2",
                title = "Recipe 2",
                servings = 4,
                instructions = "Instructions for Recipe 2",
                imageUrl = "https://example.com/image2.jpg",
                ingredients = "Ingredient A, Ingredient B, Ingredient C"
            ),
        )
    }

    @Test
    fun getSearchedRecipesUseCase_executed_receivedCorrectResult() {
        runBlocking {
            coEvery { repository.getSearchedRecipes(any())} returns Resource.Success(list)

            var useCase = GetSearchedRecipesUseCase(repository)
            var response = useCase.execute("a string")
            assertThat(response).isInstanceOf(Resource.Success::class.java)
            assertThat(response is Resource<List<FoodRecipe>>)

            var successResult = response as Resource.Success
            var results: List<FoodRecipe> = successResult.data
            assertThat(results.size).isEqualTo(2)
            var recipe = results[0]
            assertThat(recipe.title).isEqualTo(title)
        }
    }

    @Test
    fun getSearchedRecipesUseCase_executed_receivedFail() {
        runBlocking {
            coEvery { repository.getSearchedRecipes(any())} returns Resource.Failure(Exception(title))

            var useCase = GetSearchedRecipesUseCase(repository)
            var response = useCase.execute("a string")
            assertThat(response).isInstanceOf(Resource.Failure::class.java)

            var result = response as Resource.Failure
            var exception = result.exception
            assertThat(exception.message).isEqualTo(title)
        }
    }

    @Test
    fun getSearchedRecipesUseCase_executed_receivedLoading() {
        runBlocking {
            coEvery { repository.getSearchedRecipes(any())} returns Resource.Loading

            var useCase = GetSearchedRecipesUseCase(repository)
            var response = useCase.execute("a string")
            assertThat(response).isInstanceOf(Resource.Loading::class.java)
        }
    }
}
