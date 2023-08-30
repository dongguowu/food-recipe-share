package com.dishdiscoverers.core.usecase

import com.dishdiscoverers.core.domain.model.FoodRecipe
import com.dishdiscoverers.core.common.Resource
import com.dishdiscoverers.core.domain.repository.RecipeRepository
import io.mockk.coEvery
import io.mockk.mockkClass
import kotlinx.coroutines.runBlocking
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.Flow
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
            coEvery { repository.getSearchedRecipes(any())} returns list

            var useCase = GetSearchedRecipesUseCase(repository)
            var response = useCase.execute("a string")
            assertThat(response).isInstanceOf(Flow::class.java)
            val flowResponse = response as Flow<Resource<List<FoodRecipe>>>

            val results = mutableListOf<FoodRecipe>()
            flowResponse.collect { resource ->
                when (resource) {
                    is Resource.Success -> results.addAll(resource.data)
                    else -> {}
                }
            }

            assertThat(results.size).isEqualTo(2)
            val recipe = results[0]
            assertThat(recipe.title).isEqualTo(title)
        }
    }

    @Test
    fun getSearchedRecipesUseCase_executed_receivedEmptyResult() {
        runBlocking {
            coEvery { repository.getSearchedRecipes(any())} returns emptyList()

            var useCase = GetSearchedRecipesUseCase(repository)
            var response = useCase.execute("a string")
            assertThat(response).isInstanceOf(Flow::class.java)
            val flowResponse = response as Flow<Resource<List<FoodRecipe>>>

            val results = mutableListOf<FoodRecipe>()
            flowResponse.collect { resource ->
                when (resource) {
                    is Resource.Success -> results.addAll(resource.data)
                    else -> {}
                }
            }

            assertThat(results.size).isEqualTo(0)
        }
    }
}
