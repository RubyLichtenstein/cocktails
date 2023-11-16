package com.rubylichtenstein.cocktails.ui.cocktails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    viewModel: CocktailsViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
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
        LaunchedEffect(category) {
            viewModel.fetchCocktailsByCategory(category)
        }

        Box(modifier = Modifier.padding(paddingValues)) {
            when (val cocktailsState =
                viewModel.cocktailsByCategory.collectAsStateWithLifecycle().value) {
                is UiState.Loading -> CocktailsLoadingView()
                is UiState.Success -> CocktailsList(
                    cocktailsState.data,
                    {
                        viewModel.updateFavoriteStatus(it)
                    }, navController
                )

                is UiState.Error -> ErrorView(cocktailsState.message ?: "Error")
                is UiState.Empty -> TODO()
            }
        }
    }
}

@Composable
fun CocktailsList(
    cocktails: List<Cocktail>,
    onToggleFavorite: (Cocktail) -> Unit,
    navController: NavController
) {
    LazyColumn {
        items(
            cocktails,
            key = { cocktail -> cocktail.idDrink }
        ) { cocktail ->
            CocktailItem(
                cocktail, {
                    navController.navigateToDetails(cocktail.idDrink)
                }, {
                    onToggleFavorite(cocktail)
                })
        }
    }
}

@Composable
fun CocktailsLoadingView() {
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

@Composable
fun ErrorView(errorMessage: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Error: $errorMessage")
    }
}

@Composable
fun CocktailItem(
    cocktail: Cocktail,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
) {
    ListItem(
        modifier = Modifier.clickable { onClick() },
        headlineContent = {
            Text(
                text = cocktail.strDrink,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
        },
        leadingContent = {
            val size = 56.dp
            Box(
                Modifier
                    .size(size)
                    .clip(MaterialTheme.shapes.small),
            ) {
                SubcomposeAsyncImage(
                    model = cocktail.strDrinkThumb,
                    contentDescription = cocktail.strDrink,
                    loading = {
                        Box(
                            modifier = Modifier
                                .size(size)
                                .shimmer()
                                .background(Color.Gray),
                            contentAlignment = Alignment.Center
                        ) {

                        }
                    }
                )
            }
        },
        trailingContent = {
            IconButton(onClick = {
                onToggleFavorite()
            }) {
                Icon(
                    imageVector = if (cocktail.isFavorite)
                        Icons.Outlined.Favorite
                    else
                        Icons.Outlined.FavoriteBorder,
                    contentDescription = "Back",
                )
            }
        }
    )
}
