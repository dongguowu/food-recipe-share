package com.dishdiscoverers.foodrecipe.dongguo


/**
 * Represents the response result ef an API request.
 */
sealed class Resource<out R> {

    /**
     * Represents a successful response with the result data.
     * @property result The result data of the API request.
     */
    data class Success<out R>(val result: R) : Resource<R>()
    data class Failure(val exception: Exception) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}
