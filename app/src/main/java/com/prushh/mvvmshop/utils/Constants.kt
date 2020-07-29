package com.prushh.mvvmshop.utils

/**
 * This object contains all useful constants.
 */
object Constants {

    // Local database file name.
    const val DB_NAME = "product_db.db"

    // URL server API, with version subdirectory.
    const val API_BASE_URL = "http://server:3000/api/v1/"

    // All strings resources for SharedPreferences.
    const val SHARED_PREF_KEY = "Credentials"
    const val SHARED_PREF_DEFAULT_STR = "not_found"
    const val SHARED_PREF_DEFAULT_INT = 0
    const val SHARED_PREF_ID = "id"
    const val SHARED_PREF_USER = "user"
    const val SHARED_PREF_PSW = "password"
    const val SHARED_PREF_WALLET = "wallet"

    // All coupon key for get giveaway.
    const val SHOP_100 = "SHOP100"
    const val SHOP_250 = "SHOP250"
    const val SHOP_500 = "SHOP500"
}