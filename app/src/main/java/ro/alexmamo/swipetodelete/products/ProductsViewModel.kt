package ro.alexmamo.swipetodelete.products

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel

class ProductsViewModel @ViewModelInject constructor(
    private val repository: ProductsRepository
): ViewModel() {
    fun getProductList() = repository.getProductListFromFirestore()

    fun deleteProduct(id: String) = repository.deleteProductInFirestore(id)
}