package com.rubylichtenstein.cocktails.ui.cocktails

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailsScreen(
    category: String,
    navController: NavController,
    viewModel: CocktailsViewModel = hiltViewModel()
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    val cocktailsState by viewModel.cocktailsByCategory.collectAsStateWithLifecycle()

    LaunchedEffect(category) {
        viewModel.processIntents(
            CocktailsViewModel.Intents.FetchCocktailsByCategory(
                category
            )
        )
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        text = category,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            CocktailList(
                cocktails = cocktailsState,
                onToggleFavorite = {
                    viewModel.processIntents(CocktailsViewModel.Intents.UpdateFavoriteStatus(it))
                },
                navController = navController,
                onRefresh = {
                    viewModel.processIntents(
                        CocktailsViewModel.Intents.FetchCocktailsByCategory(
                            category
                        )
                    )
                },
                emptyMessage = "No cocktails found for this category",
                errorMessage = "Error fetching cocktails"
            )
        }
    }
}



