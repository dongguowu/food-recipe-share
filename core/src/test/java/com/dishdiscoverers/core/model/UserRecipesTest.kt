import com.dishdiscoverers.core.domain.model.FoodRecipe
import com.dishdiscoverers.core.domain.model.UserData
import com.dishdiscoverers.core.domain.model.UserRecipes
import org.junit.Assert.assertEquals
import org.junit.Test

class UserRecipesTest {

    @Test
    fun `UserRecipes constructor with FoodRecipe and UserData`() {
        val foodRecipe = FoodRecipe("1", "Recipe 1", 4, "Instructions", "image_url", "Ingredients")
        val userData = UserData("1", setOf("1", "2", "3"))

        val userRecipe = UserRecipes(foodRecipe, userData)

        assertEquals(foodRecipe.id, userRecipe.id)
        assertEquals(foodRecipe.title, userRecipe.title)
        assertEquals(true, userRecipe.isSaved)
    }

    @Test
    fun `mapToUserRecipes should map FoodRecipe list to UserRecipes list`() {
        val foodRecipes = listOf(
            FoodRecipe("1", "Recipe 1", 4, "Instructions", "image_url", "Ingredients"),
            FoodRecipe("2", "Recipe 2", 2, "Instructions", "image_url", "Ingredients")
        )
        val userData = UserData("1", setOf("1", "4444", "3"))

        val userRecipesList = foodRecipes.map { UserRecipes(it, userData) }

        assertEquals(foodRecipes.size, userRecipesList.size)
        assertEquals(foodRecipes[0].id, userRecipesList[0].id)
        assertEquals(foodRecipes[1].title, userRecipesList[1].title)
        assertEquals(true, userRecipesList[0].isSaved)
        assertEquals(false, userRecipesList[1].isSaved)
    }
}
