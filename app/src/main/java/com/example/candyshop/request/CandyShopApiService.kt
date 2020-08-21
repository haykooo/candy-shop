package com.example.candyshop.request

import com.example.candyshop.datamodel.category.CategoryResponseData
import com.example.candyshop.datamodel.products.PopularProductDetailResponseData
import com.example.candyshop.datamodel.products.PopularProductResponseData
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query

private const val BASE_URL = "http://212.42.196.110:6121/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface CandyShopApiService {
    @GET("category/getTopCategoriesForMobile")
    fun getTopCategories(@HeaderMap headers: Map<String, String>) :
            Deferred<CategoryResponseData>

    @GET("product/getMostPopularProducts")
    fun getMostPopularProducts(@HeaderMap headers: Map<String, String>) :
            Deferred<PopularProductResponseData>

    @GET("product/getProductById")
    fun getProductById(@Query("id") id: Int, @HeaderMap headers: Map<String, String>) :
            Deferred<PopularProductDetailResponseData>


}

object CandyShopApi {
    val retrofitService : CandyShopApiService by lazy { retrofit.create(CandyShopApiService::class.java) }
}