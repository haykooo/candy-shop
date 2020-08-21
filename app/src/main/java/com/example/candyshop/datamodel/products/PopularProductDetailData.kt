package com.example.candyshop.datamodel.products

data class PopularProductDetailData(
    val id: Int,
    val productName: String,
    val description: String,
    val ingredients: String,
    val shelfLife: String,
    val price: Float,
    val measurementEnumValue: Int,
    val imageUrl: String?,
    val categoryNames: Array<String>?,
    var favorite: Boolean

){
    /*constructor(type: String) : this(0, "", "", 0f, 0, null) {
        this.type = type;
    }*/
}