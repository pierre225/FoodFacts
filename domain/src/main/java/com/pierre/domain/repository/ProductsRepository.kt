package com.pierre.domain.repository

import com.pierre.data.remote.RemoteDataSource
import com.pierre.data.remote.models.RemoteProductResponse
import com.pierre.domain.mapper.DomainProductMapper
import com.pierre.domain.model.DomainProduct
import com.pierre.domain.repository.exceptions.ProductException

interface ProductsRepository {

    suspend fun product(code: String): DomainProduct

}

internal class ProductsRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val mapper: DomainProductMapper,
) : ProductsRepository {

    override suspend fun product(code: String): DomainProduct {
        val response = remoteDataSource.product(code)

        return when {
            response == null -> throw ProductException.NullResponseException()
            isSuccess(response) -> mapper.toDomain(response.product!!)
            hasError(response) -> throw ProductException.ErrorResponseException(response.errors?.firstOrNull()?.message?.name ?: "The api returned an error")
            else -> throw ProductException.UnknownResponseException()
        }
    }

    private fun isSuccess(response: RemoteProductResponse) =
        response.status == STATUS_SUCCESS && response.product != null

    private fun hasError(response: RemoteProductResponse) =
        response.errors != null && response.errors!!.isNotEmpty()

    companion object {
        private const val STATUS_SUCCESS = "success"
    }
}
