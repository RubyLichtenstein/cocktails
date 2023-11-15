package com.rubylichtenstein.cocktails.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubylichtenstein.cocktails.data.model.Cocktail
import com.rubylichtenstein.cocktails.data.repository.CocktailsRepository
import com.rubylichtenstein.cocktails.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: CocktailsRepository
) : ViewModel() {
    private val _searchResults = MutableStateFlow<UiState<List<Cocktail>>>(UiState.Loading)
    val searchResults: StateFlow<UiState<List<Cocktail>>> = _searchResults

    fun searchCocktails(query: String, searchInFavorites: Boolean) {
        viewModelScope.launch {
            try {
                val response = if (searchInFavorites) {
                    repository.getFavoriteCocktails().map {
                        it.filter { cocktail ->
                            cocktail.strDrink.contains(query, ignoreCase = true)
                        }
                    }
                } else {
                    repository.searchCocktails(query)
                }

                response.collect {
                    _searchResults.value = UiState.Success(it)
                }
            } catch (e: Exception) {
                _searchResults.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun updateFavoriteStatus(cocktail: Cocktail) {
        viewModelScope.launch {
            repository.updateFavoriteStatus(cocktail, !cocktail.isFavorite)
        }
    }
}