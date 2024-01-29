package com.example.groupproject.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.groupproject.data.model.Destination
import com.example.groupproject.data.model.User
import java.util.jar.Attributes.Name

@Composable
fun HomeScreen(navController: NavHostController, homeViewModel: homeViewModel) {
    // Fetch all destinations when the screen is created
    homeViewModel.getAllDestinations()

    // Observe the destinations LiveData
    val destinations = homeViewModel.destinations.value

    // Display the list of destinations
    LazyColumn {
        items(destinations) { destination ->
            DestinationItem(destination = destination)
        }
    }
}

@Composable
fun DestinationItem(destination: Destination) {
    // Display each destination item
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Name: ${destination.name}")
        Text(text = "Location: ${destination.location}")
        Text(text = "Description: ${destination.description}")
        Text(text = "Reviews: ${destination.reviews.toString()}")
    }
}
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun homeScreen(navController: NavController,homeViewModel: homeViewModel) {
    var State by remember { mutableStateOf(1) }
    var searchInput by remember { mutableStateOf("") }
    var selectedCheckBoxItems by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedOption1 by remember { mutableStateOf(SortOrder.ASCENDING_NAME) }
    var selectedOption2 by remember { mutableStateOf(SortOrder.Empty) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Log.d("selectcheck","$selectedCheckBoxItems")
        Log.d("selectoption","$selectedOption1")
        Log.d("selectoption","$selectedOption2")
        if (State == 1) {
            SearchBar{input->
                searchInput = input
            }
        }
        else if (State == 2){
            CheckBoxList{ items ->
                selectedCheckBoxItems = items
            }
        }
        else{
            RadioGroup(selectedOption1 = selectedOption1,
                selectedOption2 = selectedOption2,
                onOptionSelected = { s1,s2 ->
                    selectedOption1 = s1
                    selectedOption2 = s2
                })
        }
        // Tab with buttons
        TabRow(
            selectedTabIndex = State - 1,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Tab(
                selected = State == 1,
                onClick = { State = 1 }
            ) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            }
            Tab(
                selected = State == 2,
                onClick = { State = 2 }
            ) {
                Icon(imageVector = Icons.Default.FilterList, contentDescription = "Filter")
            }
            Tab(
                selected = State == 3,
                onClick = { State = 3 }
            ) {
                Icon(imageVector = Icons.Default.Sort, contentDescription = "Sort")
            }
        }
        val destinationName = "City of Lights"
        Button(onClick = { navController.navigate("destinationDetail/$destinationName") }) {
            Text(text = "enter destinationDetail/City of Lights test")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun homepre() {
    val navController = rememberNavController()
    var selectedOption1 by remember { mutableStateOf(SortOrder.ASCENDING_NAME) }
    var selectedOption2 by remember { mutableStateOf(SortOrder.Empty) }
//    RadioGroup(SortOrder.ASCENDING_NAME,SortOrder.Empty,onOptionSelected = {s1,s2-> })
    CheckBoxList{}
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(onSearch: (String) -> Unit) {
    var input by remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp)
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                onSearch.invoke(input)
            }
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        }
        TextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Search") },
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}
@Composable
fun CheckBoxList(onFilterClick: (List<String>) -> Unit) {
    val items = listOf("Nature", "Historic", "Music", "Culture")
    val checkedItems = remember { mutableStateListOf<Boolean>(false, false, false, false) }
    var add_row = false
    val screenWidth = LocalConfiguration.current.screenWidthDp
    var length = 0
    var count = 0
    Column(modifier = Modifier
        .height(90.dp)
        .width(screenWidth.dp)
    ) {
        Row (modifier = Modifier
            .height(40.dp)
            .width(screenWidth.dp)){
            items.forEachIndexed { index, item ->
                if((length+120) > screenWidth){
                    add_row = true
                }
                if (add_row == false) {
                    Row(
                        modifier = Modifier
                            .width(120.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = checkedItems[index],
                            onCheckedChange = { checkedItems[index] = it },
                        )
                        Text(text = item)
                    }
                    length += 120
                    count++
                }
            }
        }
        if(add_row){
            Row(modifier = Modifier
                .height(40.dp)
                .width(screenWidth.dp)){
                for(i in count until 4){
                    Row(
                        modifier = Modifier
                            .width(120.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = checkedItems[i],
                            onCheckedChange = { checkedItems[i] = it },
                        )
                        Text(text = items[i])
                    }
                }
                Spacer(modifier = Modifier.width((screenWidth-260).dp))
                Button(
                    onClick = { onFilterClick(getSelectedItems(items, checkedItems)) },
                    modifier = Modifier
                        .width(100.dp)
                )
                {
                    Text("Filter")
                }
            }
        }

    }
}


