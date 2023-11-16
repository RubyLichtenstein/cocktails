@file:OptIn(ExperimentalMaterial3Api::class)

package com.rubylichtenstein.cocktails.ui.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.rubylichtenstein.cocktails.ui.UiState
import com.rubylichtenstein.cocktails.ui.cocktails.CocktailsList

@Composable
fun CocktailsSearchBar(
    navController: NavController,
    searchFavorites: Boolean,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
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
            viewModel.processIntent(SearchIntent.SearchQueryChanged(query, searchFavorites))
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
            viewModel.processIntent(SearchIntent.SearchQueryChanged(query, searchFavorites))
        },
        active = isActive,
        onActiveChange = {
            viewModel.processIntent(SearchIntent.SetActive(it))
        },
        placeholder = {
            if (searchFavorites) {
                Text("Search Favorites")
            } else {
                Text("Search Cocktails")
            }
        }
    ) {
        when (val result = uiState.searchResult) {
            is UiState.Loading -> Box(Modifier.fillMaxWidth()) {
                if (searchQuery.isEmpty()) {
                } else {
                    LinearProgressIndicator(Modifier.fillMaxWidth())
                }
            }

            is UiState.Success -> CocktailsList(
                result.data, {
                    viewModel.processIntent(SearchIntent.ToggleFavorite(it))
                },
                navController
            )

            is UiState.Error -> Column {
                Text("Error searching cocktails")
                Button(onClick = { viewModel.processIntent(SearchIntent.Refresh) }) {
                    Text("Refresh")
                }
            }

            is UiState.Empty -> Text(text = "Not found")
        }
    }
}
