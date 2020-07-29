package com.prushh.mvvmshop.models.shop

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Represent a serializable product.
 * Entity of local Room database, with table name 'products'.
 * @property id Unique identifier and primary key for entity.
 * @property category Category for the product.
 * @property description Description of the product.
 * @property price Price of the product in €.
 * @property quantity Availability of the product.
 * @property title Name of the product.
 * @property urlToImage Url containing the product image.
 * @property saveAt Product saved date into database.
 */
@Entity(tableName = "products")
data class Product(
    @PrimaryKey
    val id: Int,
    val category: String?,
    val description: String?,
    val price: Double?,
    var quantity: Int?,
    val title: String?,
    val urlToImage: String?,
    var saveAt: String?
) : Serializable