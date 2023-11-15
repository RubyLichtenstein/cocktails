@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.rubylichtenstein.cocktails.ui.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.rubylichtenstein.cocktails.ui.cocktails.CocktailList
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
            CocktailsSearchBar(
                navController = navController,
                searchFavorites = true
            )
            MediumTopAppBar(
                title = { Text("Favorites") },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { it ->
        Box(modifier = Modifier.padding(it)) {
            CocktailList(
                cocktails = favorites,
                {
                    viewModel.updateFavoriteStatus(it)
                },
                navController = navController
            )
//            when (val state = favorites.value) {
//                is UiState.Loading -> LoadingView()
//                is UiState.Success -> CategoriesList(
//                    categories = state.data,
//                    navController = navController
//                )
//
//                is UiState.Error -> ErrorView(errorMsg = state.message)
//            }
        }
    }
}