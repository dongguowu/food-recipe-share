package com.dishdiscoverers.foodrecipe.dongguo.repository


/**
 * This `Resource` class is a sealed class
 * that provides a wrapper for different types of API or database responses.
 * It has three subclasses:
 * `Success` representing a successful response with the result data,
 * `Failure` representing a failure response with an exception,
 * `Loading` representing a loading state while waiting for the API response.
 * The type parameter `R` represents the type of the result data,
 * allowing flexibility in the types of data that can be wrapped by `Resource`.
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
