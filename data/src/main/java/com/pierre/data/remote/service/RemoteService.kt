package com.pierre.data.remote.service

import com.pierre.data.remote.models.RemoteProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteService {

    @GET("product/{code}")
    suspend fun product(
        @Path("code") code: String,
        @Query("cc") countryCode: String?,
        @Query("lc") languageCode: String?,
    ): Response<RemoteProductResponse>

}