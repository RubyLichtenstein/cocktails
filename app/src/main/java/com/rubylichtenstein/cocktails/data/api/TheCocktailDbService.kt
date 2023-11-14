package com.rubylichtenstein.cocktails.data.api

import com.rubylichtenstein.cocktails.data.CocktailDetailResponse
import com.rubylichtenstein.cocktails.data.CocktailResponse
import com.rubylichtenstein.cocktails.data.DrinksResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TheCocktailDbService {

    @GET("search.php")
    suspend fun searchCocktails(@Query("s") query: String): Response<CocktailResponse>

    @GET("list.php?c=list")
    suspend fun getCocktailCategories(): Response<DrinksResponse>

    @GET("filter.php")
    suspend fun getCocktailsByCategory(@Query("c") category: String): Response<CocktailResponse>

    @GET("lookup.php")
    suspend fun getCocktailDetails(@Query("i") cocktailId: String): Response<CocktailDetailResponse>
}