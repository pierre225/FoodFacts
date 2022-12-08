package com.pierre.data.remote

import com.pierre.data.remote.service.RemoteService
import com.pierre.data.repository.model.DataSong
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface RemoteDataSource {

    suspend fun remoteSongs(): List<DataSong.RemoteSong>?

}

internal class RemoteDataSourceImpl : RemoteDataSource {

    // Add interceptors in debug if needed
    private val client by lazy { OkHttpClient.Builder().build() }

    private val retrofit by lazy {
        Retrofit
            .Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val service by lazy {
        retrofit.create(RemoteService::class.java)
    }

    override suspend fun remoteSongs() =
        service.songs().body()

    companion object {
        private const val BASE_URL = "https://static.leboncoin.fr"
    }

}