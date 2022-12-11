package com.pierre.domain.di

import com.pierre.data.remote.RemoteDataSource
import com.pierre.domain.repository.ProductsRepository
import com.pierre.domain.mapper.DomainProductMapper
import com.pierre.domain.repository.ProductsRepositoryImpl
import com.pierre.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    fun domainMapper() = DomainProductMapper()

    @Provides
    fun searchProductUseCase(
        repository: ProductsRepository,
    ): SearchProductUseCase = SearchProductUseCaseImpl(repository)

    @Provides
    @Singleton
    fun productsRepository(
        remoteDataSource: RemoteDataSource,
        domainMapper: DomainProductMapper,
    ): ProductsRepository = ProductsRepositoryImpl(remoteDataSource, domainMapper)
}
