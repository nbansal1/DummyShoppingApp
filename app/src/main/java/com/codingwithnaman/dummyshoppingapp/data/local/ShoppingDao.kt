package com.codingwithnaman.dummyshoppingapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShoppingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dbShoppingItem: DBShoppingItem) : Long

    @Delete
    suspend fun deleteShoppingItem(dbShoppingItem: DBShoppingItem)

    @Query("SELECT * FROM shopping")
    fun observeShoppingDatabase(): LiveData<List<DBShoppingItem>>

    @Query("SELECT SUM(price * item) FROM shopping")
    fun observeTotalAmount(): LiveData<Float>
}