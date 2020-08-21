package com.example.candyshop.ui.productdetail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.candyshop.common.Constants
import com.example.candyshop.datamodel.products.PopularProductDetailData
import com.example.candyshop.request.CandyShopApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


enum class CandyShopApiStatus { LOADING, ERROR, DONE }

class ProductDetailViewModel(val id: Int) : ViewModel() {

    private val _status = MutableLiveData<CandyShopApiStatus>()

    val status: LiveData<CandyShopApiStatus>
        get() = _status

    private val _productDetail = MutableLiveData<PopularProductDetailData>()

    val productDetail: LiveData<PopularProductDetailData>
        get() = _productDetail


    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getProductDetail(id)
    }

    private fun getProductDetail(id: Int) {
        coroutineScope.launch {
            val headers: Map<String, String> = hashMapOf("authorization" to Constants.TOKEN, "languageName" to "en")
            val getCategoriesDeferred = CandyShopApi.retrofitService.getProductById(id, headers)
            try {
                _status.value = CandyShopApiStatus.LOADING
                val result = getCategoriesDeferred.await()
                _status.value = CandyShopApiStatus.DONE
                _productDetail.value = result.data
            } catch (e: Exception) {
                _status.value = CandyShopApiStatus.ERROR
                _productDetail.value = null
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}

class ProductDetailViewModelFactory(private val mParam: Int) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProductDetailViewModel(mParam) as T
    }

}