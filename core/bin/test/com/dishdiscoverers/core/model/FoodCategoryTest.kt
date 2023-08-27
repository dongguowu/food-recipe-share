package com.dishdiscoverers.core.model

import com.dishdiscoverers.core.data.model.FoodCategory
import com.google.gson.annotations.SerializedName
import org.junit.Assert.assertEquals
import org.junit.Test

class FoodCategoryTest {

    @Test
    fun testFoodCategoryPropertiesInitialization() {
        val id = "category_id"
        val name = "Category Name"
        val imageUrl = "https://example.com/image.jpg"
        val description = "Category description"

        val foodCategory = FoodCategory(id, name, imageUrl, description)

        assertEquals(id, foodCategory.id)
        assertEquals(name, foodCategory.name)
        assertEquals(imageUrl, foodCategory.imageUrl)
        assertEquals(description, foodCategory.description)
    }

    @Test
    fun testFoodCategoryToString() {
        val id = "category_id"
        val name = "Category Name"
        val imageUrl = "https://example.com/image.jpg"
        val description = "Category description"

        val foodCategory = FoodCategory(id, name, imageUrl, description)

        val expectedToString = "FoodCategory(id=$id, name=$name, imageUrl=$imageUrl, description=$description)"
        assertEquals(expectedToString, foodCategory.toString())
    }
}
