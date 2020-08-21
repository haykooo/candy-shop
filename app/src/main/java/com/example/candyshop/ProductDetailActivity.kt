package com.example.candyshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.candyshop.ui.productdetail.ProductDetailFragment

class ProductDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_detail_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ProductDetailFragment.newInstance())
                .commitNow()
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down)
    }
}