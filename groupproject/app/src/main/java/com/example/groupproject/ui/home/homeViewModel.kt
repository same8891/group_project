package com.example.groupproject.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groupproject.data.FirebaseApi
import com.example.groupproject.data.model.Destination
import com.example.groupproject.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class homeViewModel(private val firebaseApi: FirebaseApi) : ViewModel() {
    // LiveData to hold the list of destinations
    val destinations = mutableStateOf<List<Destination>>(emptyList())

    // Function to fetch all destinations
    fun getAllDestinations() {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseApi.getAllDestinations { destinationsList ->
                destinations.value = destinationsList
            }
        }
    }
}