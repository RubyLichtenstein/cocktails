@file:OptIn(ExperimentalMaterial3Api::class)

package com.rubylichtenstein.cocktails.ui.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.rubylichtenstein.cocktails.ui.UiState
import com.rubylichtenstein.cocktails.ui.cocktails.CocktailsList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CocktailsSearchBar(
    navController: NavController,
    searchFavorites: Boolean,
    viewModel: SearchViewModel = hiltViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var searchJob: Job? by remember { mutableStateOf(null) }

    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                if (isActive) PaddingValues(0.dp)
                else PaddingValues(horizontal = 8.dp, vertical = 8.dp)
            ),
        query = searchQuery,
        onQueryChange = {
            searchQuery = it
            searchJob?.cancel()
            searchJob = coroutineScope.launch {
                delay(500)  // Debounce time in milliseconds
                viewModel.searchCocktails(searchQuery, searchFavorites)
            }
        },
        leadingIcon = {
            if (isActive) {
                //back icon here
                IconButton(
                    onClick = {
                        isActive = false
                        searchQuery = ""
                    }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Close icon"
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search icon"
                )
            }
        },
        trailingIcon = {
            if (isActive && searchQuery.isNotEmpty()) {
                IconButton(onClick = {
                    if (searchQuery.isNotEmpty()) {
                        searchQuery = ""
                    } else {
                        isActive = false
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close icon"
                    )
                }
            }
        },
        onSearch = { viewModel.searchCocktails(it, searchFavorites) },
        active = isActive,
        onActiveChange = { isActive = it },
        placeholder = {
            if (searchFavorites) {
                Text("Search Favorites")
            } else {
                Text("Search Cocktails")
            }
        }
    ) {
        when (val result = viewModel.searchResults.collectAsStateWithLifecycle().value) {
            is UiState.Loading -> Box(Modifier.fillMaxWidth()) {
//                LinearProgressIndicator()
            }

            is UiState.Success -> CocktailsList(
                result.data, {
                    viewModel.updateFavoriteStatus(it)
                }, navController
            )

            is UiState.Error -> Text("Error: ${result.message}")
            is UiState.Empty -> TODO()
        }
    }
}
