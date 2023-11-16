@file:OptIn(ExperimentalMaterial3Api::class)

package com.rubylichtenstein.cocktails.ui.favorites

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rubylichtenstein.cocktails.ui.UiState
import com.rubylichtenstein.cocktails.ui.cocktails.CocktailsViewModel


@Composable
fun FavoriteCountBadge() {
    val favoritesViewModel: CocktailsViewModel = hiltViewModel()
    val favorites = favoritesViewModel.favoriteCocktails.collectAsStateWithLifecycle()
    val value = favorites.value
    val favoriteCount = if (value is UiState.Success) {
        value.data.size
    } else {
        0
    }

    BadgedBox(badge = {
        if (favoriteCount > 0) {
            Badge(
                containerColor = MaterialTheme.colorScheme.onPrimary,
            ) { Text("$favoriteCount") }
        }
    }) {
        Icon(
            Icons.Filled.Favorite,
            contentDescription = "Favorite"
        )
    }
}