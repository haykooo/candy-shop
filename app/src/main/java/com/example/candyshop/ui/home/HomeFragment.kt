package com.example.candyshop.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.candyshop.ProductDetailActivity
import com.example.candyshop.R
import com.example.candyshop.adapter.GeneralRecyclerAdapter
import com.example.candyshop.common.Constants
import com.example.candyshop.datamodel.products.PopularProductData


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val onAddToCartListener = {product: PopularProductData -> println(product)}
        val productClick = {product: PopularProductData -> homeViewModel.displayProductDetails(product)}

        val recyclerView: RecyclerView = root.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val generalRecyclerAdapter = context?.let { GeneralRecyclerAdapter(it, onAddToCartListener, productClick) }
        recyclerView.adapter = generalRecyclerAdapter

        homeViewModel.categories.observe(viewLifecycleOwner, Observer {
            generalRecyclerAdapter?.setCategories(it)
        })

        homeViewModel.popularProducts.observe(viewLifecycleOwner, Observer {
            generalRecyclerAdapter?.setPopularProducts(it)
        })

        homeViewModel.navigateToSelectedProductDetail.observe(viewLifecycleOwner, Observer {
            if ( null != it ) {
                startProductDetailActivity(it.id)
                homeViewModel.displayProductDetailsComplete()
            }
        })

        return root
    }

    fun startProductDetailActivity(key: Int){
        val intent = Intent(context, ProductDetailActivity::class.java)
        val b = Bundle()
        b.putInt(Constants.PRODUCT_ID_KEY, key)
        intent.putExtras(b)

        startActivity(intent)
        activity?.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)

    }
}
