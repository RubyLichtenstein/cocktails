package com.rubylichtenstein.cocktails.data

import com.rubylichtenstein.cocktails.data.model.Cocktail
import com.rubylichtenstein.cocktails.data.model.CocktailDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CocktailRepository(
    private val api: TheCocktailDbService
) {

    suspend fun searchCocktails(searchTerm: String): List<Cocktail> {
        return withContext(Dispatchers.IO) {
            val response = api.searchCocktails(searchTerm)
            if (response.isSuccessful) {
                response.body()?.drinks.orEmpty()
            } else {
                throw Exception("Error fetching cocktails")
            }
        }
    }

    suspend fun getCocktailCategories(): List<DrinkCategory> {
        return withContext(Dispatchers.IO) {
            val response = api.getCocktailCategories()
            if (response.isSuccessful) {
                response.body()?.drinks.orEmpty()
            } else {
                throw Exception("Error fetching cocktail categories")
            }
        }
    }

    suspend fun getCocktailsByCategory(category: String): List<Cocktail> {
        return withContext(Dispatchers.IO) {
            val response = api.getCocktailsByCategory(category)
            if (response.isSuccessful) {
                response.body()?.drinks.orEmpty()
            } else {
                throw Exception("Error fetching cocktails by category")
            }
        }
    }

    suspend fun getCocktailDetails(cocktailId: String): List<CocktailDetails> {
        return withContext(Dispatchers.IO) {
            val response = api.getCocktailDetails(cocktailId)
            if (response.isSuccessful) {
                response.body()?.drinks.orEmpty()
            } else {
                error("Error fetching cocktail details")
            }
        }
    }
}