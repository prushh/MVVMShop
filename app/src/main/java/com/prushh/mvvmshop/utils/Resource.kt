package com.prushh.mvvmshop.utils

/**
 * A generic class that contains data and status, used for network request.
 * @property data Server response data.
 * @property message Server response message.
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    /**
     * Used for a successful request.
     * @param data Server response data.
     */
    class Success<T>(data: T) : Resource<T>(data)

    /**
     * Used for an incorrect request.
     * @param message Server response error message.
     * @param data Empty server response data.
     */
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    /**
     * Used for loading resource.
     */
    class Loading<T> : Resource<T>()
}