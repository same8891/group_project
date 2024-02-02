package com.example.groupproject.ui.trips

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groupproject.data.FirebaseApi
import com.example.groupproject.data.model.Destination
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

    fun addTrip(userId: String, newTrip: Trip){
        firebaseApi.addTrip(userId, newTrip)
    }

    fun deleteTrip(userId: String, tripId: String) {
        firebaseApi.deleteTrip(userId, tripId)
    }

    fun getDestinationByName(destinationName: String, callback: (Destination?) -> Unit) {
        firebaseApi.getDestinationByName(destinationName, callback)
    }

    val destinations = mutableStateOf<List<Destination>>(emptyList())

    // Function to fetch all destinations
    fun getAllDestinations() {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseApi.getAllDestinations { destinationsList ->
                destinations.value = destinationsList
            }

        }
    }
    var destinationIds: List<String> = emptyList()

    fun getAllDestinationIds() {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseApi.getAllDestinations { destinationsList ->
                destinationIds = destinationsList.map { it.destinationId }
                Log.d("DestinationIds", "$destinationIds")
            }
        }
    }

    fun updateTrip(userId: String, tripId: String, newTrip: Trip) {
        firebaseApi.updateTrip(userId, tripId, newTrip)
    }
}