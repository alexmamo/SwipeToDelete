package ro.alexmamo.swipetodelete.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import ro.alexmamo.swipetodelete.BR
import ro.alexmamo.swipetodelete.R
import ro.alexmamo.swipetodelete.data.Product
import ro.alexmamo.swipetodelete.products.ProductsAdapter.ProductViewHolder

class ProductsAdapter(
    private val productList: List<Product> = mutableListOf()
): RecyclerView.Adapter<ProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemProduct = R.layout.item_product
        val dataBinding = DataBindingUtil.inflate<ViewDataBinding>(
            layoutInflater,
            itemProduct,
            parent,
            false
        )
        return ProductViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bindProduct(product)
    }

    override fun getItemCount() = productList.size

    inner class ProductViewHolder(
        private val dataBinding: ViewDataBinding
    ): RecyclerView.ViewHolder(dataBinding.root) {
        fun bindProduct(product: Product) = dataBinding.setVariable(BR.product, product)
    }
}