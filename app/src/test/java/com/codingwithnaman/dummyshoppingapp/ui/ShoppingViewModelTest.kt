package com.codingwithnaman.dummyshoppingapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.codingwithnaman.dummyshoppingapp.MainCoroutineRule
import com.codingwithnaman.dummyshoppingapp.getOrAwaitValueTest
import com.codingwithnaman.dummyshoppingapp.repositories.FakeShoppingRepositoryImpl
import com.codingwithnaman.dummyshoppingapp.util.Constant.MAX_LENGTH
import com.codingwithnaman.dummyshoppingapp.util.Constant.MAX_LENGTH_PRICE
import com.codingwithnaman.dummyshoppingapp.util.Status
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShoppingViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    lateinit var viewModel: ShoppingViewModel

    @Before
    fun setUp() {
        viewModel = ShoppingViewModel(FakeShoppingRepositoryImpl())
    }

    @Test
    fun `insert shopping item with empty string, return error`(){
        viewModel.insertShoppingItem("name", "", "3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too long name, return error`(){

        val string = buildString {
            for (i in 1..MAX_LENGTH + 1) {
                append(1)
            }
        }

        viewModel.insertShoppingItem(string, "5", "3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too long price, return error`(){

        val string = buildString {
            for (i in 1..MAX_LENGTH_PRICE + 1) {
                append(1)
            }
        }

        viewModel.insertShoppingItem("name", "5", string)
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too high amount, return error`(){
        viewModel.insertShoppingItem("name", "999999999999999999999999999", "3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with valid input, return success`(){
        viewModel.insertShoppingItem("name", "5", "3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun `check curImageUrl is empty when insert shopping item with valid input`(){
        viewModel.insertShoppingItem("name", "5", "3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        val curImageUrlValue = viewModel.curlImageUrl.getOrAwaitValueTest()
        assertThat(curImageUrlValue).isEmpty()
    }
}