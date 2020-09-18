package com.codingwithnaman.dummyshoppingapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DBShoppingItem::class], version = 1)
abstract class ShoppingDatabase : RoomDatabase(){
    abstract fun getShoppingDao() : ShoppingDao
}