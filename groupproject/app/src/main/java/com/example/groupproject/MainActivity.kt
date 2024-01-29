package com.example.groupproject

import android.os.Bundle
import android.util.Log
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.example.groupproject.data.FirebaseApi
import com.example.groupproject.data.model.Destination
import com.example.groupproject.theme.groupProjectTheme
import com.example.groupproject.ui.auth.AuthViewModel
import com.example.groupproject.ui.auth.LoginScreen
import com.example.groupproject.ui.auth.RegisterScreen
import com.example.groupproject.ui.destination.destinationDetailScreen
import com.example.groupproject.ui.destination.destinationDetailViewModel
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
            val tripsViewModel = tripsViewModel(firebaseApi)
            val profileViewModel = profileViewModel(firebaseApi)
            val settingsViewModel = settingViewModel(firebaseApi)
            val authViewModel = AuthViewModel(firebaseApi)
            val destinationDetailViewModel = destinationDetailViewModel(firebaseApi)

            groupProjectTheme {
                NavHost(navController, startDestination = "login") {
                    composable("home") {
                        homeScreen(navController, homeViewModel)
                        BottomNavigationBar(navController, homeViewModel, tripsViewModel,
                            profileViewModel, settingsViewModel, destinationDetailViewModel, "")
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
                    composable("destinationDetail/{destinationName}") { backStackEntry ->
                        val destinationName = backStackEntry.arguments?.getString("destinationName")
                        BottomNavigationBar(navController, homeViewModel, tripsViewModel,
                            profileViewModel, settingsViewModel, destinationDetailViewModel, destinationName!!, currentIndex = 0)
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(navController: NavHostController,
                        homeViewModel: homeViewModel,
                        tripsViewModel: tripsViewModel,
                        profileViewModel: profileViewModel,
                        settingsViewModel: settingViewModel,
                        destinationDetailViewModel: destinationDetailViewModel,
                        destinationName: String,
                        currentIndex: Int = -1) {
    var indexBar = currentIndex
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
        )
    )
    var selectedItemIndex by rememberSaveable {
        if (destinationName != "") {
            mutableIntStateOf(4)
        } else {
            mutableIntStateOf(0)
        }
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    items.forEachIndexed { index, item ->
                        val ifSelected: Boolean
                        if (indexBar != -1){
                            ifSelected = (currentIndex == index)
                            indexBar = -1
                        } else {
                            ifSelected = (selectedItemIndex == index)
                        }
                        NavigationBarItem(
                            selected = ifSelected,
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
                    4 -> destinationDetailScreen(
                        navController = navController,
                        destination = destinationName,
                        destinationDetailViewModel = destinationDetailViewModel
                    )
                }
            }
        }
    }
}
