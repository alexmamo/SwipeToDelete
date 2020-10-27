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
    private val productList: MutableList<Product> = mutableListOf()
    private val adapter = ProductsAdapter(productList)
    private val viewModel: ProductsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        dataBinding = setContentView(this, R.layout.activity_products)
        setProductsAdapter()
        getProducts()
        setOnRecyclerViewItemSwipedListener()
    }

    private fun setProductsAdapter() {
        dataBinding.productsRecyclerView.adapter = adapter
    }

    private fun getProducts() {
        val productsLiveData = viewModel.getProductList()
        productsLiveData.observe(this, { dataOrException ->
            val productList = dataOrException.data
            if (productList != null) {
                if (this.productList.isNotEmpty()) {
                    productList.clear()
                }
                this.productList.addAll(productList)
                adapter.notifyDataSetChanged()
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
                val swipedProduct = productList[position]
                deleteSwipedProduct(position, swipedProduct)

            }

        }).attachToRecyclerView(dataBinding.productsRecyclerView)
    }

    private fun deleteSwipedProduct(position: Int, swipedProduct: Product) {
        val isProductDeletedLiveData = viewModel.deleteProduct(swipedProduct.id!!)
        isProductDeletedLiveData.observe(this, { dataOrException ->
            val isProductDeleted = dataOrException.data
            if (isProductDeleted != null) {
                if (isProductDeleted) {
                    productList.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    adapter.notifyItemRangeChanged(position, productList.size)
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