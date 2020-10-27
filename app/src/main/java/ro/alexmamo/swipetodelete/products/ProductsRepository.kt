package ro.alexmamo.swipetodelete.products

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query.Direction.ASCENDING
import ro.alexmamo.swipetodelete.data.DataOrException
import ro.alexmamo.swipetodelete.data.Product
import ro.alexmamo.swipetodelete.utils.Constants.NAME_PROPERTY
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductsRepository @Inject constructor(private val productsRef: CollectionReference) {
    fun getProductListFromFirestore(): MutableLiveData<DataOrException<MutableList<Product>, Exception>> {
        val mutableLiveData = MutableLiveData<DataOrException<MutableList<Product>, Exception>>()
        productsRef.orderBy(NAME_PROPERTY, ASCENDING).get().addOnCompleteListener { productsTask ->
            val dataOrException = DataOrException<MutableList<Product>, Exception>()
            if (productsTask.isSuccessful) {
                val products = mutableListOf<Product>()
                for (document in productsTask.result!!) {
                    val product = document.toObject(Product::class.java)
                    products.add(product)
                }
                dataOrException.data = products
            } else {
                dataOrException.exception = productsTask.exception
            }
            mutableLiveData.setValue(dataOrException)
        }
        return mutableLiveData
    }

    fun deleteProductInFirestore(id: String): MutableLiveData<DataOrException<Boolean, Exception>> {
        val mutableLiveData = MutableLiveData<DataOrException<Boolean, Exception>>()
        productsRef.document(id).delete().addOnCompleteListener { deleteTask ->
            val dataOrException = DataOrException<Boolean, Exception>()
            if (deleteTask.isSuccessful) {
                dataOrException.data = true
            } else {
                dataOrException.exception = deleteTask.exception
            }
            mutableLiveData.setValue(dataOrException)
        }
        return mutableLiveData
    }
}