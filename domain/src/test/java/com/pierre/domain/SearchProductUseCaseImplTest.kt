package com.pierre.domain

import com.pierre.domain.repository.ProductsRepository
import com.pierre.domain.usecases.SearchProductUseCaseImpl
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

internal class SearchProductUseCaseImplTest {

    @RelaxedMockK
    private lateinit var repository: ProductsRepository

    private lateinit var useCase: SearchProductUseCaseImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = SearchProductUseCaseImpl(repository)
    }

    @After
    fun afterTests() {
        unmockkAll()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `It request the product from the repository with the right code`() = runTest {
            val code = "abcd"
            useCase.invoke(code)
            coVerify { repository.product(code) }
        }
}