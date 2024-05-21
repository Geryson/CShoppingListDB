package com.gery.cshoppinglistdb.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingItemDAO {
    @Query("SELECT * FROM shoppingitem")
    fun getAllItems(): MutableList<ShoppingItem>

    @Insert
    fun insertItem(item: ShoppingItem)

    @Update
    fun updateItem(item: ShoppingItem)

    @Delete
    fun deleteItem(item: ShoppingItem)

    @Query("DELETE FROM shoppingitem")
    fun deleteAllItems()
}