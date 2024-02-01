package com.example.groupproject.ui.destination

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groupproject.data.FirebaseApi
import com.example.groupproject.data.model.Destination
import com.example.groupproject.data.model.Review
import com.example.groupproject.data.model.Trip
import com.example.groupproject.data.model.User
import com.google.firebase.Timestamp
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

    fun addDestinationToTrip(destination: Destination, user: User, trip: Trip) {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseApi.addDestinationToTrip(destination, user, trip)
        }
    }

    fun getUser(userId: String, callback: (User?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseApi.getUserByEmail(userId) { result ->
                callback(result)
            }
        }
    }

    fun getAllDestination(callback: (List<Destination>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseApi.getAllDestinations { result ->
                callback(result)
            }
        }
    }

    fun toggleLike(destination: Destination) {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseApi.toggleLike(destination)
        }
    }

    fun saveDestination(destination: Destination, user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseApi.saveUserSaves(destination, user)
        }
    }

    fun removeDestinationFromSaved(destination: Destination, user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseApi.removeDestinationFromSaved(destination, user)
        }
    }

    fun getUserImage(userId: String, callback: (String?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseApi.getUserByEmail(userId) { result ->
                if (result != null) {
                    callback(result.profile[0].photoImage)
                }
            }
        }
    }

    fun addReview(destination: Destination, userEmail: String, rating: Int, reviewText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseApi.getUserByEmail(userEmail) { user ->
                if (user != null) {
                    val review = Review(
                        reviewId = userEmail,
                        userId = userEmail,
                        rating = rating,
                        description = reviewText,
                        timestamp = Timestamp.now(),
                        destination = destination.name,
                        photos = listOf(user.profile[0].photoImage),
                    )
                    firebaseApi.addReview(review, destination.name)
                }
            }
        }
    }
}