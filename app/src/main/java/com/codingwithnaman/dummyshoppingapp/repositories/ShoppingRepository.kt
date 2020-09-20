package com.codingwithnaman.dummyshoppingapp.repositories

import androidx.lifecycle.LiveData
import com.codingwithnaman.dummyshoppingapp.data.local.DBShoppingItem
import com.codingwithnaman.dummyshoppingapp.data.remote.responses.ImageResponse
import com.codingwithnaman.dummyshoppingapp.util.Resource

interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: DBShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: DBShoppingItem)

    fun observeShoppingItem() : LiveData<List<DBShoppingItem>>

    fun observeTotalPrice() : LiveData<Float>

    suspend fun searchImages(searchQuery : String) : Resource<ImageResponse>
}