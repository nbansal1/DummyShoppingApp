package com.codingwithnaman.dummyshoppingapp.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping")
data class DBShoppingItem (
    @PrimaryKey (autoGenerate = true)
    val id : Int? = null,

    @ColumnInfo(name = "price")
    var price: Float,

    @ColumnInfo(name = "item")
    var item: Int,

    @ColumnInfo(name = "imageUrl")
    var imageUrl : String,

    @ColumnInfo(name = "name")
    var name : String
)