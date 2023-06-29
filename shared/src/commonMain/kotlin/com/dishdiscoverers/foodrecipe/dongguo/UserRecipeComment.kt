package com.dishdiscoverers.foodrecipe.dongguo

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.firestore.firestore
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
}

class UserRecipeCommentRepositoryFirebase constructor(private val authRepository: AuthRepository) :
    UserRecipeCommentRepository {

    suspend fun authCurrent(): Flow<FirebaseUser?> {
        return authRepository.authStateChanged()
    }

    override suspend fun getCommentsByRecipeId(recipeId: String): Resource<List<UserRecipeComment>> {
        // Access a Cloud Firestore instance from your Activity
        val db = Firebase.firestore
        val collection = db.collection("comments")
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

    override suspend fun addComment(
        userId: String,
        recipeId: String,
        text: String,
        imageUrl: String?
    ): Resource<UserRecipeComment> {
        val db = Firebase.firestore
        val comment = UserRecipeComment(
            userId = userId,
            recipeId = recipeId,
            text = text,
            imageUrl = imageUrl ?: ""
        )
        if (userId != null && recipeId != null) {
            val authResult = authRepository.signUp("dongguo@wu.com", "dongguo")
            db.collection("comments").document(comment!!.recipeId!!)
                .set(UserRecipeComment.serializer(), comment, encodeDefaults = true)

            return Resource.Success(comment);
        }
        return Resource.Failure(Exception("failed to add"))

    }

}
