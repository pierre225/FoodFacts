package com.pierre.ui.search.model

sealed class SearchState {

    object Loading : SearchState()
    object Initial : SearchState()
    object Scanner : SearchState()
    data class Success(val uiProduct: UiProduct) : SearchState()
    data class Error(val e: Exception) : SearchState()

}