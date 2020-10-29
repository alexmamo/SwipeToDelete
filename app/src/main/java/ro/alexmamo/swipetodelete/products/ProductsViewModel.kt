package ro.alexmamo.swipetodelete.products

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers

class ProductsViewModel @ViewModelInject constructor(
    private val repository: ProductsRepository
): ViewModel() {
    val productListLiveData = liveData(Dispatchers.IO) {
        val productList = repository.getProductListFromFirestore()
        emit(productList)
    }

    fun deleteProduct(id: String) = liveData(Dispatchers.IO) {
        val isProductDeleted = repository.deleteProductInFirestore(id)
        emit(isProductDeleted)
    }
}