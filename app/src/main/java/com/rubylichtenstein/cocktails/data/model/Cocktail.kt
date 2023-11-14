package com.rubylichtenstein.cocktails.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_cocktails")
data class Cocktail(
    @PrimaryKey val idDrink: String,
    val strDrink: String,
    val strDrinkThumb: String
)