package com.prushh.mvvmshop.models.auth

/**
 * Represent a user.
 * @property id Unique identifier.
 * @property username User nickname.
 * @property password Password for login.
 * @property email User email.
 * @property wallet User credits for shopping.
 */
data class User(
    val id: Int,
    val username: String,
    val password: String,
    val email: String,
    val wallet: Float
)