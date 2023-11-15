package com.rubylichtenstein.cocktails.data.api

import com.rubylichtenstein.cocktails.data.DrinkCategory
import com.rubylichtenstein.cocktails.data.model.Cocktail
import com.rubylichtenstein.cocktails.data.model.CocktailDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CocktailsApi(
    private val service: TheCocktailDbService
) {

    suspend fun searchCocktails(searchTerm: String): List<Cocktail> {
        return withContext(Dispatchers.IO) {
            val response = service.searchCocktails(searchTerm)
            if (response.isSuccessful) {
                response.body()?.drinks.orEmpty()
            } else {
                throw Exception("Error fetching cocktails")
            }
        }
    }

    suspend fun getCocktailCategories(): List<DrinkCategory> {
        return withContext(Dispatchers.IO) {
            val response = service.getCocktailCategories()
            if (response.isSuccessful) {
                response.body()?.drinks.orEmpty()
            } else {
                throw Exception("Error fetching cocktail categories")
            }
        }
    }

    suspend fun getCocktailsByCategory(category: String): List<Cocktail> {
        return withContext(Dispatchers.IO) {
            val response = service.getCocktailsByCategory(category)
            if (response.isSuccessful) {
                response.body()?.drinks.orEmpty()
            } else {
                throw Exception("Error fetching cocktails by category")
            }
        }
    }

    suspend fun getCocktailDetails(cocktailId: String): List<CocktailDetails> {
        return withContext(Dispatchers.IO) {
            val response = service.getCocktailDetails(cocktailId)
            if (response.isSuccessful) {
                response.body()?.drinks.orEmpty()
            } else {
                error("Error fetching cocktail details")
            }
        }
    }
}