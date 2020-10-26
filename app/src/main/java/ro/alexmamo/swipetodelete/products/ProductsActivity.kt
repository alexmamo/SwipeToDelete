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
import ro.alexmamo.swipetodelete.utils.General.Companion.toastMessage

@AndroidEntryPoint
class ProductsActivity : AppCompatActivity() {
    private lateinit var dataBinding: ActivityProductsBinding
    private val products: MutableList<Product> = mutableListOf()
    private val productsAdapter = ProductsAdapter(products)
    private val productViewModel: ProductsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        dataBinding = setContentView(this, R.layout.activity_products)
        setOnRecyclerViewItemSwipedListener()
        setProductsAdapter()
        getProducts()
    }

    private fun setProductsAdapter() {
        dataBinding.productsRecyclerView.adapter = productsAdapter
    }

    private fun getProducts() {
        val productsLiveData = productViewModel.getProducts()
        productsLiveData.observe(this, { dataOrException ->
            val productList = dataOrException.data
            if (productList != null) {
                if (products.isNotEmpty()) {
                    productList.clear()
                }
                products.addAll(productList)
                productsAdapter.notifyDataSetChanged()
                hideProgressBar()
            }

            if (dataOrException.exception != null) {
                logErrorMessage(dataOrException.exception!!.message!!)
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
                deleteSwipedProduct(position, swipedProduct)

            }

        }).attachToRecyclerView(dataBinding.productsRecyclerView)
    }

    private fun deleteSwipedProduct(position: Int, swipedProduct: Product) {
        val isProductDeletedLiveData = productViewModel.deleteProduct(swipedProduct.id!!)
        isProductDeletedLiveData.observe(this, { dataOrException ->
            val isProductDeleted = dataOrException.data
            if (isProductDeleted != null) {
                if (isProductDeleted) {
                    products.removeAt(position)
                    productsAdapter.notifyItemRemoved(position)
                    productsAdapter.notifyItemRangeChanged(position, products.size)
                    val message = swipedProduct.name + " is deleted!"
                    toastMessage(this, message)
                }
            }

            if (dataOrException.exception != null) {
                logErrorMessage(dataOrException.exception!!.message!!)
            }
        })
    }
}