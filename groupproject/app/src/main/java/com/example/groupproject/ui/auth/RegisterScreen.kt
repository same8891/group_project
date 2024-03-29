package com.example.groupproject.ui.auth

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.groupproject.R
import com.example.groupproject.data.model.Profile
import com.example.groupproject.data.model.User
import com.example.groupproject.ui.util.SvgImage
import kotlinx.coroutines.launch
import java.io.IOException


@Composable
fun RegisterScreen(navController: NavController, authViewModel: AuthViewModel) {
    // State for email, password, and confirm password input fields
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    // State for showing loading or error messages
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    // State for selected currency
    var selectedCurrency by remember { mutableStateOf("USD") }
    // Dropdown menu expanded state
    var expanded by remember { mutableStateOf(false) }
    // Currency list
    val currencies = listOf("USD", "EUR", "CNY", "GPB","JPY")
    // Display name input field
    var displayName by remember { mutableStateOf("") }
    // State for success message
    var isSuccessMessageVisible by remember { mutableStateOf(false) }
    var location by remember { mutableStateOf("Washington, D.C") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo), // Replace with your logo resource
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .padding(bottom = 16.dp)
        )

        // Email input field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = displayName,
            onValueChange = { displayName = it },
            label = { Text("Display Name") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Password input field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )


        // Confirm Password input field
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )



        // Loacation input field
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Current Location") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Label for the dropdown menu
            Text(
                text = "Select Currency: ",
                modifier = Modifier.padding(end = 8.dp)
            )

            Image(bitmap = getCurrencyImage(currency = selectedCurrency),
                contentDescription = selectedCurrency, modifier = Modifier.size(24.dp))

            Text(
                text = selectedCurrency,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable { expanded = true }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                currencies.forEach { currency ->
                    DropdownMenuItem(
                        onClick = {
                            selectedCurrency = currency
                            expanded = false
                        },
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Image(bitmap = getCurrencyImage(currency = currency),
                                    contentDescription = currency, modifier = Modifier.size(24.dp))
                                Text(text = currency)
                            }
                        }
                    )
                }
            }
        }



        // Register button
        // Register button
        Button(
            onClick = {
                isLoading = true
                if (password != confirmPassword) {
                    errorMessage = "Passwords do not match."
                    isLoading = false
                    return@Button
                }

                if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || displayName.isEmpty()) {
                    errorMessage = "Please fill in all the fields."
                    isLoading = false
                    return@Button
                }
                val profile = Profile(location = location, profileId = email)
                val user = User(email = email, password = password, displayName = displayName,
                    currency = selectedCurrency, profile = listOf(profile))
                authViewModel.getUser(email) { fetchedUser ->
                    if (fetchedUser != null) {
                        errorMessage = "User already exists."
                        isLoading = false
                        return@getUser
                    } else {
                        errorMessage = ""
                        authViewModel.registerUser(user) {
                            isLoading = false
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Registration successful, will redirect to login screen in 3 seconds.",
                                    actionLabel = "Dismiss"
                                )
                            }
                            // Navigate to login screen after a delay
                            Handler(Looper.getMainLooper()).postDelayed({
                                navController.navigate("login")
                            }, 3000)
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Register")
        }

        // Navigation to LoginScreen
        TextButton(
            onClick = {
                navController.navigate("login")
            }
        ) {
            Text("Already have an account? Login here.")
        }

        // Snackbar for displaying success message
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.fillMaxWidth(),
        )

        Text(text = errorMessage, color = Color.Red)

        // Loading indicator
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(50.dp)
                    .padding(16.dp)
                    .align(CenterHorizontally)
            )
        }
    }
}

@Composable
fun getCurrencyImage(currency: String): ImageBitmap {
    // Use a function to map currency names to their corresponding resource IDs
    val currencyImageResources = mapOf(
        "USD" to R.raw.usd,
        "EUR" to R.raw.eur,
        "CNY" to R.raw.cny,
        "GPB" to R.raw.gpb,
        "JPY" to R.raw.jpy
    )
    var currencyImage: ImageBitmap? = null
    currencyImage = ImageBitmap.imageResource(LocalContext.current.resources, currencyImageResources[currency]!!)
    return currencyImage
}
