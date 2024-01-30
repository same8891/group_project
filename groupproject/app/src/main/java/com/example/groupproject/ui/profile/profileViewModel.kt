package com.example.groupproject.ui.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.groupproject.data.FirebaseApi
import com.example.groupproject.data.model.Profile
import com.example.groupproject.data.model.User
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class profileViewModel(private val firebaseApi: FirebaseApi) : ViewModel() {

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
}