package com.example.candyshop.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.candyshop.common.Constants
import com.example.candyshop.datamodel.category.CategoryData
import com.example.candyshop.datamodel.products.PopularProductData
import com.example.candyshop.request.CandyShopApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

enum class CandyShopApiStatus { LOADING, ERROR, DONE }

class HomeViewModel() : ViewModel() {

    private val _status = MutableLiveData<CandyShopApiStatus>()

    val status: LiveData<CandyShopApiStatus>
        get() = _status

    private val _categories = MutableLiveData<MutableList<CategoryData>>()
    val categories: LiveData<MutableList<CategoryData>>
        get() = _categories

    private val _popularProducts = MutableLiveData<MutableList<PopularProductData>>()
    val popularProducts: LiveData<MutableList<PopularProductData>>
        get() = _popularProducts

    private val _navigateToSelectedProductDetail = MutableLiveData<PopularProductData>()
    val navigateToSelectedProductDetail: LiveData<PopularProductData>
        get() = _navigateToSelectedProductDetail

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getCategories()
        getPopularProducts()
    }

    fun displayProductDetails(product: PopularProductData) {
        _navigateToSelectedProductDetail.value = product
    }

    fun displayProductDetailsComplete() {
        _navigateToSelectedProductDetail.value = null
    }

    private fun getCategories() {
        coroutineScope.launch {
            val headers: Map<String, String> = hashMapOf("authorization" to Constants.TOKEN, "languageName" to "en")
            val getCategoriesDeferred = CandyShopApi.retrofitService.getTopCategories(headers)
            try {
                _status.value = CandyShopApiStatus.LOADING
                val result = getCategoriesDeferred.await()
                _status.value = CandyShopApiStatus.DONE
                _categories.value = result.data
            } catch (e: Exception) {
                _status.value = CandyShopApiStatus.ERROR
                _categories.value = ArrayList()
            }
        }
    }

    private fun getPopularProducts() {
        coroutineScope.launch {
            val headers: Map<String, String> = hashMapOf("authorization" to Constants.TOKEN, "languageName" to "en")
            val getPopularProductsDeferred = CandyShopApi.retrofitService.getMostPopularProducts(headers)
            try {
                _status.value = CandyShopApiStatus.LOADING
                val result = getPopularProductsDeferred.await()
                _status.value = CandyShopApiStatus.DONE
                _popularProducts.value = result.data
            } catch (e: Exception) {
                _status.value = CandyShopApiStatus.ERROR
                _popularProducts.value = ArrayList()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}