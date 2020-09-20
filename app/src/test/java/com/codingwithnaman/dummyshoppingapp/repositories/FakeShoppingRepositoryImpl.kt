package com.codingwithnaman.dummyshoppingapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codingwithnaman.dummyshoppingapp.data.local.DBShoppingItem
import com.codingwithnaman.dummyshoppingapp.data.remote.responses.ImageResponse
import com.codingwithnaman.dummyshoppingapp.util.Resource

class FakeShoppingRepositoryImpl : ShoppingRepository {

    private val shoppingItems = mutableListOf<DBShoppingItem>()

    private val observableShoppingItem = MutableLiveData<List<DBShoppingItem>>(shoppingItems)
    private val observableTotalPrice = MutableLiveData<Float>()

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value : Boolean) {
        shouldReturnNetworkError = value
    }

    private fun refreshObservable(){
        observableShoppingItem.postValue(shoppingItems)
        observableTotalPrice.postValue(shoppingItems.sumByDouble { it.price.toDouble() }.toFloat())
    }

    override suspend fun insertShoppingItem(shoppingItem: DBShoppingItem) {
        shoppingItems.add(shoppingItem)
        refreshObservable()
    }

    override suspend fun deleteShoppingItem(shoppingItem: DBShoppingItem) {
        shoppingItems.remove(shoppingItem)
    }

    override fun observeShoppingItem(): LiveData<List<DBShoppingItem>> {
        return observableShoppingItem
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return observableTotalPrice
    }

    override suspend fun searchImages(searchQuery: String): Resource<ImageResponse> {
        return if(shouldReturnNetworkError){
            Resource.error("Error", null)
        } else{
            Resource.success(ImageResponse(emptyList(), 0, 0))
        }
    }
}