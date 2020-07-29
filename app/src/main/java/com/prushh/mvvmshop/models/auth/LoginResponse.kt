package com.prushh.mvvmshop.models.auth

/**
 * Represent login API response.
 * @property message Server response message.
 * @property status Message based on response status code.
 * @property user User information.
 */
data class LoginResponse(
    val message: String,
    val status: String,
    val user: User?
)