package com.dishdiscoverers.foodrecipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dishdiscoverers.foodrecipe.presentation.recipe_list.RecipeViewModel
import com.dishdiscoverers.foodrecipe.presentation.RecipeFeed
import com.dishdiscoverers.foodrecipe.presentation.mockRecipeList
import com.dishdiscoverers.foodrecipe.ui.theme.FoodRecipeShareTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dishdiscoverers.core.common.Constants
import com.dishdiscoverers.core.data.remote.TheMealAPIService
import com.dishdiscoverers.core.data.repository.RecipeRepositoryDateSourceImpl
import com.dishdiscoverers.core.data.repository.dataSource.RemoteRecipeDataSourceImpl
import com.dishdiscoverers.core.usecase.GetSearchedRecipesUseCase
import com.dishdiscoverers.foodrecipe.presentation.RecipesListScreen
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = RecipeViewModel(
                GetSearchedRecipesUseCase(
                    RecipeRepositoryDateSourceImpl(
                        RemoteRecipeDataSourceImpl(
                            Retrofit
                                .Builder()
                                .baseUrl(Constants.THE_MEAL_BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build()
                                .create(TheMealAPIService::class.java)
                        )
                    )
                )
            )
            FoodRecipeShareTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Greeting("Android")
                        RecipesListScreen(viewModel)
//                        RecipeFeed(recipeResources = mockRecipeList, onToggleBookmark = {_, _ ->} )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FoodRecipeShareTheme {
        Greeting("Android")
    }
}