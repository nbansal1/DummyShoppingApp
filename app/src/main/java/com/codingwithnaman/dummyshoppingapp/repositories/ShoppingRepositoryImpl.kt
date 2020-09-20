package com.codingwithnaman.dummyshoppingapp.repositories

import androidx.lifecycle.LiveData
import com.codingwithnaman.dummyshoppingapp.data.local.DBShoppingItem
import com.codingwithnaman.dummyshoppingapp.data.local.ShoppingDao
import com.codingwithnaman.dummyshoppingapp.data.remote.PixabayAPI
import com.codingwithnaman.dummyshoppingapp.data.remote.responses.ImageResponse
import com.codingwithnaman.dummyshoppingapp.util.Resource
import java.lang.Exception
import javax.inject.Inject

class ShoppingRepositoryImpl @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val pixabayAPI: PixabayAPI
) : ShoppingRepository {

    override suspend fun insertShoppingItem(shoppingItem: DBShoppingItem) {
        shoppingDao.insert(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: DBShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override fun observeShoppingItem(): LiveData<List<DBShoppingItem>> {
        return shoppingDao.observeShoppingDatabase()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return shoppingDao.observeTotalAmount()
    }

    override suspend fun searchImages(searchQuery: String): Resource<ImageResponse> {
        return try {
            val response = pixabayAPI.searchImages(searchQuery)
            if(response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: return Resource.error("Error", null)
            } else{
                return Resource.error("Error", null)
            }
        }catch (e : Exception) {
            return Resource.error("Unable to fetched the data. Please check the internet connectivity", null)
        }
    }
}