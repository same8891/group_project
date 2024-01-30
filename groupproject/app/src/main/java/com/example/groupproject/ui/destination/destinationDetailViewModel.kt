package com.example.groupproject.ui.destination

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groupproject.data.FirebaseApi
import com.example.groupproject.data.model.Destination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class destinationDetailViewModel(private val firebaseApi: FirebaseApi) : ViewModel() {
    private var destination: Destination? = null

    fun getDestinationByName(destinationName: String, callback: (Destination?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseApi.getDestinationByName(destinationName) { result ->
                destination = result
                callback(result)
            }
        }
    }

    fun getDestinationDetail(): Destination? {
        return destination
    }

    fun updateReviewLikes(userId: String, likes: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            destination?.let {
                firebaseApi.updateReviewLikes(it, userId, likes)
            }
        }
    }
}