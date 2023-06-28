package com.lduboscq.appkickstarter.main.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
        val screenModel = rememberScreenModel() {
            RecipeScreenModel(
                repository = RecipeRepositoryJson()
            )
        }

        // Layout - Scaffold
        androidx.compose.material.Scaffold(
            topBar = { MyTopBar(currentScreen = Route.Detail(recipe, title)) },
            bottomBar = { MyBottomBar(currentScreen = Route.Detail(recipe, title))},

            content = { paddingValues ->
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    Box(
                        Modifier.padding(horizontal = 14.dp).fillMaxSize()
                    ) {

                        LazyColumn(
                            modifier = Modifier.padding(0.dp)
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                        ) {
                            item {
                                Text(recipe)
//                                recipe?.let {
//                                    Image(
//                                        url = it.imageUrl,
//                                        modifier = Modifier.fillMaxSize()
//                                    )
//                                }
                            }
//                            var array = recipe?.instructions?.split("\n") ?: emptyList()
//                            for (text in array) {
//                                item {
//                                    Text(
//                                        text = text,
//                                        fontWeight = FontWeight.Normal,
//                                        fontSize = 24.sp,
//                                        lineHeight = 42.sp,
//                                        textAlign = TextAlign.Start,
//                                        color = MaterialTheme.colorScheme.onSecondaryContainer
//                                    )
//                                }
//                            }
//                            for (i in 1..5) {
//                                item {
//                                    Text("")
//                                }
//                            }
                        }
                    }
                }
            }
        )
        }
    }