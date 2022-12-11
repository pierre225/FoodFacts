package com.pierre.ui

import com.pierre.domain.model.DomainProduct
import com.pierre.domain.repository.exceptions.ProductException
import com.pierre.domain.usecases.SearchProductUseCase
import com.pierre.ui.search.SearchViewModel
import com.pierre.ui.search.mapper.UiProductMapper
import com.pierre.ui.search.model.SearchState
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

internal class SearchViewModelTest {

    @RelaxedMockK
    private lateinit var searchProductUseCase: SearchProductUseCase

    private val mapper = UiProductMapper()

    private lateinit var searchViewModel: SearchViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())

        searchViewModel = SearchViewModel(
            searchProductUseCase = searchProductUseCase,
            mapper = mapper
        )
    }

    @After
    fun afterTests() {
        unmockkAll()
    }

    @Test
    fun `verify the first state is a Initial`() {
        assert(searchViewModel.state.value == SearchState.Initial)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `verify search bar code forwards the call to the use case`() = runTest {
        val code = "123456"
        searchViewModel.search(code)

        coVerify { searchProductUseCase.invoke(code) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `It returns the right uiProduct when success`() = runTest {
        val code = "123456"
        val domainProduct = DomainProduct(code, "name", "brand", "description", "image", "ingredients")
        val uiProduct = mapper.toUi(domainProduct)
        coEvery { searchProductUseCase.invoke(code) }.returns(domainProduct)

        searchViewModel.search(code)

        coVerifyOrder {
            searchProductUseCase.invoke(code)
            mapper.toUi(domainProduct)
            assert(searchViewModel.state.value == SearchState.Success(uiProduct))
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `It comes back to the initial state when there is an error`() = runTest {
        val code = "123456"
        val errorMessage = "error message"
        coEvery { searchProductUseCase.invoke(code) }.throws(ProductException.UnknownResponseException(errorMessage))

        searchViewModel.search(code)

        coVerifyOrder {
            searchProductUseCase.invoke(code)
            assert(searchViewModel.state.value == SearchState.Initial)
        }
    }

}