package com.example.groupproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.groupproject.data.FirebaseApi
import com.example.groupproject.theme.groupProjectTheme
import com.example.groupproject.ui.auth.AuthViewModel
import com.example.groupproject.ui.auth.LoginScreen
import com.example.groupproject.ui.auth.RegisterScreen
import com.example.groupproject.ui.home.homeScreen
import com.example.groupproject.ui.home.homeViewModel
import com.example.groupproject.ui.profile.profileScreen
import com.example.groupproject.ui.profile.profileViewModel
import com.example.groupproject.ui.setting.settingScreen
import com.example.groupproject.ui.setting.settingViewModel
import com.example.groupproject.ui.trips.tripsScreen
import com.example.groupproject.ui.trips.tripsViewModel

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val firebaseApi = FirebaseApi()
            val navController = rememberNavController()
            val homeViewModel = homeViewModel(firebaseApi)
            val tripsViewModel = tripsViewModel()
            val profileViewModel = profileViewModel()
            val settingsViewModel = settingViewModel()
            val authViewModel = AuthViewModel(firebaseApi)

            groupProjectTheme {
                NavHost(navController, startDestination = "login") {
                    composable("home") {
                        homeScreen(navController, homeViewModel)
                        val items = listOf(
                            BottomNavigationItem(
                                title = "Home",
                                selectedIcon = Icons.Outlined.Home,
                                unselectedIcon = Icons.Filled.Home,
                                hasNews = false
                            ),
                            BottomNavigationItem(
                                title = "Trips",
                                selectedIcon = Icons.Outlined.List,
                                unselectedIcon = Icons.Filled.List,
                                hasNews = false
                            ),
                            BottomNavigationItem(
                                title = "Profile",
                                selectedIcon = Icons.Outlined.Person,
                                unselectedIcon = Icons.Filled.Person,
                                hasNews = false
                            ),
                            BottomNavigationItem(
                                title = "Settings",
                                selectedIcon = Icons.Outlined.Settings,
                                unselectedIcon = Icons.Filled.Settings,
                                hasNews = false,
                                // badgeCount = 2
                            )
                        )
                        var selectedItemIndex by rememberSaveable {
                            mutableStateOf(0)
                        }
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            Scaffold(
                                bottomBar = {
                                    NavigationBar {
                                        items.forEachIndexed { index, item ->
                                            NavigationBarItem(
                                                selected = selectedItemIndex == index,
                                                onClick = {
                                                    selectedItemIndex = index
                                                    // navController.navigate(item.title)
                                                },
                                                label = {
                                                    Text(text = item.title)
                                                },
                                                alwaysShowLabel = false,
                                                icon = {
                                                    BadgedBox(
                                                        badge = {
                                                            if (item.badgeCount != null) {
                                                                Badge {
                                                                    Text(text = item.badgeCount.toString())
                                                                }
                                                            } else if (item.hasNews) {
                                                                Badge()
                                                            }
                                                        }
                                                    ) {
                                                        Icon(
                                                            imageVector = if (index == selectedItemIndex) {
                                                                item.selectedIcon
                                                            } else item.unselectedIcon,
                                                            contentDescription = item.title
                                                        )
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                            ) {
                                Column(Modifier.padding(it)) {
                                    when (selectedItemIndex) {
                                        0 -> homeScreen(navController, homeViewModel)
                                        1 -> tripsScreen(navController, tripsViewModel)
                                        2 -> profileScreen(navController, profileViewModel)
                                        3 -> settingScreen(navController, settingsViewModel)
                                    }
                                }
                            }
                        }
                    }
                    composable("trips") {
                        tripsScreen(navController, tripsViewModel)
                    }
                    composable("profile") {
                        profileScreen(navController, profileViewModel)
                    }
                    composable("settings") {
                        settingScreen(navController, settingsViewModel)
                    }
                    composable("login") {
                        LoginScreen(navController, authViewModel)
                    }
                    composable("register") {
                        RegisterScreen(navController, authViewModel)
                    }
                }
            }
        }
    }
}


data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)