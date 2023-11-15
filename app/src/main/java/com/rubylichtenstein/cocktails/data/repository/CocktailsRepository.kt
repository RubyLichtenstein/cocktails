package com.rubylichtenstein.cocktails.data.repository

import com.rubylichtenstein.cocktails.data.api.CocktailsApi
import com.rubylichtenstein.cocktails.data.model.Cocktail
import com.rubylichtenstein.cocktails.data.model.CocktailDetails
import com.rubylichtenstein.cocktails.data.room.FavoriteCocktailsDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class CocktailsRepository(
    private val cocktailsApi: CocktailsApi,
    private val favoriteCocktailsDao: FavoriteCocktailsDao
) {

    suspend fun searchCocktails(searchTerm: String): Flow<List<Cocktail>> = flow {
        val cocktails = cocktailsApi.searchCocktails(searchTerm)
        val favoriteIds = favoriteCocktailsDao.getAllFavorites().first().map { it.idDrink }
        val updatedCocktails = cocktails.map { cocktail ->
            cocktail.copy(isFavorite = favoriteIds.contains(cocktail.idDrink))
        }
        emit(updatedCocktails)
    }

    suspend fun getCocktailDetails(id: String): Flow<CocktailDetails> = flow {
        val cocktailDetails = cocktailsApi.getCocktailDetails(id).firstOrNull()
        val isFavorite = favoriteCocktailsDao.isFavorite(id).first()
        cocktailDetails?.let {
            emit(it.copy(isFavorite = isFavorite))
        } ?: throw Exception("Cocktail not found")
    }

    suspend fun updateFavoriteStatus(cocktail: Cocktail, isFavorite: Boolean) {
        if (isFavorite) {
            favoriteCocktailsDao.addFavorite(cocktail.copy(isFavorite = true))
        } else {
            favoriteCocktailsDao.removeFavorite(cocktail)
        }
    }

    fun getCocktailsByCategory(category: String): Flow<List<Cocktail>> =
        flow { emit(cocktailsApi.getCocktailsByCategory(category)) }
            .combine(favoriteCocktailsDao.getAllFavorites()) { cocktails, favorites ->
                val favoriteIds = favorites.map { it.idDrink }
                cocktails.map { cocktail ->
                    cocktail.copy(isFavorite = favoriteIds.contains(cocktail.idDrink))
                }
            }

    fun getFavoriteCocktails(): Flow<List<Cocktail>> = favoriteCocktailsDao.getAllFavorites()
}