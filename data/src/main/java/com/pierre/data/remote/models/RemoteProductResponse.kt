package com.pierre.data.remote.models

import com.google.gson.annotations.SerializedName

data class RemoteProductResponse(
    @SerializedName("status") val status : String,
    @SerializedName("product") val product : RemoteProduct?,
    @SerializedName("errors") val errors : List<RemoteProductError>?,
) {

    data class RemoteProduct(
        @SerializedName("code") val code : String,
        @SerializedName("product_name") val name : String,
        @SerializedName("brands") val brands : String,
        @SerializedName("categories") val categories : String,
        @SerializedName("image_small_url") val imageUrl : String,
        @SerializedName("ingredients_text_with_allergens") val ingredients : String,
    )

    data class RemoteProductError(@SerializedName("message") val message : RemoteProductErrorMessage) {
        data class RemoteProductErrorMessage(@SerializedName("lc_name") val name : String)
    }

}
