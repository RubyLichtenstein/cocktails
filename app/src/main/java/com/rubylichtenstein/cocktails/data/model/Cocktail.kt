package com.rubylichtenstein.cocktails.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class CocktailsResponse(
    val drinks: List<Cocktail>? = null
)

@Entity(tableName = "favorite_cocktails")
data class Cocktail(
    @PrimaryKey val idDrink: String,
    val strDrink: String,
    val strDrinkThumb: String,
    val isFavorite: Boolean = false
)