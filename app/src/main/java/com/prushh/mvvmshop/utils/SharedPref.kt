package com.prushh.mvvmshop.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.prushh.mvvmshop.utils.Constants.SHARED_PREF_DEFAULT_INT
import com.prushh.mvvmshop.utils.Constants.SHARED_PREF_DEFAULT_STR
import com.prushh.mvvmshop.utils.Constants.SHARED_PREF_ID
import com.prushh.mvvmshop.utils.Constants.SHARED_PREF_PSW
import com.prushh.mvvmshop.utils.Constants.SHARED_PREF_USER
import com.prushh.mvvmshop.utils.Constants.SHARED_PREF_WALLET

/**
 * This class provides access to items stored in the SharedPreferences object,
 * which are used in some parts of the application.
 * @param context Application context.
 */
class SharedPref(context: Context) {

    /**
     * Try to get sharedPreferences information by KEY.
     */
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(Constants.SHARED_PREF_KEY, Context.MODE_PRIVATE)


    /**
     * Declare getter and setter for id value.
     */
    var id: Int
        get() = sharedPref.getInt(SHARED_PREF_ID, SHARED_PREF_DEFAULT_INT)
        set(value) = sharedPref.edit { putInt(SHARED_PREF_ID, value) }

    /**
     * Declare getter and setter for user value.
     */
    var user: String
        get() = sharedPref.getString(SHARED_PREF_USER, SHARED_PREF_DEFAULT_STR)
            ?: SHARED_PREF_DEFAULT_STR
        set(value) = sharedPref.edit { putString(SHARED_PREF_USER, value) }

    /**
     * Declare getter and setter for password value.
     */
    var password: String
        get() = sharedPref.getString(SHARED_PREF_PSW, SHARED_PREF_DEFAULT_STR)
            ?: SHARED_PREF_DEFAULT_STR
        set(value) = sharedPref.edit { putString(SHARED_PREF_PSW, value) }

    /**
     * Declare getter and setter for wallet value.
     */
    var wallet: Float
        get() = sharedPref.getFloat(SHARED_PREF_WALLET, SHARED_PREF_DEFAULT_INT.toFloat())
        set(value) = sharedPref.edit { putFloat(SHARED_PREF_WALLET, value) }

    /**
     * Remove all stored shared preferences.
     */
    fun clear() {
        with(sharedPref.edit()) {
            clear()
            commit()
        }
    }
}