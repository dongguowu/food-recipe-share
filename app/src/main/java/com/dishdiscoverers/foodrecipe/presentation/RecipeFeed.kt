package com.dishdiscoverers.foodrecipe.presentation

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import com.dishdiscoverers.core.data.SaveableRecipeResource
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.dishdiscoverers.core.data.RecipeResource

@Composable
fun RecipeFeed(
    recipeResources: List<SaveableRecipeResource>,
    onToggleBookmark: (String, Boolean) -> Unit
){
    LazyColumn {
        items(recipeResources) {
            RecipeResourceCard(
                it.recipeResource,
                isBookmarked = it.isBookmarked,
                onToggleBookmark = {
                    onToggleBookmark(it.recipeResource.id, !it.isBookmarked)
                }
            )
        }
    }

}

@Composable
fun RecipeResourceCard(recipeResource: RecipeResource, isBookmarked: Boolean, onToggleBookmark: () -> Unit) {
    var showDetails by rememberSaveable { mutableStateOf(false) }

        ClickableText(text = AnnotatedString(recipeResource.title), onClick = {
            showDetails = !showDetails
        })

        if (showDetails) {
            Text(recipeResource.ingredients)
        }

}

//@Preview(showBackground = true)
//@Composable
//fun RecipeFeedPreview() {
//    RecipeFeed(recipeResources = emptyList()) { _, _ -> }
//}
