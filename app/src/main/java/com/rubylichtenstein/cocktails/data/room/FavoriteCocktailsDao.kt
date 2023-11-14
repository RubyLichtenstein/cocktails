package com.rubylichtenstein.cocktails.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rubylichtenstein.cocktails.data.model.Cocktail
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteCocktailsDao {
    @Query("SELECT * FROM favorite_cocktails")
    fun getAllFavorites(): Flow<List<Cocktail>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(cocktail: Cocktail)

    @Delete
    suspend fun removeFavorite(cocktail: Cocktail)

    @Query("SELECT EXISTS(SELECT * FROM favorite_cocktails WHERE idDrink = :id)")
    fun isFavorite(id: String): Flow<Boolean>
}