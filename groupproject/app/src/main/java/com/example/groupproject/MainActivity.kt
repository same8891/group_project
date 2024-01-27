package com.example.groupproject

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.get
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.groupproject.data.FireBaseAPI
import com.example.groupproject.data.user
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val api = FireBaseAPI
//        var u1 = user("kkk","00")
//        var res = api.PutUser(u1)
//        Log.d("api",res)
        Log.d("api","aaaaa")
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
//
                NavigationMain()
            }

        }
    }
}
sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Planning : Screen("planning", "Planning", Icons.Default.Event)
    object Profile : Screen("profile", "Profile", Icons.Default.AccountCircle)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}
val items = listOf(
    Screen.Home,
    Screen.Planning,
    Screen.Profile,
    Screen.Settings
)
@Composable
fun NavigationBar(
    navController: NavController
) {
    BottomNavigation(
        backgroundColor = Color.White,
        modifier = Modifier.background(MaterialTheme.colorScheme.primary)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        var currentRoute = Screen.Home.route
        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(screen.icon, contentDescription = null) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    Log.d("current", currentRoute!!)
                    if(currentRoute != screen.route) {
                        currentRoute = screen.route
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
@Composable
fun NavigationMain() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen()
        }
        composable(Screen.Planning.route) {
            PlanningScreen()
        }
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }

    NavigationBar(navController = navController)
}
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HomeScreen() {
    Log.d("Screen", "home")
    val api: FireBaseAPI = viewModel(factory = FireBaseAPI.Factory)
    var u1 = user("kkk","00")
    var result = ""
    api.viewModelScope.launch {
        result = api.putUser(u1)
        Log.d("api",result)
    }
}


@Composable
fun PlanningScreen() {
    Log.d("Screen", "plan")
}

@Composable
fun ProfileScreen() {
    Log.d("Screen", "profile")
}

@Composable
fun SettingsScreen() {
    Log.d("Screen", "setting")
}
@Preview(showBackground = true)
@Composable
fun NavigationBarPreview() {
    NavigationMain()
}