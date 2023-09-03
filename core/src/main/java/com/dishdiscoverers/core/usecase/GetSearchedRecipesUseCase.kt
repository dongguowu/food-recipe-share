package com.dishdiscoverers.core.usecase

import com.dishdiscoverers.core.common.Resource
import com.dishdiscoverers.core.domain.model.FoodRecipe
import com.dishdiscoverers.core.domain.repository.RecipeRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GetSearchedRecipesUseCase @Inject constructor(private val recipeRepository: RecipeRepository) {
    operator fun invoke(searchQuery: String): Flow<Resource<List<FoodRecipe>>> = flow {
        try {
            emit(Resource.Loading)
            emit(Resource.Success(recipeRepository.getSearchedRecipes(searchQuery)))
        }catch (ex: HttpException) {
            emit(Resource.Failure(ex))
        }catch (ex: IOException) {
            emit(Resource.Failure(ex))
        }
    }
}