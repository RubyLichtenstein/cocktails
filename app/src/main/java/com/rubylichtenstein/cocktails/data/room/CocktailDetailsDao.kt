package com.rubylichtenstein.cocktails.data.room

import androidx.room.*
import com.rubylichtenstein.cocktails.data.model.CocktailDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface CocktailDetailsDao {
    @Query("SELECT * FROM cocktail_details")
    fun getAllCocktails(): List<CocktailDetails>

    @Query("SELECT * FROM cocktail_details WHERE idDrink = :id")
    fun getCocktailById(id: String): Flow<CocktailDetails?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg cocktails: CocktailDetails)

    @Delete
    suspend fun delete(cocktail: CocktailDetails)

    @Query("UPDATE cocktail_details SET isFavorite = :isFavorite WHERE idDrink = :id")
    suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean)
}
