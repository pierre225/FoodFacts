package com.pierre.songs.ui.splash.model

import androidx.annotation.StringRes

sealed class PreloadState {

    object Loading : PreloadState()
    object Success : PreloadState()
    data class Error(@StringRes val message: Int, val retry: Boolean) : PreloadState()

}