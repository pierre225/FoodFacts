package com.pierre.data.remote.models

import com.google.gson.annotations.SerializedName

data class RemoteProduct(
    @SerializedName("code") val code : String
) {

    data class Product()
}
