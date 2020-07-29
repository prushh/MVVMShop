package com.prushh.mvvmshop.repository

import com.prushh.mvvmshop.api.RetrofitInstance
import com.prushh.mvvmshop.db.ProductDatabase
import com.prushh.mvvmshop.db.ProductDao
import com.prushh.mvvmshop.api.ShopApi
import com.prushh.mvvmshop.models.shop.Product

/**
 * This class contains API calls for show and buy products.
 * There are also database methods for show, save and delete products in local storage.
 * @property db Used for accessing to local database methods.
 */
class ShopRepository(
    private val db: ProductDatabase
) {

    /**
     * Call getAllProducts API in RetrofitInstance.
     * @return Response<ShopResponse>: server response for getAllProducts API call.
     * @see ShopApi.getAllProducts
     */
    suspend fun getAllProducts() = RetrofitInstance.api.getAllProducts()

    /**
     * Call getProductById API in RetrofitInstance.
     * @param productId Unique identifier for the product.
     * @return Response<ShopResponse>: server response for getProductById API call.
     * @see ShopApi.getProductById
     */
    suspend fun getProductById(productId: Int) = RetrofitInstance.api.getProductById(productId)

    /**
     * Call buyProductById API in RetrofitInstance.
     * @param userId Unique identifier for the user.
     * @param productId Unique identifier for the product.
     * @return Response<ShopResponse>: server response for buyProductById API call.
     * @see ShopApi.buyProductById
     */
    suspend fun buyProductById(userId: Int, productId: Int) =
        RetrofitInstance.api.buyProductById(userId, productId)

    /**
     * Call getArchive API in RetrofitInstance.
     * @param userId Unique identifier for the user.
     * @return Response<ShopResponse>: server response for getArchive API call.
     * @see ShopApi.getArchive
     */
    suspend fun getArchive(userId: Int) = RetrofitInstance.api.getArchive(userId)

    /**
     * Call addCoupon API in RetrofitInstance.
     * @param userId Unique identifier for the user.
     * @param coupon Value of giveaway credit.
     * @return Response<ShopResponse>: server response for addCoupon API call.
     * @see ShopApi.addCoupon
     */
    suspend fun addCoupon(userId: Int, coupon: Int) = RetrofitInstance.api.addCoupon(userId, coupon)

    /**
     * Call upsert db function through ProductDao.
     * @param product Product information.
     * @see ProductDao.upsert
     */
    suspend fun upsert(product: Product) = db.getProductDao().upsert(product)

    /**
     * Call getAllProducts db function through ProductDao.
     * @return LiveData<List<Product>>: List of all products saved in local storage.
     * @see ProductDao.getAllProducts
     */
    fun getSavedProducts() = db.getProductDao().getAllProducts()

    /**
     * Call delete db function through ProductDao.
     * @param product Product information.
     * @see ProductDao.delete
     */
    suspend fun delete(product: Product) = db.getProductDao().delete(product)
}