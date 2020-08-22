package com.example.candyshop.adapter

import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.Matrix
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.candyshop.R
import com.example.candyshop.datamodel.category.CategoryData
import com.example.candyshop.datamodel.products.PopularProductData

class GeneralRecyclerAdapter(private val context: Context,
                             private var categoriesData: MutableList<CategoryData>?,
                             private var popularProducts: MutableList<PopularProductData>?,
                             private val onAddToCartListener: (PopularProductData) -> Unit,
                             private val onProductClick: (PopularProductData) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    constructor(context: Context,
                onAddToCartListener: (PopularProductData) -> Unit,
                productClick: (PopularProductData) -> Unit) : this(context,null, null, onAddToCartListener, productClick)

    companion object {
        val VIEW_TYPE_HEADER = 0
        val VIEW_TYPE_CATEGORIES = 1
        val VIEW_TYPE_PRODUCTS = 2
    }

    class HeaderViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.header_list_item, parent, false))

    class CategoriesViewHolder(
        val categoriesData: MutableList<CategoryData>?,
        inflater: LayoutInflater,
        parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.categories_list_layout, parent, false)){

        private var recyclerView : RecyclerView? = null
        private var linearLayoutManager: LinearLayoutManager
        private var currentDegree = 0f

        init {
            recyclerView = itemView.findViewById(R.id.recyclerCategories)
            val categoriesRecyclerAdapter = CategoriesRecyclerAdapter(itemView.context, categoriesData)
            linearLayoutManager = LinearLayoutManager(itemView.context)
            linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            recyclerView!!.layoutManager = linearLayoutManager
            recyclerView!!.isNestedScrollingEnabled = true
            recyclerView!!.setHasFixedSize(true)
            recyclerView!!.adapter = categoriesRecyclerAdapter

            recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    animateViewsOfRecyclerView(dx, categoriesRecyclerAdapter)
                }
            })
        }

        fun getVisibleItems(count: Int) : MutableList<View> {
            val list = mutableListOf<View>()

            /////////// linearLayoutManager.findFirstVisibleItemPosition() --- not working correctly ...
            //val firstIndex = linearLayoutManager.findFirstVisibleItemPosition()
            //val lastIndex = linearLayoutManager.findLastVisibleItemPosition()
            //for (position in firstIndex..lastIndex) {

            for (position in 0..count) {
                recyclerView?.getChildAt(position)?.let {
                    list.add(it)
                }
            }

            return list
        }

        private fun rotateImage(imageView: ImageView, degree: Float) {
            val matrix = Matrix()
            imageView.scaleType = ImageView.ScaleType.MATRIX //required
            matrix.postRotate(
                degree,
                (imageView.drawable.bounds.width() / 2).toFloat(),
                (imageView.drawable.bounds.height() / 2).toFloat()
            )
            imageView.imageMatrix = matrix
        }

        fun animateViewsOfRecyclerView(dx: Int, categoriesRecyclerAdapter: CategoriesRecyclerAdapter) {
            if (dx == 0)
                return

            getVisibleItems(categoriesRecyclerAdapter.itemCount).forEach{
                val categoryImage = it.findViewById<ImageView>(R.id.ivCategoryImage)
                var degree = 0f
                if (dx > 0){
                    degree = 0.1f
                } else if (dx < 0){
                    degree = -0.1f
                }

                currentDegree += degree
                rotateImage(categoryImage, currentDegree + degree)
            }


            /*for (i in 0 until categoriesRecyclerAdapter.itemCount) {
                val holder = recyclerView!!.findViewHolderForAdapterPosition(i) ?: continue

                categoriesRecyclerAdapter.animate(dx, holder as CategoriesRecyclerAdapter.CategoryViewHolder)
            }*/
        }
    }

    class PopularProductViewHolder(val onAddToCartListener: (PopularProductData) -> Unit, val onProductClick: (PopularProductData) -> Unit, inflater: LayoutInflater, parent: ViewGroup ) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.popular_product_list_item, parent, false)){
        private var productName : TextView? = null
        private var price : TextView? = null
        private var ibAddToCart : ImageButton? = null
        private var productImage : ImageView? = null
        private var viewMaxAnimSize : View? = null
        private var llResizable : View? = null
        private var llCount : View? = null

        init {
            productName = itemView.findViewById(R.id.tvProductName)
            price = itemView.findViewById(R.id.tvPrice)
            ibAddToCart = itemView.findViewById(R.id.ibAddToCart)
            productImage = itemView.findViewById(R.id.ivPopularProduct)
            viewMaxAnimSize = itemView.findViewById(R.id.viewMaxAnimSize)
            llResizable = itemView.findViewById(R.id.llResizable)
            llCount = itemView.findViewById(R.id.llCount)
        }

        fun bind(popularProduct: PopularProductData) {
            itemView.setOnClickListener { onProductClick(popularProduct) }

            productName?.text = popularProduct.productName
            price?.text = popularProduct.price.toString().plus(" ").plus(itemView.context.getString(R.string.d_kg))

            ibAddToCart?.setOnClickListener {
                showCountAnim()

                //onAddToCartListener(popularProduct)
            }

            llCount?.setOnClickListener {
                hideCountAnim()
            }

            productImage?.let {
                Glide
                    .with(itemView.context)
                    .load(popularProduct.imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.loading_spinner)
                    .into(it)
            };
        }

        fun showCountAnim() {
            val maxAnimSize: Float = viewMaxAnimSize!!.width.toFloat() - 50
            ObjectAnimator.ofFloat(price!!, "translationX", maxAnimSize).apply {
                duration = 300
                start()
            }

            llResizable?.layoutParams?.width = 85.toPx()
            llResizable?.requestLayout()

            ObjectAnimator.ofFloat(ibAddToCart!!, "translationX", 300f).apply {
                duration = 300
                start()
            }

            ObjectAnimator.ofFloat(llCount!!, "translationX", /*94.toPx().toFloat()*/0f).apply {
                duration = 300
                start()
            }

        }

        fun hideCountAnim() {
            ObjectAnimator.ofFloat(price!!, "translationX", 0f).apply {
                duration = 300
                start()
            }

            llResizable?.layoutParams?.width = 50.toPx()
            llResizable?.requestLayout()

            ObjectAnimator.ofFloat(ibAddToCart!!, "translationX", 0f).apply {
                duration = 300
                start()
            }

            ObjectAnimator.ofFloat(llCount!!, "translationX", /*94.toPx().toFloat()*/-94.toPx().toFloat()).apply {
                duration = 300
                start()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            VIEW_TYPE_HEADER -> HeaderViewHolder(LayoutInflater.from(context), parent)
            VIEW_TYPE_CATEGORIES -> CategoriesViewHolder(categoriesData, LayoutInflater.from(context), parent)
            else -> PopularProductViewHolder( onAddToCartListener, onProductClick, LayoutInflater.from(context), parent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(popularProducts?.get(position)?.type) {
            "header" -> VIEW_TYPE_HEADER
            "categories" -> VIEW_TYPE_CATEGORIES
            else -> VIEW_TYPE_PRODUCTS
        }
    }

    override fun getItemCount(): Int {
        return if (popularProducts == null) 0 else popularProducts!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            // TODO: 20-Aug-20 nothing at all
        } else if (holder is CategoriesViewHolder) {
            // TODO: 20-Aug-20 There is nothing to do here
        } else if (holder is PopularProductViewHolder){
            popularProducts?.get(position)?.let { holder.bind(it) }
        }
    }

    fun setCategories(categoriesData: MutableList<CategoryData>) {
        /*val lst = categoriesData.toMutableList()
        categoriesData.addAll(lst)
        categoriesData.addAll(lst)
        categoriesData.addAll(lst)
        categoriesData.addAll(lst)
        categoriesData.addAll(lst)*/

        // qani vor yndameny 2 item a galis normal chi erevum, dra hamar es verevi commenty karaq baceq nayenq vonc a stacvel...
        // karcum em sranic lav dzev mekel duq kaseq :D

        this.categoriesData = categoriesData
        notifyDataSetChanged()
    }

    fun setPopularProducts(popularProducts: MutableList<PopularProductData>) {
        this.popularProducts = popularProducts

        // check if already added
        if (this.popularProducts!![0].type != "header"){
            val headerItem = PopularProductData("header")
            val categoriesItem = PopularProductData("categories")
            this.popularProducts?.add(0, categoriesItem)
            this.popularProducts?.add(0, headerItem)
        }

        notifyDataSetChanged()
    }
}

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()
fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()