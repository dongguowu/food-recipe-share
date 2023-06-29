package com.dishdiscoverers.foodrecipe.xiaowei

import Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen

/**
 * Screen class representing the user's profile.
 *
 * @property email The email address associated with the user's profile.
 */
class ProfileScreen(private val email: String) : Screen {
    /**
     * Composable function that displays the content of the profile screen.
     * It shows the user's profile image, email, and a list of favorite recipes.
     */
    @Composable
    override fun Content() {

        val screenModel =
            rememberScreenModel() { ProfileScreenModel(LoginRepositoryRealmLocal()) }
        val state by screenModel.state.collectAsState()

        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                topBar = { TopBar(email) },
                bottomBar = { MyBottomBar() }
            ) {
                Image(
                    url = "https://i.pinimg.com/564x/9d/36/fd/9d36fd94e51bdb73759070905718e669.jpg",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    when (val result = state) {
                        is ProfileScreenModel.State.Init -> Text("")
                        is ProfileScreenModel.State.Loading -> Text("")
                        is ProfileScreenModel.State.Result -> {
                            Text("")
                        }

                        else -> {}
                    }
                    Spacer(modifier = Modifier.height(25.dp))
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .aspectRatio(1f)
                            .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)

                    ) {
                        Image(
                            url = "https://i.pinimg.com/564x/40/9b/94/409b94c14fe4214b5b6397e637c331b9.jpg",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Text(
                        text = "$email",
                        modifier = Modifier.padding(top = 20.dp),
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onBackground
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Your ❤️ Recipe book",
                        modifier = Modifier.padding(bottom = 24.dp),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colors.onBackground
                    )
                    Divider(
                        color = MaterialTheme.colors.onPrimary,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                    // LazyRow to display recipe cards
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        // Other content...

                        val recipes = recipeList.take(4) // Get the first 4 recipes
                        LazyRow(
                            modifier = Modifier.padding(vertical = 8.dp),
                            contentPadding = PaddingValues(end = 16.dp)
                        ) {
                            items(recipes) { recipe ->
                                RecipeCard(recipe)
                            }
                        }

                        if (recipeList.size > 4) {
                            val remainingRecipes = recipeList.drop(4) // Get the remaining recipes
                            if (remainingRecipes.isNotEmpty()) {
                                LazyRow(
                                    modifier = Modifier.padding(top = 8.dp),
                                    contentPadding = PaddingValues(end = 16.dp)
                                ) {
                                    items(remainingRecipes) { recipe ->
                                        RecipeCard(recipe)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
/**
 * Composable function that displays a recipe card.
 * It shows an image of the recipe and its name.
 *
 * @param recipe The recipe object containing the necessary data.
 */
@Composable
private fun RecipeCard(recipe: Recipe) {
    Box(
        modifier = Modifier
            .width(150.dp)
            .padding(8.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colors.surface,
                        MaterialTheme.colors.surface.copy(alpha = 0.8f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
                shape = MaterialTheme.shapes.medium
            )
            .clickable { /* Handle card click */ }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Image(
                url=recipe.url,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = recipe.name,
                style = MaterialTheme.typography.body2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

data class Recipe(val name: String, val url: String)

private val recipeList = listOf(
    Recipe("Recipe 1","https://i.pinimg.com/564x/38/65/82/3865824abd8ab5de5f17428b4c332281.jpg" ),
    Recipe("Recipe 2", "https://i.pinimg.com/564x/20/17/4b/20174b8d660df82dfc18a73e0975a1fa.jpg"),
    Recipe("Recipe 3", "https://i.pinimg.com/564x/db/2e/78/db2e78e912cd5a49dc31a955f0a2e848.jpg"),
    Recipe("Recipe 4", "https://i.pinimg.com/564x/8e/f6/26/8ef626c377672a3f32e1ad6651c4c7af.jpg"),
    Recipe("Recipe 5", "https://i.pinimg.com/564x/97/03/4a/97034a8a61cea314556db2575d419712.jpg"),

)