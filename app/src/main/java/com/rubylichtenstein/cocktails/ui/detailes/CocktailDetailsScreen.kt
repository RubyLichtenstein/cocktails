@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.rubylichtenstein.cocktails.ui.detailes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.rubylichtenstein.cocktails.data.model.Cocktail
import com.rubylichtenstein.cocktails.data.model.CocktailDetails
import com.rubylichtenstein.cocktails.ui.UiState
import com.rubylichtenstein.cocktails.ui.common.ErrorView
import com.rubylichtenstein.cocktails.ui.detailes.CocktailDetailsViewModel.Intents

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

        is UiState.Error ->
            ErrorView(errorMsg = "Error loading cocktail details", onRefresh = {
                viewModel.processIntents(Intents.FetchCocktailDetails(cocktailId))
            })

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
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
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
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Image(cocktail)
            Spacer(modifier = Modifier.height(16.dp))
            CocktailDetails(cocktail)
            Spacer(modifier = Modifier.height(16.dp))
            CocktailIngredients(cocktail)
        }
    }
}

@Composable
private fun Image(cocktail: CocktailDetails) {
    SubcomposeAsyncImage(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth(),
        contentScale = ContentScale.FillWidth,
        model = cocktail.strDrinkThumb,
        contentDescription = cocktail.strDrink,
        loading = {
            DetailsImageLoader()
        }
    )
}

@Composable
fun CocktailDetails(cocktail: CocktailDetails) {
    FlowRow(modifier = Modifier) {
        Chip(icon = Icons.Default.Category, label = cocktail.strCategory)
        Spacer(modifier = Modifier.size(8.dp))
        Chip(icon = Icons.Default.LocalBar, label = cocktail.strAlcoholic)
        Spacer(modifier = Modifier.size(8.dp))
        Chip(icon = Icons.Default.EmojiFoodBeverage, label = cocktail.strGlass)
        Spacer(modifier = Modifier.size(8.dp))
        Chip(icon = Icons.Default.Info, label = cocktail.idDrink)
    }
}

@Composable
fun Chip(icon: ImageVector, label: String) {
    AssistChip(
        onClick = { },
        label = { Text(text = label) },
        leadingIcon = { Icon(imageVector = icon, contentDescription = null) },
    )
}

@Composable
fun CocktailIngredients(cocktail: CocktailDetails) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            "Ingredients",
            style = MaterialTheme.typography.titleLarge,
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
