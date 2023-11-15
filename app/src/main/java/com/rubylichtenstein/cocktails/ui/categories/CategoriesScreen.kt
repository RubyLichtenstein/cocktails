@file:OptIn(ExperimentalMaterial3Api::class)

package com.rubylichtenstein.cocktails.ui.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.rubylichtenstein.cocktails.data.DrinkCategory
import com.rubylichtenstein.cocktails.ui.UiState
import com.rubylichtenstein.cocktails.ui.navigateToCocktails
import com.rubylichtenstein.cocktails.ui.search.CocktailsSearchBar
import com.valentinilk.shimmer.shimmer


@Composable
fun CategoriesScreen(
    navController: NavController,
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    val categoriesState = viewModel.categories.collectAsStateWithLifecycle()

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            Column {
                CocktailsSearchBar(
                    navController = navController,
                    searchFavorites = false
                )
                TopAppBar(
                    title = {
                        Text("Categories")
                    },
                    scrollBehavior = scrollBehavior,
                )
            }
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            when (val state = categoriesState.value) {
                is UiState.Loading -> LoadingView()
                is UiState.Success -> Column {
                    CategoriesList(
                        categories = state.data,
                        navController = navController
                    )
                }

                is UiState.Error -> ErrorView(
                    errorMsg = "Sorry we cant show you content, please retry :)",//state.message ?: "Error",
                    onRetry = { viewModel.fetchCocktailCategories() }
                )

                is UiState.Empty -> TODO()
            }
        }
    }
}

@Composable
fun CategoriesList(categories: List<DrinkCategory>, navController: NavController) {
    LazyColumn {
        items(categories) { category ->
            ListItem(
                headlineContent = { Text(category.strCategory) },
                modifier = Modifier.clickable {
                    navController.navigateToCocktails(category.strCategory)
                }
            )
        }
    }
}

@Composable
fun ErrorView(errorMsg: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = errorMsg, color = Color.Red)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
fun LoadingView() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(3) {
            ListItem(
                headlineContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .shimmer()
                            .background(Color.Gray)
                    )
                },
            )
        }
    }
}
