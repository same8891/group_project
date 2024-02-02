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
    fun getUserTrips(email: String, callback: (List<Trip>?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseApi.getUserTrips(email) { tripsList ->
                trips.value = tripsList ?: emptyList()
                callback(trips.value)
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

    fun getAllDestinationIds(callback: (List<String>?) -> Unit) {
        firebaseApi.getAllDestinationIds { destinationIdsList ->
            destinationIds = destinationIdsList ?: emptyList()
            callback(destinationIdsList)
        }
    }

    fun updateTrip(userId: String, tripId: String, newTrip: Trip,callback: (Trip?) -> Unit) {
        firebaseApi.updateTrip(userId, tripId, newTrip, callback)
    }

    fun getTrip(userId: String, tripId: String, callback: (Trip?) -> Unit) {
        firebaseApi.getTrip(userId, tripId, callback)
    }

    fun calculateTotalCostForTrip(trip: Trip, callback: (Double) -> Unit) {
        var totalCost = 0.0
        val destinationCount = trip.destinationList.size
        var processedDestinations = 0

        trip.destinationList.forEach { destinationName ->
            getDestinationByName(destinationName) { destination ->
                processedDestinations++
                totalCost += destination?.price?.value ?: 0.0

                if (processedDestinations == destinationCount) {
                    callback(totalCost)
                }
            }
        }
    }
}