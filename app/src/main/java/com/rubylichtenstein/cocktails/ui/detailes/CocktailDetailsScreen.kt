@file:OptIn(ExperimentalMaterial3Api::class)

package com.rubylichtenstein.cocktails.ui.detailes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.EmojiFoodBeverage
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.rubylichtenstein.cocktails.data.model.Cocktail
import com.rubylichtenstein.cocktails.data.model.CocktailDetails
import com.rubylichtenstein.cocktails.ui.UiState
import com.rubylichtenstein.cocktails.ui.detailes.CocktailDetailsViewModel.*
import com.valentinilk.shimmer.shimmer

@Composable
fun CocktailDetailsScreen(
    cocktailId: String,
    navController: NavController,
    viewModel: CocktailDetailsViewModel = hiltViewModel()
) {
    val cocktailDetails = viewModel.cocktailDetails.collectAsStateWithLifecycle().value

    LaunchedEffect(cocktailId) {
        viewModel.processIntents(Intents.FetchCocktailDetails(cocktailId))
    }

    when (cocktailDetails) {
        is UiState.Loading -> CocktailDetailsLoader()
        is UiState.Success -> CocktailDetailView(
            cocktailDetails.data,
            navController,
            onFavoriteClick = {
                val cocktail = Cocktail(
                    idDrink = cocktailDetails.data.idDrink,
                    strDrink = cocktailDetails.data.strDrink,
                    strDrinkThumb = cocktailDetails.data.strDrinkThumb,
                    isFavorite = cocktailDetails.data.isFavorite
                )
                viewModel.processIntents(Intents.UpdateFavoriteStatus(cocktail))
            })

        is UiState.Error -> {
            Box {
                Column(Modifier.align(Alignment.Center)) {
                    Text("Error loading cocktail details")
                    Button(onClick = {
                        viewModel.processIntents(Intents.FetchCocktailDetails(cocktailId))
                    }) {
                        Text("Refresh")
                    }
                }
            }
        }

        is UiState.Empty -> Box { }
        UiState.Initial -> Box { }
    }
}

@Composable
fun CocktailDetailView(
    cocktail: CocktailDetails,
    navController: NavController,
    onFavoriteClick: () -> Unit
) {
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text(cocktail.strDrink) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            if (cocktail.isFavorite)
                                Icons.Filled.Favorite
                            else
                                Icons.Filled.FavoriteBorder,
                            contentDescription = "Add to Favorites"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            SubcomposeAsyncImage(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
                model = cocktail.strDrinkThumb,
                contentDescription = cocktail.strDrink,
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .fillMaxWidth()
                            .shimmer()
                            .background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                    }
                }
            )

            Column(modifier = Modifier) {
                DetailItem(icon = Icons.Default.LocalDrink, label = cocktail.strDrink)
                DetailItem(icon = Icons.Default.Info, label = cocktail.idDrink)
                DetailItem(icon = Icons.Default.Category, label = cocktail.strCategory)
                DetailItem(icon = Icons.Default.LocalBar, label = cocktail.strAlcoholic)
                DetailItem(icon = Icons.Default.EmojiFoodBeverage, label = cocktail.strGlass)
            }

            CocktailIngredients(cocktail)
        }
    }
}

@Composable
fun DetailItem(icon: ImageVector, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            icon, contentDescription = null, modifier = Modifier
                .size(24.dp)
                .padding(end = 8.dp)
        )
        Text(label)
    }
}

@Composable
fun CocktailIngredients(cocktail: CocktailDetails) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            "Ingredients",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        val ingredientsList = listOf(
            cocktail.strIngredient1 to cocktail.strMeasure1,
            cocktail.strIngredient2 to cocktail.strMeasure2,
            cocktail.strIngredient3 to cocktail.strMeasure3,
            cocktail.strIngredient4 to cocktail.strMeasure4,
            cocktail.strIngredient5 to cocktail.strMeasure5,
            cocktail.strIngredient6 to cocktail.strMeasure6,
            cocktail.strIngredient7 to cocktail.strMeasure7,
            cocktail.strIngredient8 to cocktail.strMeasure8,
            cocktail.strIngredient9 to cocktail.strMeasure9,
            cocktail.strIngredient10 to cocktail.strMeasure10,
            cocktail.strIngredient11 to cocktail.strMeasure11,
            cocktail.strIngredient12 to cocktail.strMeasure12,
            cocktail.strIngredient13 to cocktail.strMeasure13,
            cocktail.strIngredient14 to cocktail.strMeasure14,
            cocktail.strIngredient15 to cocktail.strMeasure15

        )

        ingredientsList.forEach { (ingredient, measure) ->
            if (!ingredient.isNullOrEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(ingredient)
                    Text(measure ?: "N/A")
                }
            }
        }
    }
}

@Composable
fun CocktailDetailsLoader() {
    Scaffold {
        Box(
            modifier = Modifier.padding(it),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}