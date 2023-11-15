package com.rubylichtenstein.cocktails.ui

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rubylichtenstein.cocktails.ui.categories.CategoriesScreen
import com.rubylichtenstein.cocktails.ui.cocktails.CocktailsScreen
import com.rubylichtenstein.cocktails.ui.detailes.DetailsScreen
import com.rubylichtenstein.cocktails.ui.favorites.FavoritesScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "categories") {
        composable("categories") {
            CategoriesScreen(navController)
        }

        composable("favorites") {
            FavoritesScreen(navController)
        }

        composable(
            "cocktails/{category}",
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category")?.let { Uri.decode(it) }
                ?: return@composable
            CocktailsScreen(category, navController)
        }

        composable(
            "details/{cocktailId}",
            arguments = listOf(navArgument("cocktailId") { type = NavType.StringType })
        ) { backStackEntry ->
            val cocktailId = backStackEntry.arguments?.getString("cocktailId") ?: return@composable
            DetailsScreen(cocktailId, navController)
        }

    }
}

fun NavController.navigateToCategories() {
    navigate("categories")
}

fun NavController.navigateToDetails(cocktailId: String) {
    navigate("details/$cocktailId")
}

fun NavController.navigateToCocktails(categoryName: String) {
    navigate("cocktails/${Uri.encode(categoryName)}")
}