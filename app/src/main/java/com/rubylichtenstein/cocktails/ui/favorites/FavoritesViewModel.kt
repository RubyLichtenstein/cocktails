package com.rubylichtenstein.cocktails.ui.favorites

import androidx.lifecycle.viewModelScope
import com.rubylichtenstein.cocktails.data.model.Cocktail
import com.rubylichtenstein.cocktails.data.repository.CocktailsRepository
import com.rubylichtenstein.cocktails.ui.UiState
import com.rubylichtenstein.cocktails.ui.asUiState
import com.rubylichtenstein.cocktails.ui.base.CocktailsBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: CocktailsRepository
) : CocktailsBaseViewModel(repository) {
    private val _favoriteCocktails = MutableStateFlow<UiState<List<Cocktail>>>(UiState.Loading)
    val favoriteCocktails: StateFlow<UiState<List<Cocktail>>> = _favoriteCocktails

    init {
        fetchFavorites()
    }

    private fun fetchFavorites() {
        viewModelScope.launch {
            repository.getFavoriteCocktails().asUiState().collect {
                _favoriteCocktails.value = it
            }
        }
    }

    fun processIntents(intents: Intents) {
        when (intents) {
            is Intents.UpdateFavoriteStatus -> updateFavoriteStatus(intents.cocktail)
            Intents.FetchFavorites -> fetchFavorites()
        }
    }

    sealed class Intents {
        object FetchFavorites : Intents()
        data class UpdateFavoriteStatus(val cocktail: Cocktail) : Intents()
    }
}