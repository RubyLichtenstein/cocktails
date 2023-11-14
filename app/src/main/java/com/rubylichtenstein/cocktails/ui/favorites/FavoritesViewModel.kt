package com.rubylichtenstein.cocktails.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubylichtenstein.cocktails.data.model.Cocktail
import com.rubylichtenstein.cocktails.data.room.FavoriteCocktailsDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoriteCocktailsDao: FavoriteCocktailsDao
) : ViewModel() {
    private val _favoriteCocktails = MutableStateFlow<List<Cocktail>>(emptyList())
    val favoriteCocktails: StateFlow<List<Cocktail>> = _favoriteCocktails

    init {
        fetchFavorites()
    }

    private fun fetchFavorites() {
        viewModelScope.launch {
            favoriteCocktailsDao.getAllFavorites().collect {
                _favoriteCocktails.value = it
            }
        }
    }

    fun addFavorite(cocktail: Cocktail) {
        viewModelScope.launch {
            favoriteCocktailsDao.addFavorite(cocktail)
        }
    }
}