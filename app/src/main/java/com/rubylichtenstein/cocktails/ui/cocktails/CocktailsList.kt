package com.rubylichtenstein.cocktails.ui.cocktails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.rubylichtenstein.cocktails.data.model.Cocktail
import com.rubylichtenstein.cocktails.ui.navigateToDetails
import com.valentinilk.shimmer.shimmer

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
fun CocktailItem(
    cocktail: Cocktail,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
) {
    ListItem(
        modifier = Modifier
            .clickable { onClick() }
            .clip(MaterialTheme.shapes.medium),
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
                    model = cocktail.strDrinkThumb + "/preview",
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
