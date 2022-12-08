package com.pierre.domain.repository.exceptions

sealed class ProductException(override val message: String) : Exception(message) {
    data class NullResponseException(override val message: String = "The response was null") : ProductException(message)
    data class ErrorResponseException(override val message: String = "The api returned an error") : ProductException(message)
    data class UnknownResponseException(override val message: String = "Unknown response") : ProductException(message)
}
