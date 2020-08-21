package com.example.candyshop.datamodel.category


data class CategoryData(
    val id: Int,
    val categoryName: String,
    val categoryColoursHexes: CategoryColoursHexes,
    val categoryColoursEnumValue: Int,
    val productCount: Int,
    val imageUrl: String
)