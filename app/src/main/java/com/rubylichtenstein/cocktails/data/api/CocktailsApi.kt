package com.rubylichtenstein.cocktails.data.api

import com.rubylichtenstein.cocktails.data.model.Cocktail
import com.rubylichtenstein.cocktails.data.model.CocktailDetails
import com.rubylichtenstein.cocktails.data.model.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class CocktailsApi(
    private val service: TheCocktailDbService
) {

    suspend fun searchCocktails(searchTerm: String): List<Cocktail> =
        performRequest("Error fetching cocktails") {
            service.searchCocktails(searchTerm)
        }.drinks.orEmpty()

    suspend fun getCocktailCategories(): List<Category> =
        performRequest("Error fetching cocktail categories") {
            service.getCocktailCategories()
        }.drinks

    suspend fun getCocktailsByCategory(category: String): List<Cocktail> =
        performRequest("Error fetching cocktails by category") {
            service.getCocktailsByCategory(category)
        }.drinks.orEmpty()

    suspend fun getCocktailDetails(cocktailId: String): List<CocktailDetails> =
        performRequest("Error fetching cocktail details") {
            service.getCocktailDetails(cocktailId)
        }.drinks

    private suspend fun <T> performRequest(
        errorMessage: String,
        request: suspend () -> Response<T>,
    ): T {
        return withContext(Dispatchers.IO) {
            val response = request()
            if (response.isSuccessful) {
                response.body() ?: error("Empty response")
            } else {
                error(errorMessage)
            }
        }
    }
}