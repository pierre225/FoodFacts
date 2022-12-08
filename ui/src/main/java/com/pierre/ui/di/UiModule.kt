package com.pierre.songs.ui.di

import com.pierre.ui.search.mapper.UiProductMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UiModule {

    @Provides
    fun uiMapper() = UiProductMapper()

}
