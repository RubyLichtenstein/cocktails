@file:OptIn(ExperimentalMaterial3Api::class)

package com.rubylichtenstein.cocktails.ui.search

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.rubylichtenstein.cocktails.ui.cocktails.CocktailList

@Composable
fun FavoritesCocktailsSearchBar(
    navController: NavController,
) {
    val viewModel: SearchFavoriteCocktailsViewModel = hiltViewModel()
    CocktailsSearchBar(
        navController = navController,
        viewModel = viewModel,
        placeHolderText = "Search Favorites"
    )
}

@Composable
fun AllCocktailsSearchBar(
    navController: NavController,
) {
    val viewModel: SearchApiCocktailsViewModel = hiltViewModel()
    CocktailsSearchBar(
        navController = navController,
        viewModel = viewModel,
        placeHolderText = "Search Cocktails"
    )
}

@Composable
fun CocktailsSearchBar(
    navController: NavController,
    viewModel: BaseSearchViewModel,
    placeHolderText: String
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isActive = uiState.isActive
    val searchQuery = uiState.searchQuery

    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                if (isActive) PaddingValues(0.dp)
                else PaddingValues(horizontal = 8.dp, vertical = 8.dp)
            ),
        query = searchQuery,
        onQueryChange = { query ->
            viewModel.processIntent(SearchIntent.SearchQueryChanged(query))
        },
        leadingIcon = {
            if (isActive) {
                IconButton(
                    onClick = {
                        viewModel.processIntent(SearchIntent.SetActive(false))
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
                    viewModel.processIntent(SearchIntent.ClearSearch)
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close icon"
                    )
                }
            }
        },
        onSearch = { query ->
            viewModel.processIntent(SearchIntent.SearchQueryChanged(query))
        },
        active = isActive,
        onActiveChange = {
            viewModel.processIntent(SearchIntent.SetActive(it))
        },
        placeholder = {
            Text(placeHolderText)
        }
    ) {
        CocktailList(
            cocktails = uiState.searchResult,
            onToggleFavorite = { viewModel.processIntent(SearchIntent.ToggleFavorite(it)) },
            navController = navController,
            onRefresh = { viewModel.processIntent(SearchIntent.Refresh) },
            emptyMessage = "No cocktails found",
            errorMessage = "Error searching cocktails"
        )
    }
}
