package com.example.groupproject.ui.setting

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groupproject.data.FirebaseApi
import com.example.groupproject.data.model.Destination
import com.example.groupproject.data.model.Feedback
import com.example.groupproject.data.model.Profile
import com.example.groupproject.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class settingViewModel(private val firebaseApi: FirebaseApi) : ViewModel() {

    private val _userProfile = MutableStateFlow<User?>(null)
    val userProfile: StateFlow<User?> = _userProfile

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    fun getUserProfile(email: String) {
        firebaseApi.getUserByEmail(email) { user ->
            _userProfile.value = user
        }
    }

    fun getUser(email: String) {
        firebaseApi.getUserByEmail(email) { user ->
            _user.value = user
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


    // LiveData to hold the list of destinations
    val feedback = mutableStateOf<List<Feedback>>(emptyList())

    // Function to fetch all destinations
    fun getFeedbacks() {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseApi.getAllFeedbacks { feedbackList ->
                feedback.value = feedbackList
            }
        }
    }

    fun addFeedback(feedback: Feedback) {
        firebaseApi.addFeedback(feedback = feedback)
    }
}