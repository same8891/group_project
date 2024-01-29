package com.example.groupproject.ui.profile

import androidx.lifecycle.ViewModel
import com.example.groupproject.data.FirebaseApi
import com.example.groupproject.data.model.User
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

}