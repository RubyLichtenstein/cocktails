package com.rubylichtenstein.cocktails.ui.cocktails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.rubylichtenstein.cocktails.data.model.Cocktail
import com.rubylichtenstein.cocktails.ui.UiState
import com.rubylichtenstein.cocktails.ui.navigateToDetails
import com.valentinilk.shimmer.shimmer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailsScreen(
    category: String,
    navController: NavController,
    viewModel: CocktailViewModel = hiltViewModel()
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            LargeTopAppBar(
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
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        LaunchedEffect(category) {
            viewModel.fetchCocktailsByCategory(category)
        }

        Box(modifier = Modifier.padding(paddingValues)) {
            when (val cocktailsState = viewModel.cocktailsByCategory.collectAsState().value) {
                is UiState.Loading -> LoadingView()
                is UiState.Success -> CocktailList(cocktailsState.data, navController)
                is UiState.Error -> ErrorView(cocktailsState.message)
            }
        }
    }
}

@Composable
fun CocktailList(cocktails: List<Cocktail>, navController: NavController) {
    LazyColumn {
        items(cocktails) { cocktail ->
            CocktailItem(cocktail, navController)
        }
    }
}

@Composable
fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorView(errorMessage: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Error: $errorMessage")
    }
}

@Composable
fun CocktailItem(cocktail: Cocktail, navController: NavController) {
    ListItem(
        modifier = Modifier.clickable {
            navController.navigateToDetails(cocktail.idDrink)
        },
        headlineContent = {
            Text(
                text = cocktail.strDrink,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
        },
        leadingContent = {
            Box(
                Modifier
                    .size(56.dp)
                    .clip(MaterialTheme.shapes.small),
            ) {
                SubcomposeAsyncImage(
                    modifier = Modifier.size(56.dp),
                    model = cocktail.strDrinkThumb,
                    contentDescription = cocktail.strDrink,
                    loading = {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(Color.Blue)
                                .shimmer(),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(Color.Red)
                            )
                        }
                    }
                )
            }
        },
        trailingContent = {
            IconButton(onClick = { /* Handle add to favorites */ }) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Back",
                )
            }
        }
    )
}
