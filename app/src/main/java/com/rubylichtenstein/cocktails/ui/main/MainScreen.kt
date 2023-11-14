package com.rubylichtenstein.cocktails.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rubylichtenstein.cocktails.ui.AppNavigation
import com.rubylichtenstein.cocktails.ui.favorites.FavoriteCountBadge

@Composable
fun MainAppScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = {
            BottomAppBar {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    NavigationBarItem(
                        icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
                        label = { Text("Categories") },
                        selected = currentRoute == "categories",
                        onClick = {
                            navController.navigate("categories") {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )

                    NavigationBarItem(
                        icon = {
                            FavoriteCountBadge()
                        },
                        label = { Text("Favorites") },
                        selected = currentRoute == "favorites",
                        onClick = {
                            navController.navigate("favorites") {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
    ) {
        Box(Modifier.padding(it)) {
            AppNavigation(navController)
        }
    }
}