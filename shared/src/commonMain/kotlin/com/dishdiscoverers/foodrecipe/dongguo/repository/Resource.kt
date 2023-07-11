package com.dishdiscoverers.foodrecipe.dongguo.repository


/**
 * wrap our different types of API responses, or database response.
 * @param R The type of the result data.
 */
sealed class Resource<out R> {

    /**
     * Represents a successful response with the result data.
     * @property result The result data of the request.
     */
    data class Success<out R>(val result: R) : Resource<R>()

    /**
     * Represents a failure response with an exception.
     * @property exception The exception associated with the failure.
     */
    data class Failure(val exception: Exception) : Resource<Nothing>()

    /**
     * Represents a loading state while waiting for the API response.
     */
    object Loading : Resource<Nothing>()
}
