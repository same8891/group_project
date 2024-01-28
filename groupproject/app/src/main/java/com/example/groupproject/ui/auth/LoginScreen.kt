package com.example.groupproject.ui.auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.groupproject.R
import com.example.groupproject.data.model.User
import com.example.groupproject.theme.groupProjectTheme
@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel) {
    // State for email and password input fields
    var email by remember { mutableStateOf("test@gmail.com") }
    var password by remember { mutableStateOf("test") }

    // State for showing loading or error messages
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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

// Login button
        Button(
            onClick = {
                isLoading = true
                authViewModel.loginUser(email, password) { user ->
                    isLoading = false
                    if (user != null) {
                        // Check if the entered password matches the stored password
                        if (user.password == password) {
                            // Save user information to SharedPreferences
                            val sharedPref: SharedPreferences = navController.context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
                            with(sharedPref.edit()) {
                                putString("userEmail", user.email)
                                apply()
                                Log.d("HomeTest", "homeScreen: userEmail is ${user.email}")
                            }
                            // Navigate to the next screen
                            navController.navigate("home")
                        } else {
                            errorMessage = "Login failed. Please check your credentials."
                        }
                    } else {
                        // Login failed, show error message
                        errorMessage = "Login failed. Please check your credentials."
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Login")
        }

        // Display error message if login fails
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Navigation to RegisterScreen
        TextButton(
            onClick = {
                navController.navigate("register")
            }
        ) {
            Text("Don't have an account? Register here.")
        }

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