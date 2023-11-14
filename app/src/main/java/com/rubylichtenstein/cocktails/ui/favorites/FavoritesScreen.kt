@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.rubylichtenstein.cocktails.ui.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.rubylichtenstein.cocktails.ui.cocktails.CocktailList

@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val favorites = viewModel.favoriteCocktails.collectAsStateWithLifecycle()

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("Favorites") },
                scrollBehavior = scrollBehavior,
            )
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            CocktailList(
                cocktails = favorites.value,
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