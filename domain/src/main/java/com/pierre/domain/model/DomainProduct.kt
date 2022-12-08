package com.pierre.domain.model

data class DomainProduct(
    val code: String,
    val name: String,
    val brand: String,
    val description: String?,
    val imageUrl: String?,
    val ingredients: String?,
)