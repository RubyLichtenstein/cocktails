@file:OptIn(ExperimentalMaterial3Api::class)

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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.rubylichtenstein.cocktails.ui.cocktails.CocktailList
import com.rubylichtenstein.cocktails.ui.search.FavoritesCocktailsSearchBar

@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val favorites by viewModel.favoriteCocktails.collectAsStateWithLifecycle()

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column {
                FavoritesCocktailsSearchBar(navController)
                TopAppBar(
                    title = { Text("Favorites") },
                    scrollBehavior = scrollBehavior,
                )
            }
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            CocktailList(
                cocktails = favorites,
                onToggleFavorite = {
                    viewModel.processIntents(FavoritesViewModel.Intents.UpdateFavoriteStatus(it))
                },
                onRefresh = {
                    viewModel.processIntents(FavoritesViewModel.Intents.FetchFavorites)
                },
                navController = navController,
                emptyMessage = "No favorites yet, add some by clicking the heart icon",
                errorMessage = "Error fetching favorites"
            )
        }
    }
}

@Composable
fun EmptyScreen(text: String) {
    Box(
        modifier = Modifier
            .padding(32.dp)
            .fillMaxSize()
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text,
            textAlign = TextAlign.Center,
        )
    }
}