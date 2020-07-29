package com.prushh.mvvmshop.repository

import com.prushh.mvvmshop.api.RetrofitInstance
import com.prushh.mvvmshop.api.ShopApi
import com.prushh.mvvmshop.models.auth.User
import com.prushh.mvvmshop.models.auth.UserMinimal

/**
 * This class contains API calls for authentication purpose.
 */
class LoginRepository {

    /**
     * Call signIn API in RetrofitInstance.
     * @param user User for sign in process.
     * @return Response<LoginResponse>: server response for signIn API call.
     * @see ShopApi.signIn
     */
    suspend fun signIn(user: User) = RetrofitInstance.api.signIn(user)

    /**
     * Call login API in RetrofitInstance.
     * @param payload Payload for authentication process.
     * @return Response<LoginResponse>: server response for login API call.
     * @see ShopApi.login
     */
    suspend fun login(payload: UserMinimal) = RetrofitInstance.api.login(payload)

    /**
     * Call checkAuth API in RetrofitInstance.
     * @param payload Payload for authentication check process.
     * @return Response<LoginResponse>: server response for checkAuth API call.
     * @see ShopApi.checkAuth
     */
    suspend fun checkAuth(payload: UserMinimal) = RetrofitInstance.api.checkAuth(payload)
}