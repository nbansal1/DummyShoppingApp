package com.codingwithnaman.dummyshoppingapp.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingwithnaman.dummyshoppingapp.data.local.DBShoppingItem
import com.codingwithnaman.dummyshoppingapp.data.remote.responses.ImageResponse
import com.codingwithnaman.dummyshoppingapp.repositories.ShoppingRepository
import com.codingwithnaman.dummyshoppingapp.util.Constant
import com.codingwithnaman.dummyshoppingapp.util.Constant.MAX_LENGTH
import com.codingwithnaman.dummyshoppingapp.util.Constant.MAX_LENGTH_PRICE
import com.codingwithnaman.dummyshoppingapp.util.Event
import com.codingwithnaman.dummyshoppingapp.util.Resource
import kotlinx.coroutines.launch

class ShoppingViewModel @ViewModelInject constructor(
    private val repository: ShoppingRepository
)  : ViewModel(){

    val shoppingItem = repository.observeShoppingItem()
    val totalPrice = repository.observeTotalPrice()

    private val _images = MutableLiveData<Event<Resource<ImageResponse>>>()
    val images : LiveData<Event<Resource<ImageResponse>>> = _images

    private val _curImageUrl = MutableLiveData<String>()
    val curlImageUrl : LiveData<String> = _curImageUrl

    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<DBShoppingItem>>>()
    val insertShoppingItemStatus : LiveData<Event<Resource<DBShoppingItem>>> = _insertShoppingItemStatus

    fun setCurImageUrl(url : String){
        _curImageUrl.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: DBShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItemTODB(shoppingItem: DBShoppingItem) = viewModelScope.launch {

    }

    fun insertShoppingItem(name : String, amountString : String, priceString: String) {

        if(name.isEmpty() || amountString.isEmpty() || priceString.isEmpty()) {
            _insertShoppingItemStatus.postValue(Event(Resource.error("The field must not be empty", null)))
            return
        }

        if(name.length > MAX_LENGTH) {
            _insertShoppingItemStatus.postValue(Event(Resource.error("The amount length must be less than $MAX_LENGTH ", null)))
            return
        }

        if(priceString.length > MAX_LENGTH_PRICE) {
            _insertShoppingItemStatus.postValue(Event(Resource.error("The price length must be less than $MAX_LENGTH_PRICE ", null)))
            return
        }

        val amount = try {
            amountString.toInt()
        }catch (e: Exception) {
            _insertShoppingItemStatus.postValue(Event(Resource.error("Please enter valid amount ", null)))
            return
        }

        val shoppingItem = DBShoppingItem( price = priceString.toFloat(), item = amount, imageUrl = curlImageUrl.value?: "",name = name)
        insertShoppingItemTODB(shoppingItem)
        setCurImageUrl("")
        _insertShoppingItemStatus.postValue(Event(Resource.success(shoppingItem)))

    }

    fun searchImage(searchQuery : String) {
        if(searchQuery.isEmpty()) return

        _images.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.searchImages(searchQuery)
            _images.value = Event(response)
        }
    }
























}