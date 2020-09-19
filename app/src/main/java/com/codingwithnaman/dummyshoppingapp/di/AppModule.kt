package com.codingwithnaman.dummyshoppingapp.di

import android.content.Context
import androidx.room.Room
import com.codingwithnaman.dummyshoppingapp.data.local.ShoppingDao
import com.codingwithnaman.dummyshoppingapp.data.local.ShoppingDatabase
import com.codingwithnaman.dummyshoppingapp.data.remote.PixabayAPI
import com.codingwithnaman.dummyshoppingapp.util.Constant.BASE_URL
import com.codingwithnaman.dummyshoppingapp.util.Constant.TABLE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun createShoppingDatabase(
        @ApplicationContext context : Context
    ) : ShoppingDatabase = Room.databaseBuilder(
        context,
        ShoppingDatabase::class.java,
        TABLE_NAME
    ).build()

    @Singleton
    @Provides
    fun getShoppingDao(database: ShoppingDatabase) : ShoppingDao{
        return database.getShoppingDao()
    }

    @Singleton
    @Provides
    fun createRetrofit() : PixabayAPI = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PixabayAPI::class.java)
}