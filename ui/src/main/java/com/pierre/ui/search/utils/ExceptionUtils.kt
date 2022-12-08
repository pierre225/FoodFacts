package com.pierre.songs.ui.utils

import com.pierre.ui.R
import java.io.IOException

/**
 * Utils class to help us handle exceptions
 * This one is very simple because we only consider the IOException but ideally we should consider a lot more
 */
object ExceptionUtils {

    /**
     * Returns a string resource from a type of exception so we can show the user what went wrong
     */
    fun messageFromException(e : Exception) = when (e) {
        is IOException -> R.string.io_exception_message
        else -> R.string.internal_exception_message
    }

    /**
     * Returns true if the user can retry the previous action false otherwise
     */
    fun canRetry(e : Exception) = when (e) {
        is IOException -> true
        else -> false
    }
}