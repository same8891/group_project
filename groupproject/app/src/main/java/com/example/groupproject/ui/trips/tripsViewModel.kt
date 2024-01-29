package com.example.groupproject.ui.trips

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groupproject.data.FirebaseApi
import com.example.groupproject.data.model.Trip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class tripsViewModel(private val firebaseApi: FirebaseApi) : ViewModel() {
    // LiveData to hold the list of trips
    val trips = mutableStateOf<List<Trip>>(emptyList())

    // Function to fetch all trips for a user
    fun getUserTrips(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseApi.getUserByEmail(email) { user ->
                user?.let {
                    trips.value = user.trips
                }
            }
        }
    }
}