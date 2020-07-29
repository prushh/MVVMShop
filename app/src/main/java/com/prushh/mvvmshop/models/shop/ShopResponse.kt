package com.prushh.mvvmshop.models.shop

/**
 * Represent shop API response.
 * @property message Server response message.
 * @property status Message based on response status code.
 * @property products List of required products.
 */
data class ShopResponse(
    val message: String,
    val status: String,
    val products: MutableList<Product>
)