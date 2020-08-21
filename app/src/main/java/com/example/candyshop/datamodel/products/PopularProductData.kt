package com.example.candyshop.datamodel.products

data class PopularProductData(
    val id: Int,
    val productName: String,
    val description: String,
    val price: Float,
    val measurementEnumValue: Int,
    val imageUrl: String?,
    var type: String = "product"
){
    constructor(type: String) : this(0, "", "", 0f, 0, null) {
        this.type = type;
    }
}