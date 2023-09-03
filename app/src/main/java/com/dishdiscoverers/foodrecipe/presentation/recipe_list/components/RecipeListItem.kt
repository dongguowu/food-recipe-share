package com.dishdiscoverers.foodrecipe.presentation.recipe_list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dishdiscoverers.core.domain.model.FoodRecipe

@Composable
fun RecipeListItem(
    foodRecipe: FoodRecipe, onItemClick: (FoodRecipe) -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onItemClick(foodRecipe) }
        .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = "${foodRecipe.title}. ${foodRecipe.ingredients?.subSequence(0, 100)} (${foodRecipe.instructions.subSequence(0, 150)})",
            style = MaterialTheme.typography.labelLarge
        )
    }
}