package com.pierre.ui.search.mapper

import com.pierre.domain.model.DomainProduct
import com.pierre.ui.search.model.UiProduct

class UiProductMapper {

    fun toUi(domainProduct: DomainProduct) = UiProduct(
        code = domainProduct.code,
        name = domainProduct.name,
        brand = domainProduct.brand,
        description = domainProduct.description,
        imageUrl = domainProduct.imageUrl,
        ingredients = domainProduct.ingredients)
}