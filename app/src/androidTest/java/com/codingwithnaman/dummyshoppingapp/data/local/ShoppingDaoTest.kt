package com.codingwithnaman.dummyshoppingapp.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.codingwithnaman.dummyshoppingapp.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ShoppingDaoTest {

    private lateinit var database: ShoppingDatabase
    private lateinit var shoppingDao: ShoppingDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ShoppingDatabase::class.java
        ).allowMainThreadQueries().build()

        shoppingDao = database.getShoppingDao()
    }

    @After
    fun teardown(){
        database.close()
    }

    @Test
    fun insertShoppingItem() = runBlockingTest{
        val dbShoppingItem = DBShoppingItem(1,2f, 10,"url", "name")
        shoppingDao.insert(dbShoppingItem)
        val cacheShoppingItem = shoppingDao.observeShoppingDatabase().getOrAwaitValue()

        assertThat(cacheShoppingItem).contains(dbShoppingItem)

    }

    @Test
    fun deleteShoppingItem() = runBlockingTest {
        val dbShoppingItem = DBShoppingItem(1,2f, 10,"url", "name")
        shoppingDao.insert(dbShoppingItem)
        shoppingDao.deleteShoppingItem(dbShoppingItem)

        val cacheShoppingItem = shoppingDao.observeShoppingDatabase().getOrAwaitValue()
        assertThat(cacheShoppingItem).doesNotContain(dbShoppingItem)
    }

    @Test
    fun observeTotalPriceSun() = runBlockingTest {
        val dbShoppingItem = DBShoppingItem(1,2f, 10,"url", "name")
        val dbShoppingItem1 = DBShoppingItem(2,4f, 30,"url", "name")
        val dbShoppingItem2 = DBShoppingItem(3,20f, 20,"url", "name")

        shoppingDao.insert(dbShoppingItem)
        shoppingDao.insert(dbShoppingItem1)
        shoppingDao.insert(dbShoppingItem2)

        val totalPrice = shoppingDao.observeTotalAmount().getOrAwaitValue()
        assertThat(totalPrice).isEqualTo((2f * 10) + (4f * 30) + (20f * 20))

    }

}