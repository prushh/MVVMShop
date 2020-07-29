package com.prushh.mvvmshop.api

import com.prushh.mvvmshop.utils.Constants.API_BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * This class provide an API object for requests to the server.
 */
class RetrofitInstance {

    companion object {
        /**
         * Setup of retrofit instance with logging interceptor.
         * This is by lazy for have only single instance, stored and synchronized.
         */
        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        /**
         * Create the retrofit instance by passing the class that contains the API calls as a parameter
         * This is by lazy for have only single instance, stored and synchronized.
         */
        val api: ShopApi by lazy {
            retrofit.create(ShopApi::class.java)
        }
    }
}