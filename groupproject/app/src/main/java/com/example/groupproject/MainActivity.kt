package com.example.groupproject

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.FirebaseApp
data class user(val account:String, val passwsord: String)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Firebase.firestore
        var u1 = user("Bernie","000")
        db.collection("users").document(u1.account).set(u1).addOnSuccessListener {
            // User information added to the database successfully
            Log.d("error", "success")
        }
            .addOnFailureListener { e ->
                Log.d("error", "adderror")
            }
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
//

            }

        }
    }
}