package com.prushh.mvvmshop.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.prushh.mvvmshop.models.shop.Product

/**
 * This is an interface that contains methods used to access the database.
 */
@Dao
interface ProductDao {

    /**
     * Insert or update a product in the database.
     * @param product Product information.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(product: Product)

    /**
     * Get all products in the database.
     * @return LiveData<List<Product>>: Observable list of products.
     */
    @Query("SELECT * FROM products")
    fun getAllProducts(): LiveData<List<Product>>

    /**
     * Delete a product in the database.
     * @param product Product information.
     */
    @Delete
    suspend fun delete(product: Product)
}