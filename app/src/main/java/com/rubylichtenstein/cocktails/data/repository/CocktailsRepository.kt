package com.rubylichtenstein.cocktails.data.repository

import com.rubylichtenstein.cocktails.data.api.CocktailsApi
import com.rubylichtenstein.cocktails.data.model.Cocktail
import com.rubylichtenstein.cocktails.data.model.CocktailDetails
import com.rubylichtenstein.cocktails.data.room.CocktailDetailsDao
import com.rubylichtenstein.cocktails.data.room.FavoriteCocktailsDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class CocktailsRepository(
    private val cocktailsApi: CocktailsApi,
    private val favoriteCocktailsDao: FavoriteCocktailsDao,
    private val cocktailDetailsDao: CocktailDetailsDao
) {

    suspend fun searchCocktails(searchTerm: String): Flow<List<Cocktail>> =
        flow { emit(cocktailsApi.searchCocktails(searchTerm)) }
            .combine(favoriteCocktailsDao.getAllFavorites()) { cocktails, favorites ->
                val favoriteIds = favorites.map { it.idDrink }.toSet()
                cocktails.map { cocktail ->
                    val isFavorite = favoriteIds.contains(cocktail.idDrink)
                    cocktail.copy(isFavorite = isFavorite)
                }
            }

    fun getCocktailDetails(id: String): Flow<CocktailDetails> = flow {
        val localData = cocktailDetailsDao.getCocktailById(id).firstOrNull()

        if (localData == null) {
            fetchAndSaveCocktailDetails(id)
        }

        cocktailDetailsDao.getCocktailById(id).collect { cocktailDetails ->
            if (cocktailDetails != null) {
                emit(cocktailDetails)
            }
        }
    }


    private suspend fun fetchAndSaveCocktailDetails(id: String) {
        val cocktailDetails = cocktailsApi.getCocktailDetails(id).firstOrNull()
            ?: error("Cocktail not found")
        val isFavorite = favoriteCocktailsDao.isFavorite(id).firstOrNull() ?: false
        val copy = cocktailDetails.copy(isFavorite = isFavorite)
        cocktailDetailsDao.insertAll(copy)
    }

    suspend fun updateFavoriteStatus(cocktail: Cocktail, isFavorite: Boolean) {
        cocktailDetailsDao.updateFavoriteStatus(cocktail.idDrink, isFavorite)
        if (isFavorite) {
            favoriteCocktailsDao.addFavorite(cocktail.copy(isFavorite = true))
        } else {
            favoriteCocktailsDao.removeFavorite(cocktail)
        }
    }

    fun getCocktailsByCategory(category: String): Flow<List<Cocktail>> =
        flow { emit(cocktailsApi.getCocktailsByCategory(category)) }
            .combine(favoriteCocktailsDao.getAllFavorites()) { cocktails, favorites ->
                val favoriteIds = favorites.map { it.idDrink }.toSet()
                cocktails.map { cocktail ->
                    cocktail.copy(isFavorite = favoriteIds.contains(cocktail.idDrink))
                }
            }

    fun getFavoriteCocktails(): Flow<List<Cocktail>> = favoriteCocktailsDao.getAllFavorites()
}