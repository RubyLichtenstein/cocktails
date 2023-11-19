@file:OptIn(ExperimentalMaterial3Api::class)

package com.rubylichtenstein.cocktails.ui.categories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.rubylichtenstein.cocktails.data.model.Category
import com.rubylichtenstein.cocktails.ui.UiState
import com.rubylichtenstein.cocktails.ui.common.ErrorView
import com.rubylichtenstein.cocktails.ui.navigateToCocktails
import com.rubylichtenstein.cocktails.ui.search.AllCocktailsSearchBar

@Composable
fun CategoriesScreen(
    navController: NavController,
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    val categoriesState = viewModel.categories.collectAsStateWithLifecycle()

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column {
                AllCocktailsSearchBar(
                    navController = navController
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
                    errorMsg = "Error loading categories",
                    onRefresh = { viewModel.fetchCocktailCategories() }
                )

                is UiState.Empty -> Box {
                    Text("No categories found :(")
                }

                UiState.Initial -> Box {}
            }
        }
    }
}

@Composable
fun CategoriesList(categories: List<Category>, navController: NavController) {
    LazyColumn {
        items(categories) { category ->
            Column {
                ListItem(
                    headlineContent = { Text(category.strCategory) },
                    modifier = Modifier.clickable {
                        navController.navigateToCocktails(category.strCategory)
                    }
                )
            }
        }
    }
}
