package com.example.candyshop.datamodel.category

data class CategoryResponseData(
    val success: Boolean,
    val data: MutableList<CategoryData>,
    val message: String
)