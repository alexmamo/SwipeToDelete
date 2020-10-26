package ro.alexmamo.swipetodelete.products

import android.view.LayoutInflater.from
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.inflate
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import ro.alexmamo.swipetodelete.BR
import ro.alexmamo.swipetodelete.R
import ro.alexmamo.swipetodelete.data.Product
import ro.alexmamo.swipetodelete.products.ProductsAdapter.ProductViewHolder

class ProductsAdapter(
    private val products: List<Product> = mutableListOf()
): RecyclerView.Adapter<ProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = from(parent.context)
        val dataBinding = inflate<ViewDataBinding>(layoutInflater, R.layout.item_product, parent, false)
        return ProductViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bindProduct(product)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    inner class ProductViewHolder(
        private val dataBinding: ViewDataBinding
    ): RecyclerView.ViewHolder(dataBinding.root) {

        fun bindProduct(product: Product) {
            dataBinding.setVariable(BR.product, product)
        }
    }
}