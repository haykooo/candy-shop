package com.example.candyshop.datamodel.products

data class PopularProductDetailResponseData(
    val success: Boolean,
    val data: PopularProductDetailData,
    val message: String
)