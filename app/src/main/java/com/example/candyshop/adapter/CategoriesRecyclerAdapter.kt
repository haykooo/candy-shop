package com.example.candyshop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.candyshop.R
import com.example.candyshop.datamodel.category.CategoryData

class CategoriesRecyclerAdapter(
    private val context: Context,
    private var categoriesData: MutableList<CategoryData>?
) : RecyclerView.Adapter<CategoriesRecyclerAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.category_list_item, parent, false)) {
        private var categoryName: TextView? = null
        private var productCount: TextView? = null
        private var categoryImage: ImageView? = null
        private var cardView: CardView? = null
        private var flBackgroundGradient: FrameLayout? = null

        private var rotateDegree = 0f
        private var animationEnded = true

        init {
            categoryName = itemView.findViewById(R.id.tvCategoryName)
            productCount = itemView.findViewById(R.id.tvProductCount)
            categoryImage = itemView.findViewById(R.id.ivCategoryImage)
            cardView = itemView.findViewById(R.id.cardView)
            flBackgroundGradient = itemView.findViewById(R.id.flBackgroundGradient)
        }

        fun bind(categoryData: CategoryData, position: Int) {
            categoryName?.text = categoryData.categoryName
            productCount?.text = categoryData.productCount.toString().plus(" ")
                .plus(itemView.context.getString(R.string.products))

            // gradient colors are null - so I solved it this way
            flBackgroundGradient?.setBackgroundResource(if (position == 0) R.drawable.gradient_first else R.drawable.gradient_second)

            categoryImage?.let {
                Glide
                    .with(itemView.context)
                    .load(categoryData.imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.loading_spinner)
                    .into(it)
            }
        }

        fun rotate(dx: Float) {
            if (animationEnded) {

                var currentDegree = 0f
                if (dx > 0){
                    currentDegree = 5f
                } else if (dx < 0) {
                    currentDegree = -5f
                } else {
                    return
                }

                val rotateAnimation = RotateAnimation(rotateDegree, rotateDegree + currentDegree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
                rotateDegree += currentDegree

                rotateAnimation.duration = 100
                rotateAnimation.fillAfter = true
                rotateAnimation.setAnimationListener(object: Animation.AnimationListener {
                    override fun onAnimationEnd(animation: Animation?) {
                        animationEnded = true
                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                    }

                    override fun onAnimationStart(animation: Animation?) {
                    }
                })

                animationEnded = false
                categoryImage?.startAnimation(rotateAnimation)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(context), parent)
    }

    override fun getItemCount(): Int {
        return if (categoriesData == null) 0 else categoriesData!!.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        categoriesData?.get(position)?.let { holder.bind(it, position) }
    }

    fun animate(dx: Float, holder: CategoryViewHolder) {
        holder.rotate(dx)
    }


}