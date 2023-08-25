package com.dishdiscoverers.core.data.api

import com.dishdiscoverers.core.data.model.meal.TheMealAPIMealsResponse
import com.dishdiscoverers.core.data.model.meal.TheMealRecipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TheMealAPIService {
    @GET("api/json/v1/1/search.php")
    suspend fun searchRecipe(
        @Query("s")
        searchTitle:String
    ) : Response<TheMealAPIMealsResponse>
}