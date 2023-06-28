package com.lduboscq.appkickstarter.main.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.lduboscq.appkickstarter.main.data.RecipeRepositoryJson
import com.lduboscq.appkickstarter.main.router.Route
import com.lduboscq.appkickstarter.main.screenModel.RecipeScreenModel
import com.lduboscq.appkickstarter.mains.model.Recipe
import com.lduboscq.appkickstarter.ui.Image
import com.lduboscq.appkickstarter.ui.MyBottomBar
import com.lduboscq.appkickstarter.ui.MyTopBar

internal class DetailScreen (var recipe: String, val title: String = "Recipe Details"): Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        // Insert repository
//        val screenModel = rememberScreenModel() {
//            RecipeScreenModel(
//                repository = RecipeRepositoryJson()
//            )
//        }


//        val state by screenModel.state.collectAsState()

        val testRecipe: Recipe = Recipe(
            id = "1",
            title = "Ella\'s Vegetable and Meat Egg Rolls",
            servings = 14,
            instructions = "Fry ground beef, drain, set aside for now. Heat wok, add oil, heat until hot, but not smoking, put celery, onions, bean sprouts and waterchestnuts. fry 2 minutes. Add salt, sugar, and soy sauce, cook 1 minute more. Add ground beef and mix well. Mix cornstarch and water well. Add to mixture in wok. set aside and cool. When cool add to egg roll wrappers, wrapping diagonaly then fry in deep fat for 3 to 5 minutes. Serve with a mixture of mustard and ketchup. Did egg rolls in this. Use 7 egg roll wrappers and cut in half and this will make 15 egg rolls. NOTES : Very good.",
            imageUrl = "https://www.alisonspantry.com/uploads/new-products/4078-2.jpg"
        )

        var list: MutableList<Recipe> = mutableListOf()
        list.add(testRecipe)
//        if (state is RecipeScreenModel.State.Result) {
//            list =
//                (state as? RecipeScreenModel.State.Result)?.list?.toMutableList() ?: mutableListOf()
//        }

        // Layout - Scaffold
        Scaffold(
            topBar = { MyTopBar(currentScreen = Route.Detail(recipe, title)) },
            bottomBar = { MyBottomBar(currentScreen = Route.Detail(recipe, title)) },

            content = { paddingValues ->

//                if (state is RecipeScreenModel.State.Result) {
//                    LazyColumn {
//                        val list =
//                            (state as? RecipeScreenModel.State.Result)?.list?.toMutableList()
//                                ?: mutableListOf()
//
//                        if (list.isEmpty()) {
//                            item {
//                                RecipeCard(
//                                    recipe = null,
//                                )
//                            }
//                        } else {
//                            items(list) { recipe ->
//                                RecipeCard(
//                                    recipe = recipe
//                                )
//                            }
//                        }
//                    }
//                }
            }
        )
    }
}