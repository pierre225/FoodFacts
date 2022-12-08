package com.pierre.domain.usecases

import com.pierre.domain.repository.ProductsRepository
import com.pierre.domain.mapper.DomainProductMapper
import com.pierre.domain.model.DomainProduct

interface SearchProductUseCase : suspend (String) -> DomainProduct

internal class SearchProductUseCaseImpl(
    private val productsRepository: ProductsRepository,
) : SearchProductUseCase {

    override suspend fun invoke(code: String): DomainProduct =
        productsRepository.product(code)

}