package com.pierre.ui.search.utils

import android.content.Context
import com.pierre.domain.repository.exceptions.ProductException
import com.pierre.ui.R
import java.io.IOException

/**
 * Returns a string resource from a type of exception so we can show the user what went wrong
 * Using a resource to be localized
 */
fun Exception.messageFromException(context: Context): String = when (this) {
    is IOException -> context.getString(R.string.io_exception_message)
    is ProductException.UnknownResponseException -> context.getString(R.string.unknown_exception_message)
    is ProductException.NullResponseException -> context.getString(R.string.null_product_response_exception_message)
    is ProductException.ErrorResponseException -> this.message
    else -> context.getString(R.string.unknown_exception_message)
}

/**
 * Returns true if the user can retry the previous action false otherwise
 */
fun Exception.canRetry() = when (this) {
    is IOException -> true
    else -> false
}

