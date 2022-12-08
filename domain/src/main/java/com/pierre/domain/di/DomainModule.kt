package com.pierre.domain.di

import com.pierre.data.repository.SongsRepository
import com.pierre.domain.usecases.GetPagedSongsUseCase
import com.pierre.domain.mapper.SongsDomainMapper
import com.pierre.domain.usecases.GetPagedSongsUseCaseImpl
import com.pierre.domain.usecases.PreloadSongsUseCase
import com.pierre.domain.usecases.PreloadSongsUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    fun domainMapper() = SongsDomainMapper()

    @Provides
    fun getPagedSongsUseCase(
        repository: SongsRepository,
        domainMapper: SongsDomainMapper
    ): GetPagedSongsUseCase = GetPagedSongsUseCaseImpl(repository, domainMapper)

    @Provides
    fun preloadSongUseCase(
        repository: SongsRepository,
    ): PreloadSongsUseCase = PreloadSongsUseCaseImpl(repository)

}
