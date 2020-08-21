package com.example.candyshop.ui.productdetail

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.candyshop.R
import com.example.candyshop.adapter.toPx
import com.example.candyshop.common.Constants
import com.example.candyshop.common.ResizeAnimation
import kotlinx.android.synthetic.main.fragment_product_detail.*


class ProductDetailFragment : Fragment() {

    companion object {
        fun newInstance() = ProductDetailFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = activity?.intent?.getIntExtra(Constants.PRODUCT_ID_KEY, 0)
        val productDetailViewModel: ProductDetailViewModel by viewModels { ProductDetailViewModelFactory(id!!) }

        productDetailViewModel.productDetail.observe(viewLifecycleOwner, Observer {
            context?.let { it1 ->
                Glide
                    .with(it1)
                    .load(it.imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.loading_spinner)
                    .into(ivProduct)
            }

            tvCategory.text = it.categoryNames?.joinToString(", ") { it }
            tvProductName.text = it.productName
            tvPrice.text = it.price.toString().plus(" AMD")
            tvDescription.text = it.description
            tvIngredients.text = it.ingredients
            tvShelfLife.text = it.shelfLife
        })

        fab.setOnClickListener {
            animateCountView()
        }

        ibBack.setOnClickListener { activity?.finish() }
    }

    private fun animateCountView() {
        val displayMetrics = DisplayMetrics()
        activity?.getWindowManager()?.getDefaultDisplay()?.getMetrics(displayMetrics)

        ObjectAnimator.ofFloat(fab, "alpha", 1f, 0f).apply {
            duration = 100
            start()
            addListener(object: Animator.AnimatorListener{
                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    fab.visibility = View.GONE
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationCancel(animation: Animator?) {
                }
            })
        }

        val resizeFab = ResizeAnimation(llAnim, 66.toPx().toFloat(), 66.toPx().toFloat(), 40.toPx().toFloat(), 40.toPx().toFloat(), 100)
        llAnim.startAnimation(resizeFab)

        btnAddToBasket.layoutParams.width = displayMetrics.widthPixels - 190.toPx()

        ObjectAnimator.ofFloat(llAnim, "alpha", 0f, 1f).apply {
            duration = 300
            start()
        }

        val resizeCountView = ResizeAnimation(llAnim, 200f, 100.toPx().toFloat(), displayMetrics.widthPixels.toFloat(), 74.toPx().toFloat(), 300)
        llAnim.startAnimation(resizeCountView)
    }

}

