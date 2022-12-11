package com.pierre.data.di

import com.pierre.data.BuildConfig
import com.pierre.data.remote.RemoteDataSource
import com.pierre.data.remote.RemoteDataSourceImpl
import com.pierre.data.remote.service.RemoteService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object RemoteModule {

    @Provides
    @Singleton
    fun remoteDataSource(service: RemoteService): RemoteDataSource = RemoteDataSourceImpl(service)

    @Provides
    @Singleton
    fun okhttpClient() : OkHttpClient =
        OkHttpClient.Builder()
            .apply { if (BuildConfig.DEBUG) addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)) }
            .build()

    @Provides
    @Singleton
    fun retrofit(client: OkHttpClient) : Retrofit =
        Retrofit
            .Builder()
            .client(client)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun remoteService(retrofit: Retrofit): RemoteService =
        retrofit.create(RemoteService::class.java)

}
