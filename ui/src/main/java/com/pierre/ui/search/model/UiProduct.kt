package com.pierre.ui.search.model

data class UiProduct(
    val code: String,
    val name: String,
    val brand: String,
    val description: String?,
    val imageUrl: String?,
    val ingredients: String?,
)