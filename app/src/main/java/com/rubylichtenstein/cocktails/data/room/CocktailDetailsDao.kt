package com.rubylichtenstein.cocktails.data.room

import androidx.room.*
import com.rubylichtenstein.cocktails.data.model.CocktailDetails


@Dao
interface CocktailDetailsDao {
    @Query("SELECT * FROM cocktail_details")
    fun getAllCocktails(): List<CocktailDetails>

    @Query("SELECT * FROM cocktail_details WHERE idDrink = :id")
    fun getCocktailById(id: String): CocktailDetails?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg cocktails: CocktailDetails)

    @Delete
    suspend fun delete(cocktail: CocktailDetails)
}
