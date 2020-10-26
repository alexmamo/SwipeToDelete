package ro.alexmamo.swipetodelete.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import ro.alexmamo.swipetodelete.utils.Constants.TAG

class General {
    companion object {
        fun logErrorMessage(errorMessage: String) {
            Log.d(TAG, errorMessage)
        }

        fun toastMessage(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}