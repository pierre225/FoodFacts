package com.pierre.data.di

import com.pierre.data.remote.RemoteDataSource
import com.pierre.data.remote.RemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object RemoteModule {

    @Provides
    @Singleton
    fun remoteDataSource(): RemoteDataSource = RemoteDataSourceImpl()

}
