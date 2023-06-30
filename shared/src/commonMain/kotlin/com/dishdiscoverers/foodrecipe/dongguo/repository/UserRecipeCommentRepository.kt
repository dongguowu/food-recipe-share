package com.dishdiscoverers.foodrecipe.dongguo.repository

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.where
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

@Serializable
data class UserRecipeComment(
    var id: String? = null,
    var userId: String = "",
    var recipeId: String = "",
    var text: String = "",
    var imageUrl: String = "",
)

interface UserRecipeCommentRepository {
    suspend fun getCommentsByRecipeId(recipeId: String): Resource<List<UserRecipeComment>>
    suspend fun addComment(
        userId: String,
        recipeId: String,
        text: String,
        imageUrl: String? = null
    ): Resource<UserRecipeComment>
    //TODO: update, delete
    //suspend fun getCommentsByUserId(userId : String): Resource<List<UserRecipeComment>>
    suspend fun getCommentById(id: String): Resource<UserRecipeComment>
}

class UserRecipeCommentRepositoryFirebase constructor(private val authRepository: AuthRepository) :
    UserRecipeCommentRepository {

    private val COLLECTION_PATH_COMMENTS = "comments"
    suspend fun authCurrent(): Flow<FirebaseUser?> {
        return authRepository.authStateChanged()
    }

    override suspend fun getCommentsByRecipeId(recipeId: String): Resource<List<UserRecipeComment>> {
        val db = Firebase.firestore
        val collection = db.collection(COLLECTION_PATH_COMMENTS).where("recipeId", recipeId)
        val querySnapshot = collection.get()
        val list: MutableList<UserRecipeComment> = mutableListOf()
        for (document in querySnapshot.documents) {
            val commentsData = document.data(UserRecipeComment.serializer())
            commentsData?.let {
                list.add(it.copy(id = document.id))
            }
        }
        return Resource.Success(list.toList())
    }

    override suspend fun getCommentById(id: String): Resource<UserRecipeComment> {
        val db = Firebase.firestore

        // Temp hack to demonstrate that auth also works
        authRepository.signOut()

        val docRef = db.collection(COLLECTION_PATH_COMMENTS).document(id)
        val doc = docRef.get()
        if (doc.exists) {
            val result = doc.data(UserRecipeComment.serializer())
            return Resource.Success(result)
        }
        return Resource.Failure(Exception("No related comment found."))
    }
    override suspend fun addComment(
        userId: String,
        recipeId: String,
        text: String,
        imageUrl: String?
    ): Resource<UserRecipeComment> {
        val db = Firebase.firestore
        val id = recipeId + userId.substring(0, 8) + text
        val comment = UserRecipeComment(
            id = id,
            userId = userId,
            recipeId = recipeId,
            text = text,
            imageUrl = imageUrl ?: ""
        )
        if (userId != null && recipeId != null) {
            val authResult = authRepository.signUp("dongguo@wu.com", "dongguo")
            db.collection("comments")
                .document(id)
                .set(UserRecipeComment.serializer(), comment, encodeDefaults = true)

            return Resource.Success(comment);
        }
        return Resource.Failure(Exception("failed to add"))

    }

}
