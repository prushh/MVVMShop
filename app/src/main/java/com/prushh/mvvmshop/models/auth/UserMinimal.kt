package com.prushh.mvvmshop.models.auth

/**
 * Represent payload sent for authentication.
 * @property user Username or email of the user.
 * @property password Password for login.
 */
data class UserMinimal(
    val user: String,
    val password: String
)