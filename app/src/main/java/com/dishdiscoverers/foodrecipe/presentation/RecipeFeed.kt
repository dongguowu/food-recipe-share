package com.dishdiscoverers.foodrecipe.presentation

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.dishdiscoverers.core.data.model.FoodRecipe
import com.dishdiscoverers.core.data.model.SavableFoodRecipe

@Composable
fun RecipeFeed(
    recipeResources: List<SavableFoodRecipe>,
    onToggleBookmark: (String, Boolean) -> Unit
) {
    LazyColumn {
        items(recipeResources) {
            RecipeResourceCard(
                it.foodRecipe,
                isBookmarked = it.isBookmarked,
                onToggleBookmark = {
                    onToggleBookmark(it.foodRecipe.id, !it.isBookmarked)
                }
            )
        }
    }

}

@Composable
fun RecipeResourceCard(
    recipeResource: FoodRecipe,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit
) {
    var showDetails by rememberSaveable { mutableStateOf(false) }

    ClickableText(text = AnnotatedString(recipeResource.title), onClick = {
        showDetails = !showDetails
    })

    if (showDetails) {
        recipeResource.ingredients?.let { Text(it) }
    }

}

@Preview(showBackground = true)
@Composable
fun RecipeFeedPreview() {
    RecipeFeed(recipeResources = mockRecipeList) { _, _ -> }
}

val mockRecipeList = listOf(
    SavableFoodRecipe(
        FoodRecipe(
            id = "1",
            title = "Chocolate Cake",
            servings = 8,
            instructions = "1. Preheat the oven...",
            imageUrl = "https://example.com/chocolate-cake.jpg",
            ingredients = "2 cups flour, 1 cup sugar, 1/2 cup cocoa"
        ),
        isBookmarked = true
    ),
    SavableFoodRecipe(
        FoodRecipe(
            id = "2",
            title = "Pasta Carbonara",
            servings = 4,
            instructions = "1. Boil the pasta...",
            imageUrl = "https://example.com/pasta-carbonara.jpg",
            ingredients = "8 oz spaghetti, 2 eggs, 1/2 cup grated cheese"
        ),
        isBookmarked = false
    ),
)