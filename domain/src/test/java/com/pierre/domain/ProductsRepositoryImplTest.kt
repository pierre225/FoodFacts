package com.pierre.domain

import com.pierre.data.remote.RemoteDataSource
import com.pierre.data.remote.models.RemoteProductResponse
import com.pierre.data.remote.models.RemoteProductResponse.*
import com.pierre.domain.mapper.DomainProductMapper
import com.pierre.domain.repository.ProductsRepositoryImpl
import com.pierre.domain.repository.exceptions.ProductException
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

internal class ProductsRepositoryImplTest {

    @RelaxedMockK
    private lateinit var remoteDataSource: RemoteDataSource

    private val mapper = DomainProductMapper()

    private lateinit var repository: ProductsRepositoryImpl

    private val remoteError = RemoteProductError(RemoteProductError.RemoteProductErrorMessage("error"))

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        repository = ProductsRepositoryImpl(
            remoteDataSource = remoteDataSource,
            mapper = mapper
        )
    }

    @After
    fun afterTests() {
        unmockkAll()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `It forwards the call to the remote data source`() = runTest {
        try { repository.product("123") }
        catch (e: Exception) {
            // Silent exception
        }

        coVerify { remoteDataSource.product(any(), any(), any()) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `It throws a NullResponseException when the data source returns null`() = runTest {
        coEvery { remoteDataSource.product(any(), any(), any()) }.returns(null)

        try {
            repository.product("123456")
        } catch (e: Exception) {
            assert(e is ProductException.NullResponseException)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `It maps and returns the Domain in case of success`() = runTest {
        val code = "123"
        val remoteProduct = RemoteProduct(code, "name", "brand", "category", "image", "ingredients")
        val successfulResponse = mockk<RemoteProductResponse>(relaxed = true).apply {
            every { status }.returns("success")
            every { product }.returns(remoteProduct)
        }

        coEvery { remoteDataSource.product(code, any(), any()) }.returns(successfulResponse)

        val domainProduct = repository.product(code)
        coVerifyOrder {
            remoteDataSource.product(code, any(), any())
            mapper.toDomain(remoteProduct)
        }

        assert(domainProduct.code == code)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `It throws an ErrorResponseException when the response product is null and there is an error in the response`() = runTest {
        val errorResponse = mockk<RemoteProductResponse>(relaxed = true).apply {
            every { product }.returns(null)
            every { errors }.returns(listOf(remoteError))
        }
        coEvery { remoteDataSource.product(any(), any(), any()) }.returns(errorResponse)

        try {
            repository.product("3124")
        } catch (e: Exception) {
            assert(e is ProductException.ErrorResponseException)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `It throws an ErrorResponseException when the status is not success and there is an error`() = runTest {
        val errorResponse = mockk<RemoteProductResponse>(relaxed = true).apply {
            every { status }.returns("something")
            every { errors }.returns(listOf(remoteError))
        }
        coEvery { remoteDataSource.product(any(), any(), any()) }.returns(errorResponse)

        try {
            repository.product("3124")
        } catch (e: Exception) {
            assert(e is ProductException.ErrorResponseException)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `It throws an UnknownResponseException when there is no error or product in the response`() = runTest {
        val errorResponse = mockk<RemoteProductResponse>(relaxed = true).apply {
            every { product }.returns(null)
            every { errors }.returns(null)
        }
        coEvery { remoteDataSource.product(any(), any(), any()) }.returns(errorResponse)

        try {
            repository.product("3124")
        } catch (e: Exception) {
            assert(e is ProductException.UnknownResponseException)
        }
    }
}