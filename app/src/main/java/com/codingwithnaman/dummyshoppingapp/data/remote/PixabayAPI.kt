package com.codingwithnaman.dummyshoppingapp.data.remote

import com.codingwithnaman.dummyshoppingapp.BuildConfig
import com.codingwithnaman.dummyshoppingapp.data.remote.responses.ImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayAPI {

    @GET("/api/")
    suspend fun searchImages(
        @Query("q") searchQuery : String,
        @Query("key") key : String = BuildConfig.API_KEY
    ) : Response<ImageResponse>
}