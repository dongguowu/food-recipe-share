package com.dishdiscoverers.foodrecipe

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.dishdiscoverers.core.domain.model.FoodRecipe
import com.dishdiscoverers.foodrecipe.presentation.recipe_list.components.RecipeListItem
import com.dishdiscoverers.foodrecipe.ui.theme.FoodRecipeShareTheme
import org.junit.Rule
import org.junit.Test

class RecipeListItemTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun myTest() {
        composeTestRule.setContent {
            FoodRecipeShareTheme {
                RecipeListItem(
                    FoodRecipe(
                        id = "abc",
                        title = "title",
                        servings = 1,
                        instructions = "",
                        imageUrl = "",
                        ingredients = ""
                    ),
                    onItemClick = {})
            }

            composeTestRule.onNodeWithText("title")
        }

    }
}