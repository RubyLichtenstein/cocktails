package com.rubylichtenstein.cocktails.data.api

import com.rubylichtenstein.cocktails.data.model.CocktailDetailResponse
import com.rubylichtenstein.cocktails.data.model.CocktailsResponse
import com.rubylichtenstein.cocktails.data.model.CategoriesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TheCocktailDbService {

    @GET("search.php")
    suspend fun searchCocktails(@Query("s") query: String): Response<CocktailsResponse>

    @GET("list.php?c=list")
    suspend fun getCocktailCategories(): Response<CategoriesResponse>

    @GET("filter.php")
    suspend fun getCocktailsByCategory(@Query("c") category: String): Response<CocktailsResponse>

    @GET("lookup.php")
    suspend fun getCocktailDetails(@Query("i") cocktailId: String): Response<CocktailDetailResponse>
}