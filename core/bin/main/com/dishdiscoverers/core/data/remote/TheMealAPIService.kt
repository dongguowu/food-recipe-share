package com.dishdiscoverers.core.data.remote

import com.dishdiscoverers.core.data.remote.dto.TheMealAPIMealsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TheMealAPIService {
    @GET("api/json/v1/1/search.php")
    suspend fun searchRecipes(
        @Query("s")
        searchTitle:String
    ) : Response<TheMealAPIMealsResponse>
}