package ro.alexmamo.swipetodelete.data

data class DataOrException<T, E : Exception?>(var data: T? = null, var exception: E? = null)