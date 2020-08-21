package com.example.candyshop.datamodel.products

import com.example.candyshop.datamodel.category.CategoryData

data class PopularProductResponseData(
    val success: Boolean,
    val data: MutableList<PopularProductData>,
    val message: String
)