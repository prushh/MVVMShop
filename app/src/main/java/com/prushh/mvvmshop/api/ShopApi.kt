package com.prushh.mvvmshop.api

import com.prushh.mvvmshop.models.auth.LoginResponse
import com.prushh.mvvmshop.models.auth.User
import com.prushh.mvvmshop.models.auth.UserMinimal
import com.prushh.mvvmshop.models.shop.ShopResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * This is an interface that contains methods used to call server API.
 */
interface ShopApi {

    /**
     * Call getAllProducts API with GET request.
     * @return Response<ShopResponse>: server response for getAllProducts API call.
     */
    @GET("products")
    suspend fun getAllProducts(): Response<ShopResponse>

    /**
     * Call getProductById API with GET request.
     * @param productId Unique identifier for product.
     * @return Response<ShopResponse>: server response for getProductById API call.
     */
    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id")
        productId: Int
    ): Response<ShopResponse>

    /**
     * Call buyProductById API with PUT request.
     * @param userId Unique identifier for user.
     * @param productId Unique identifier for product.
     * @return Response<ShopResponse>: server response for buyProductById API call.
     */
    @PUT("products/buy/{userId}&{productId}")
    suspend fun buyProductById(
        @Path("userId")
        userId: Int,
        @Path("productId")
        productId: Int
    ): Response<ShopResponse>

    /**
     * Call getArchive API with GET request.
     * @param userId Unique identifier for user.
     * @return Response<ShopResponse>: server response for getArchive API call.
     */
    @GET("products/archive/{id}")
    suspend fun getArchive(
        @Path("id")
        userId: Int
    ): Response<ShopResponse>

    /**
     * Call addCoupon API with PUT request.
     * @param userId Unique identifier for user.
     * @param coupon Value of giveaway credit.
     * @return Response<LoginResponse>: server response for addCoupon API call.
     */
    @PUT("coupon/{userId}&{coupon}")
    suspend fun addCoupon(
        @Path("userId")
        userId: Int,
        @Path("coupon")
        coupon: Int
    ): Response<LoginResponse>

    /**
     * Call signIn API with POST request and JSON parameter.
     * @param user Represent user with all information.
     * @return Response<LoginResponse>: server response for signIn API call.
     */
    @Headers("Content-Type: application/json")
    @POST("signin")
    suspend fun signIn(
        @Body
        user: User
    ): Response<LoginResponse>

    /**
     * Call login API with POST request and JSON parameter.
     * @param payload Represent payload sent for authentication.
     * @return Response<LoginResponse>: server response for login API call.
     */
    @Headers("Content-Type: application/json")
    @POST("login")
    suspend fun login(
        @Body
        payload: UserMinimal
    ): Response<LoginResponse>

    /**
     * Call checkAuth API with POST request and JSON parameter.
     * @param payload Represent payload sent for authentication.
     * @return Response<LoginResponse>: server response for checkAuth API call.
     */
    @Headers("Content-Type: application/json")
    @POST("check")
    suspend fun checkAuth(
        @Body
        payload: UserMinimal
    ): Response<LoginResponse>

}