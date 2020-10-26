package ro.alexmamo.swipetodelete.products

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel

class ProductsViewModel @ViewModelInject constructor(
    private val productsRepository: ProductsRepository
): ViewModel() {
    fun getProducts() = productsRepository.getProductsFromFirestore()

    fun deleteProduct(productId: String) = productsRepository.deleteProductInFirestore(productId)
}