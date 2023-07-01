package com.dishdiscoverers.foodrecipe.dongguo.repository

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.where
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable


/**
 * Repository for managing user recipe comments.
 */
interface UserRecipeCommentRepository {

    /**
     * Retrieves comments for a specific recipe.
     *
     * @param recipeId The ID of the recipe.
     * @return A resource containing the list of comments.
     */
    suspend fun getCommentsByRecipeId(recipeId: String): Resource<List<UserRecipeComment>>

    /**
     * Retrieves a comment by its ID.
     *
     * @param id The ID of the comment.
     * @return A resource containing the comment.
     */
    suspend fun getCommentById(id: String): Resource<UserRecipeComment>

    /**
     * Adds a comment to a recipe.
     *
     * @param userId    The ID of the user.
     * @param recipeId  The ID of the recipe.
     * @param text      The text of the comment.
     * @param imageUrl  The URL of the image attached to the comment (optional).
     * @return A resource indicating the success status of the operation.
     */
    suspend fun addComment(
        userId: String,
        recipeId: String,
        text: String,
        imageUrl: String? = null
    ): Resource<UserRecipeComment>

    //TODO: update, delete
    //suspend fun getCommentsByUserId(userId : String): Resource<List<UserRecipeComment>>


}

/**
 * Firebase implementation of UserRecipeCommentRepository.
 */
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

/**
 * Represents a user's recipe comment.
 */
@Serializable
data class UserRecipeComment(
    var id: String? = null,
    var userId: String = "",
    var recipeId: String = "",
    var text: String = "",
    var imageUrl: String = "",
)