package com.prushh.mvvmshop.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.prushh.mvvmshop.models.shop.Product
import com.prushh.mvvmshop.utils.Constants.DB_NAME

/**
 * This is an abstract class that represent Room Database.
 */
@Database(
    entities = [Product::class],
    version = 1
)
abstract class ProductDatabase : RoomDatabase() {

    /**
     * Provide to use functions of DAO interface.
     * @return ProductDao
     */
    abstract fun getProductDao(): ProductDao

    companion object {

        /**
         * Database instance, for visibility purposes we declare @Volatile.
         */
        @Volatile
        private var instance: ProductDatabase? = null

        /**
         * Used to synchronize, it makes the instance atomic.
         */
        private val LOCK = Any()

        /**
         * Invoked every time after the ProductDatabase constructor.
         * @return ProductDatabase: new or already created database instance.
         */
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        /**
         * Create and build local database.
         * @param context Context of application.
         */
        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ProductDatabase::class.java,
                DB_NAME
            ).build()
    }
}