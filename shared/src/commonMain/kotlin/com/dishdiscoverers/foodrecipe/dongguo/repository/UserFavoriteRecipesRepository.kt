package com.dishdiscoverers.foodrecipe.dongguo.repository

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.where
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

@Serializable
data class UserFavoriteRecipe(
    var userId: String = "",
    var recipeId: String = "",
)

interface UserFavoriteRecipeRepository {
    suspend fun getFavoritesRecipeByUserId(userId: String): Resource<List<UserFavoriteRecipe>>
    suspend fun getFavoritesRecipeByRecipeId(recipeId: String): Resource<List<UserFavoriteRecipe>>
    suspend fun addFavoriteRecipe(userId: String, recipeId: String): Resource<UserFavoriteRecipe>
    suspend fun deleteFavoriteRecipe(
        userId: String,
        recipeId: String,
    ): Resource<List<UserFavoriteRecipe>>

    suspend fun getFavoritesRecipe(
        userId: String,
        recipeId: String
    ): Resource<Boolean>
}

class UserFavoriteRecipeRepositoryFirebase constructor(private val authRepository: AuthRepository) :
    UserFavoriteRecipeRepository {

    private val COLLECTION_PATH_FAVORIATE = "favorite_recipes"
    suspend fun authCurrent(): Flow<FirebaseUser?> {
        return authRepository.authStateChanged()
    }

    override suspend fun getFavoritesRecipeByUserId(userId: String): Resource<List<UserFavoriteRecipe>> {
        val db = Firebase.firestore
        val collection =
            db.collection(COLLECTION_PATH_FAVORIATE).where(field = "userId", equalTo = userId)
        val querySnapshot = collection.get()
        val list: MutableList<UserFavoriteRecipe> = mutableListOf()
        for (document in querySnapshot.documents) {
            val commentsData = document.data(UserFavoriteRecipe.serializer())
            commentsData?.let {
                list.add(it.copy(recipeId = document.id))
            }
        }
        return Resource.Success(list.toList())
    }

    override suspend fun getFavoritesRecipeByRecipeId(recipeId: String): Resource<List<UserFavoriteRecipe>> {
        val db = Firebase.firestore
        val collection =
            db.collection(COLLECTION_PATH_FAVORIATE).where(field = "recipeId", equalTo = recipeId)
        val querySnapshot = collection.get()
        val list: MutableList<UserFavoriteRecipe> = mutableListOf()
        for (document in querySnapshot.documents) {
            val commentsData = document.data(UserFavoriteRecipe.serializer())
            commentsData?.let {
                list.add(it.copy(recipeId = document.id))
            }
        }
        return Resource.Success(list.toList())
    }

    override suspend fun getFavoritesRecipe(userId: String, recipeId: String): Resource<Boolean> {
        val db = Firebase.firestore
        val collection =
            db.collection(COLLECTION_PATH_FAVORIATE).where(field = "userId", equalTo = userId)
                .where(field = "recipeId", equalTo = recipeId)
        val querySnapshot = collection.get()
        if (querySnapshot != null)
            return Resource.Success(true)
        return Resource.Success(true)
    }

    override suspend fun addFavoriteRecipe(
        userId: String,
        recipeId: String
    ): Resource<UserFavoriteRecipe> {
        val db = Firebase.firestore
        val id = recipeId + userId
        val userFavoriteRecipe = UserFavoriteRecipe(
            userId = userId,
            recipeId = recipeId,
        )
        if (userId != null && recipeId != null) {
            val authResult = authRepository.signUp("dongguo@wu.com", "dongguo")
            db.collection("comments")
                .document(id)
                .set(UserFavoriteRecipe.serializer(), userFavoriteRecipe, encodeDefaults = true)

            return Resource.Success(userFavoriteRecipe);
        }
        return Resource.Failure(Exception("failed to add"))
    }

    override suspend fun deleteFavoriteRecipe(
        userId: String,
        recipeId: String
    ): Resource<List<UserFavoriteRecipe>> {
        if (authRepository.isLoggedIn()) {
            val db = Firebase.firestore
            val id = recipeId + userId
            val docRef = db.collection(COLLECTION_PATH_FAVORIATE).document(id)
            val doc = docRef.get()
            if (doc.exists) {
                val frogData = doc.data(UserFavoriteRecipe.serializer())
                try {
                    docRef.delete()

                } catch (e: Exception) {
                    return Resource.Failure(Exception(e.message))
                }
                // Temp hack to demonstrate that auth also works
                authRepository.delete()
            }
        }

        // return favorite recipe list
        val db = Firebase.firestore
        val collection =
            db.collection(COLLECTION_PATH_FAVORIATE).where(field = "userId", equalTo = userId)
        val querySnapshot = collection.get()
        val list: MutableList<UserFavoriteRecipe> = mutableListOf()
        for (document in querySnapshot.documents) {
            val commentsData = document.data(UserFavoriteRecipe.serializer())
            commentsData?.let {
                list.add(it.copy(recipeId = document.id))
            }
        }
        return Resource.Success(list.toList())

    }
}
