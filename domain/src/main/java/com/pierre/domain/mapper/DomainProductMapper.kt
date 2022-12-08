package com.pierre.domain.mapper

import com.pierre.data.remote.models.RemoteProductResponse
import com.pierre.domain.model.DomainProduct

class DomainProductMapper {

    fun toDomain(dataProduct: RemoteProductResponse.RemoteProduct) = DomainProduct(
        code = dataProduct.code,
        name = dataProduct.name,
        brand = dataProduct.brands,
        description = dataProduct.categories,
        imageUrl = dataProduct.imageUrl,
        ingredients = dataProduct.ingredients,
    )
}