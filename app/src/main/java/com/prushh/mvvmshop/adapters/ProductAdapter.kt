package com.prushh.mvvmshop.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prushh.mvvmshop.R
import com.prushh.mvvmshop.models.shop.Product
import kotlinx.android.synthetic.main.item_product_preview.view.*

/**
 * This class provide the display of articles in RecyclerView.
 * @param isSavedFragment Optional, used to show different information.
 */
class ProductAdapter(
    private var isSavedFragment: Boolean = false
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    /**
     * This inner class provide to override methods of RecyclerView.ViewHolder().
     * It can access to the members of its outer class.
     * @param itemView Single item displayed.
     */
    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    /**
     * It's called Object Expression: object of an anonymous class,
     * used for analyze changed items inside current list.
     */
    private val differCallback = object : DiffUtil.ItemCallback<Product>() {
        // Check if two items inside current list have the same id.
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        // Check if two items inside current list have the same contents.
        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    /**
     * Tool that takes two lists and compares them, calculating the differences.
     */
    val differ = AsyncListDiffer(this, differCallback)

    /**
     * Create the view holder by specifying the layout.
     * @param parent ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_product_preview,
                parent,
                false
            )
        )
    }

    /**
     * Get size of current list, that contain items.
     * @return Int: number of items in current list.
     */
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    /**
     * Defined in the Fragment.
     */
    private var onItemClickListener: ((Product) -> Unit)? = null

    /**
     * Fill UI fields with current item information.
     * @param holder Represent the contents of the item at che given position.
     * @param position Selected item position.
     */
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = differ.currentList[position]

        // Fill items information to UI.
        holder.itemView.apply {
            Glide.with(this).load(product.urlToImage).into(ivProductImage)
            tvTitle.text = product.title
            tvDescription.text = product.description
            tvCategory.text = resources.getString(R.string.category, product.category)
            tvQuantityOrDate.text = if (isSavedFragment) {
                resources.getString(R.string.save_at, product.saveAt)
            } else {
                resources.getString(R.string.quantity, product.quantity)
            }

            // If not null, provide to put current product.
            setOnClickListener {
                onItemClickListener?.let { it(product) }
            }
        }
    }

    /**
     * Used to define the listener in the fragment.
     */
    fun setOnItemClickListener(listener: (Product) -> Unit) {
        onItemClickListener = listener
    }
}