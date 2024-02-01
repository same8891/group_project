package com.example.groupproject.ui.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.groupproject.data.FirebaseApi
import com.example.groupproject.data.model.Destination
import com.example.groupproject.data.model.Profile
import com.example.groupproject.data.model.Review
import com.example.groupproject.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class profileViewModel(private val firebaseApi: FirebaseApi) : ViewModel() {

    private val _userProfile = MutableStateFlow<Profile?>(null)
    val userProfile: MutableStateFlow<Profile?> = _userProfile

    private val _user = MutableStateFlow<User?>(null)
    val user: MutableStateFlow<User?> = _user

    fun getUserProfile(email: String) {
        firebaseApi.getUserByEmail(email) { user ->
            if (user != null) {
                _userProfile.value = user.profile[0]
            }
        }
    }

    fun getUser(email: String, callback: (User?) -> Unit) {
        firebaseApi.getUserByEmail(email) { user ->
            if (user != null) {
                _user.value = user
                callback(user)
            }
        }
    }

    fun updateUser(user: User, userId: String) {
        firebaseApi.saveUser(user, userId)
    }

    fun updateProfile(userId: String, profile: Profile) {
        firebaseApi.saveProfile(userId, profile)
    }

    fun uploadImage(userId: String, uri: Uri, callback: (String?) -> Unit) {
        firebaseApi.uploadImage(userId, uri) { imageUrl ->
            callback(imageUrl)
        }
    }


    fun uploadUserImage(userId: String, url: String) {
        firebaseApi.addUserProfileImageList(userId, url)
    }

    fun updateProfileImageFromUrl(userId: String, imageUrl: String) {
        firebaseApi.updateProfileImage(userId, imageUrl)
    }

    fun updateProfileImageFromUri(userId: String, uri: Uri) {
        firebaseApi.uploadImage(userId, uri) { imageUrl ->
            if (imageUrl != null) {
                firebaseApi.updateProfileImage(userId, imageUrl)
            }
        }
    }

    fun deleteUserReview(userId: String, reviewId: String, destinationId: String) {
        firebaseApi.deleteUserReview(userId, reviewId)
        firebaseApi.deleteDestinationReview(destinationId, reviewId)
    }

    fun getUserSavedDestinations(userId: String, callback: (List<Destination>) -> Unit) {
        firebaseApi.getUserSavedDestinations(userId) { destinations ->
            callback(destinations)
        }
    }

    fun removeDestinationReview(destination: String, reviewId: String) {
        firebaseApi.deleteDestinationReview(destination, reviewId)
    }

    fun updateDestinationReview(destination: String, reviewId: String, it: Review) {
        firebaseApi.updateDestinationReview(destination, reviewId, it)
    }
}