package ro.alexmamo.swipetodelete.utils

import android.util.Log
import ro.alexmamo.swipetodelete.utils.Constants.TAG

class General {
    companion object {
        fun logErrorMessage(errorMessage: String) {
            Log.d(TAG, errorMessage)
        }
    }
}