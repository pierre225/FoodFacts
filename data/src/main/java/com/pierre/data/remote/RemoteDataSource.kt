package com.pierre.data.remote

import android.accounts.NetworkErrorException
import com.pierre.data.BuildConfig
import com.pierre.data.remote.models.RemoteProductResponse
import com.pierre.data.remote.service.RemoteService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale

interface RemoteDataSource {

    suspend fun product(code: String): RemoteProductResponse?

}

internal class RemoteDataSourceImpl : RemoteDataSource {

    private val client by lazy {
        OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }.build()
    }

    private val retrofit by lazy {
        Retrofit
            .Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val service by lazy {
        Locale.getAvailableLocales()
        retrofit.create(RemoteService::class.java)
    }

    private val countryCode get() = Locale.getDefault().country
    private val languageCode get() = Locale.getDefault().language

    override suspend fun product(code: String) : RemoteProductResponse? =
        service.product(code, countryCode, languageCode).body()

    companion object {
        private const val BASE_URL = "https://world.openfoodfacts.org/api/v3/"
    }
}