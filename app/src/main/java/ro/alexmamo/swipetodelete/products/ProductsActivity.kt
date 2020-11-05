package ro.alexmamo.swipetodelete.products

import android.os.Bundle
import android.view.View.GONE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ro.alexmamo.swipetodelete.R
import ro.alexmamo.swipetodelete.data.Product
import ro.alexmamo.swipetodelete.databinding.ActivityProductsBinding
import ro.alexmamo.swipetodelete.utils.General.Companion.logErrorMessage

@AndroidEntryPoint
class ProductsActivity : AppCompatActivity() {
    private lateinit var dataBinding: ActivityProductsBinding
    private val products: MutableList<Product> = mutableListOf()
    private val adapter = ProductsAdapter(products)
    private val viewModel: ProductsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        dataBinding = setContentView(this, R.layout.activity_products)
        setProductsAdapter()
        getProductList()
        setOnRecyclerViewItemSwipedListener()
    }

    private fun setProductsAdapter() {
        dataBinding.productsRecyclerView.adapter = adapter
    }

    private fun getProductList() {
        viewModel.productListLiveData.observe(this, { dataOrException ->
            val productList = dataOrException.data
            if (productList != null) {
                if (products.isNotEmpty()) {
                    productList.clear()
                }
                products.addAll(productList)
                adapter.notifyDataSetChanged()
                hideProgressBar()
            }

            if (dataOrException.e != null) {
                logErrorMessage(dataOrException.e!!.message!!)
            }
        })
    }

    private fun hideProgressBar() {
        dataBinding.progressBar.visibility = GONE
    }

    private fun setOnRecyclerViewItemSwipedListener() {
        ItemTouchHelper(object : SimpleCallback(0, LEFT or RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val swipedProduct = products[position]
                deleteProduct(position, swipedProduct)
            }

        }).attachToRecyclerView(dataBinding.productsRecyclerView)
    }

    private fun deleteProduct(position: Int, product: Product) {
        val isProductDeletedLiveData = viewModel.deleteProduct(product.id!!)
        isProductDeletedLiveData.observe(this, { dataOrException ->
            val isProductDeleted = dataOrException.data
            if (isProductDeleted != null) {
                if (isProductDeleted) {
                    products.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    adapter.notifyItemRangeChanged(position, products.size)
                }
            }

            if (dataOrException.e != null) {
                logErrorMessage(dataOrException.e!!.message!!)
            }
        })
    }
}