fun getSelectedItems(items: List<String>, checkedItems: List<Boolean>): List<String> {
    return items.filterIndexed { index, _ -> checkedItems[index] }
}
enum class SortOrder {
    ASCENDING_NAME,
    DESCENDING_NAME,
    Likes,
    RATING,
    Empty
}
@Composable
fun RadioGroup(
    selectedOption1: SortOrder,
    selectedOption2: SortOrder,
    onOptionSelected: (SortOrder, SortOrder) -> Unit
) {
    var currentSortOrder1 = selectedOption1
    var currentSortOrder2 = selectedOption2
    val options = SortOrder.values()
    val choices = listOf(
        SortOrder.Likes,
        SortOrder.RATING
    )
    val screenWidth = LocalConfiguration.current.screenWidthDp
    Column (modifier = Modifier
        .height(90.dp)
        .width(screenWidth.dp)){
        Row (verticalAlignment = Alignment.CenterVertically){
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .width(120.dp)
                    .padding(start = 8.dp)){
                Text(text = "Name")
                Switch(
                    checked = selectedOption1 == SortOrder.ASCENDING_NAME,
                    onCheckedChange = {
                        onOptionSelected(if (it) SortOrder.ASCENDING_NAME else SortOrder.DESCENDING_NAME,currentSortOrder2)
                    },
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            choices.forEach { choice ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .width(100.dp)
                        .clickable {
                            onOptionSelected(currentSortOrder1, choice)
                        }
                ) {
                    RadioButton(
                        selected = choice == selectedOption2,
                        onClick = {
                            onOptionSelected(currentSortOrder1,choice)
                        }
                    )
                    Text(
                        text = when (choice) {
                            SortOrder.Likes -> "Likes"
                            SortOrder.RATING -> "Rating"
                            else -> ""
                        }
                    )
                }
            }
        }
        Button(
            onClick = { onOptionSelected(SortOrder.ASCENDING_NAME,SortOrder.Empty) },
            modifier = Modifier
                .width(100.dp)
                .align(Alignment.End)
        ) {
            Text("Clear")
        }
    }
}
//@Composable
//fun CardItem(cardItem: CardItem) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp)
//    ) {
//        // Image
//        Image(
//            painter = painterResource(id = cardItem.imageRes),
//            contentDescription = null,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(200.dp)
//                .clip(Shapes.),
//            contentScale = ContentScale.Crop
//        )
//
//        // Title
//        Spacer(modifier = Modifier.height(8.dp))
//        Text(
//            text = cardItem.title,
//            style = MaterialTheme.typography.bodyLarge
//        )
//
//        // Rating
//        Spacer(modifier = Modifier.height(4.dp))
//        Row(
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Icon(imageVector = Icons.Default.Star, contentDescription = null)
//            Spacer(modifier = Modifier.width(4.dp))
//            Text(text = cardItem.rating.toString())
//        }
//
//        // Location
//        Spacer(modifier = Modifier.height(4.dp))
//        Row(
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null)
//            Spacer(modifier = Modifier.width(4.dp))
//            Text(text = cardItem.location)
//        }
//
//        // Tags
//        Spacer(modifier = Modifier.height(8.dp))
//        LazyColumn {
//            items(cardItem.tags) { tag ->
//                Row(
//                    modifier = Modifier
//                        .background(MaterialTheme.colorScheme.primary)
//                        .padding(8.dp)
//                        .clip(MaterialTheme.shapes.small),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(imageVector = Icons.Default.Tag, contentDescription = null)
//                    Spacer(modifier = Modifier.width(4.dp))
//                    Text(text = tag, color = Color.White)
//                }
//            }
//        }
//    }
//}