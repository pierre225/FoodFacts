package com.pierre.ui.search.utils

import android.content.Context
import com.pierre.domain.repository.exceptions.ProductException
import com.pierre.ui.R
import java.io.IOException

/**
 * Utils class to help us handle exceptions
 */
object ExceptionUtils {

    /**
     * Returns a string resource from a type of exception so we can show the user what went wrong
     * Using a resource to be localized
     */
    fun messageFromException(e : Exception, context: Context): String = when (e) {
        is IOException -> context.getString(R.string.io_exception_message)
        is ProductException.UnknownResponseException -> context.getString(R.string.unknown_exception_message)
        is ProductException.NullResponseException -> context.getString(R.string.null_product_response_exception_message)
        is ProductException.ErrorResponseException -> e.message
        else -> context.getString(R.string.unknown_exception_message)
    }

    /**
     * Returns true if the user can retry the previous action false otherwise
     */
    fun canRetry(e : Exception) = when (e) {
        is IOException -> true
        else -> false
    }
}