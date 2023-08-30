package com.dishdiscoverers.core.model

import com.dishdiscoverers.core.domain.model.Ingredient
import com.google.gson.annotations.SerializedName
import org.junit.Assert.assertEquals
import org.junit.Test

class IngredientTest {

    @Test
    fun testIngredientPropertiesInitialization() {
        val id = "ingredient_id"
        val name = "Ingredient Name"
        val category = "Category"
        val unit = "Unit"
        val imageUrl = "https://example.com/image.jpg"
        val description = "Ingredient description"

        val ingredient = Ingredient(id, name, category, unit, imageUrl, description)

        assertEquals(id, ingredient.id)
        assertEquals(name, ingredient.name)
        assertEquals(category, ingredient.category)
        assertEquals(unit, ingredient.unit)
        assertEquals(imageUrl, ingredient.imageUrl)
        assertEquals(description, ingredient.description)
    }

    @Test
    fun testIngredientToString() {
        val id = "ingredient_id"
        val name = "Ingredient Name"
        val category = "Category"
        val unit = "Unit"
        val imageUrl = "https://example.com/image.jpg"
        val description = "Ingredient description"

        val ingredient = Ingredient(id, name, category, unit, imageUrl, description)

        val expectedToString = "Ingredient(id=$id, name=$name, category=$category, unit=$unit, imageUrl=$imageUrl, description=$description)"
        assertEquals(expectedToString, ingredient.toString())
    }
}
