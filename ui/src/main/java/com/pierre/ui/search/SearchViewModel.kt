package com.pierre.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pierre.domain.usecases.SearchProductUseCase
import com.pierre.ui.search.mapper.UiProductMapper
import com.pierre.ui.search.model.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchProductUseCase: SearchProductUseCase,
    private val mapper: UiProductMapper
) : ViewModel() {

    private val _state = MutableStateFlow<SearchState>(SearchState.Initial)
    val state: StateFlow<SearchState> = _state

    fun search(code: String) {
        viewModelScope.launch {
            _state.emit(SearchState.Loading)
            try { searchProduct(code) }
            catch (e: Exception) { onError(e) }
        }
    }

    fun codeIsValid(code: String) =
        code.length >= 12

    private suspend fun searchProduct(code: String) {
        val product = mapper.toUi(searchProductUseCase.invoke(code))
        _state.emit(SearchState.Success(product))
    }

    private suspend fun onError(e: Exception) {
        _state.emit(SearchState.Error(e))
    }
}