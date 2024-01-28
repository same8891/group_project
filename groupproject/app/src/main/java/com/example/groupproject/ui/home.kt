package com.example.groupproject.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.groupproject.NavigationBar
import com.example.groupproject.NavigationMain
import com.example.groupproject.data.FirebaseApi
import com.example.groupproject.data.user

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HomeScreen(navController: NavController) {
    var State by remember { mutableStateOf(1) }
    var searchInput by remember { mutableStateOf("") }
    var selectedCheckBoxItems by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedOption by remember { mutableStateOf(SortOrder.ASCENDING_NAME) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Log.d("selectcheck","$selectedCheckBoxItems")
        Log.d("selectoption","$selectedOption")
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
            RadioGroup(selectedOption = selectedOption,
                onOptionSelected = {
                    selectedOption = it
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
    }
    val screenHeight = LocalConfiguration.current.screenHeightDp

    Column (modifier = Modifier
        .fillMaxSize()
        .padding(top = (screenHeight-75).dp)){
        NavigationBar(navController = navController)
    }
}
@Preview(showBackground = true)
@Composable
fun homepre() {
    val navController = rememberNavController()
    var selectedOption by remember { mutableStateOf(SortOrder.ASCENDING_NAME) }
    RadioGroup(SortOrder.ASCENDING_NAME,onOptionSelected = { selectedOption = it })
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(onSearch: (String) -> Unit) {
    var input by remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(MaterialTheme.colorScheme.primary)
            ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Display search icon or close icon based on the filtering state
        IconButton(
            onClick = {
//              onSearchClick.invoke()
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

    Column(
    ) {
        items.forEachIndexed { index, item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = checkedItems[index],
                    onCheckedChange = { checkedItems[index] = it },
                    modifier = Modifier
                        .padding(end = 8.dp)
                )
                Text(text = item)
            }
        }
        Button(
            onClick = { getSelectedItems(items,checkedItems) },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Filter")
        }
    }
}


fun getSelectedItems(items: List<String>, checkedItems: List<Boolean>): List<String> {
    return items.filterIndexed { index, _ -> checkedItems[index] }
}
enum class SortOrder {
    ASCENDING_NAME,
    DESCENDING_NAME,
    FAVORITES,
    RATING
}
@Composable
fun RadioGroup(
    selectedOption: SortOrder,
    onOptionSelected: (SortOrder) -> Unit
) {
    val options = SortOrder.values()
    val choices = listOf(
        SortOrder.ASCENDING_NAME,
        SortOrder.DESCENDING_NAME,
        SortOrder.FAVORITES,
        SortOrder.RATING
    )
    Column (){
        choices.forEach { choice ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onOptionSelected(choice)
                    }
            ) {
                RadioButton(
                    selected = choice == selectedOption,
                    onClick = {
                        onOptionSelected(choice)
                    }
                )
                Text(text = when (choice) {
                    SortOrder.ASCENDING_NAME -> "Ascending Order by Name"
                    SortOrder.DESCENDING_NAME -> "Descending Order by Name"
                    SortOrder.FAVORITES -> "Number of Favorites"
                    SortOrder.RATING -> "Rating"
                })
            }
        }
    }
}