package com.rubylichtenstein.cocktails.data

import com.rubylichtenstein.cocktails.data.model.Cocktail
import com.rubylichtenstein.cocktails.data.model.CocktailDetails

data class DrinksResponse(
    val drinks: List<DrinkCategory>
)

data class DrinkCategory(
    val strCategory: String
)

data class CocktailResponse(
    val drinks: List<Cocktail>
)

data class CocktailDetailResponse(
    val drinks: List<CocktailDetails>
)

