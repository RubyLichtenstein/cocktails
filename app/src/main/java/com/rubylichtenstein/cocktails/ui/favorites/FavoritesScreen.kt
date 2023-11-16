@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.rubylichtenstein.cocktails.ui.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.rubylichtenstein.cocktails.ui.UiState
import com.rubylichtenstein.cocktails.ui.cocktails.CocktailsList
import com.rubylichtenstein.cocktails.ui.cocktails.CocktailsLoadingView
import com.rubylichtenstein.cocktails.ui.cocktails.CocktailsViewModel
import com.rubylichtenstein.cocktails.ui.search.CocktailsSearchBar

@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: CocktailsViewModel = hiltViewModel()
) {
    val favorites by viewModel.favoriteCocktails.collectAsStateWithLifecycle()

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            Column {
                CocktailsSearchBar(
                    navController = navController,
                    searchFavorites = true
                )
                TopAppBar(
                    title = { Text("Favorites") },
                    scrollBehavior = scrollBehavior,
                )
            }
        }
    ) { it ->
        Box(modifier = Modifier.padding(it)) {
            when (val state = favorites) {
                is UiState.Loading -> CocktailsLoadingView()
                is UiState.Success -> {
                    CocktailsList(
                        cocktails = state.data,
                        {
                            viewModel.updateFavoriteStatus(it)
                        },
                        navController = navController
                    )
                }

                is UiState.Empty -> EmptyFavoritesScreen()
                is UiState.Error -> TODO()
            }
        }
    }
}

@Composable
fun EmptyFavoritesScreen() {
    Box(
        modifier = Modifier
            .padding(32.dp)
            .fillMaxSize()
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "No favorites yet, add some by clicking the heart icon",
            textAlign = TextAlign.Center,
        )
    }
}