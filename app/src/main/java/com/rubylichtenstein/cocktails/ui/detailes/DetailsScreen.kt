@file:OptIn(ExperimentalMaterial3Api::class)

package com.rubylichtenstein.cocktails.ui.detailes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.rubylichtenstein.cocktails.ui.UiState
import com.rubylichtenstein.cocktails.ui.cocktails.CocktailViewModel
import com.rubylichtenstein.cocktails.data.CocktailDetail

@Composable
fun DetailsScreen(
    cocktailId: String,
    navController: NavController,
    viewModel: CocktailViewModel = hiltViewModel()
) {
    // Observe the cocktail details state
    val cocktailDetails = viewModel.cocktailDetails.collectAsState().value

    // Fetch cocktail details
    LaunchedEffect(cocktailId) {
        viewModel.fetchCocktailDetails(cocktailId)
    }

    when (cocktailDetails) {
        is UiState.Loading -> LoadingView()
        is UiState.Success -> CocktailDetailView(cocktailDetails.data, navController)
        is UiState.Error -> ErrorView(cocktailDetails.message)
    }
}


@Composable
fun CocktailDetailView(cocktail: CocktailDetail, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(cocktail.strDrink) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            // Image
            Image(
                painter = rememberImagePainter(cocktail.strDrinkThumb),
                contentDescription = "Cocktail Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Cocktail Information
            Text(text = "Name: ${cocktail.strDrink}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "ID: ${cocktail.idDrink}", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Category: ${cocktail.strCategory}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Type: ${cocktail.strAlcoholic}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(text = "Glass: ${cocktail.strGlass}", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Instructions: ${cocktail.strInstructions}",
                style = MaterialTheme.typography.bodyMedium
            )

            // Ingredients and Measures
            cocktail.ingredients().forEach { ingredient ->
                Text(text = ingredient, style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { /* Handle add to favorites */ }) {
                Text("Add to Favorites")
            }
        }
    }
}

@Composable
fun LoadingView() {
    // Loading view implementation
}

@Composable
fun ErrorView(errorMessage: String) {
    // Error view implementation
}

@Composable
fun CocktailDetailView(cocktail: CocktailDetail) {
    Column(modifier = Modifier.padding(16.dp)) {
        // Image
        Image(
            painter = rememberImagePainter(cocktail.strDrinkThumb),
            contentDescription = "Cocktail Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Cocktail Information
        Text(text = "Name: ${cocktail.strDrink}")
        Text(text = "ID: ${cocktail.idDrink}")
        Text(text = "Category: ${cocktail.strCategory}")
        Text(text = "Type: ${cocktail.strAlcoholic}")
        Text(text = "Glass: ${cocktail.strGlass}")
        Text(text = "Instructions: ${cocktail.strInstructions}")

        // Ingredients and Measures
        cocktail.ingredients().forEach { ingredient ->
            Text(text = ingredient)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* Handle add to favorites */ }) {
            Text("Add to Favorites")
        }
    }
}

/**
 * Extracts and pairs ingredients with their measures.
 */
private fun CocktailDetail.ingredients(): List<String> {
    val ingredients = mutableListOf<String>()
//    listOfNotNull(
//        cocktail.strIngredient1 to cocktail.strMeasure1,
//        cocktail.strIngredient2 to cocktail.strMeasure2,
//        cocktail.strIngredient3 to cocktail.strMeasure3,
//        // Continue for all ingredients and measures
//    ).forEach { (ingredient, measure) ->
//        if (!ingredient.isNullOrBlank()) {
//            ingredients.add("$ingredient: $measure")
//        }
//    }
    return ingredients
}

