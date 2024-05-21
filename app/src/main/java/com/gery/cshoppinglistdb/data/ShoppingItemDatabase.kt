package com.gery.cshoppinglistdb.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ShoppingItem::class], version = 1, exportSchema = false)
abstract class ShoppingItemDatabase : RoomDatabase() {
    abstract fun shoppingItemDao(): ShoppingItemDAO

    companion object {
        private var INSTANCE: ShoppingItemDatabase? = null

        fun getInstance(context: Context): ShoppingItemDatabase {
            if (INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ShoppingItemDatabase::class.java, "shopping.db").build()
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}