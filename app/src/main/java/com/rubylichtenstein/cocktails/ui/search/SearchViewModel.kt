package com.rubylichtenstein.cocktails.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubylichtenstein.cocktails.data.model.Cocktail
import com.rubylichtenstein.cocktails.data.repository.CocktailsRepository
import com.rubylichtenstein.cocktails.ui.UiState
import com.rubylichtenstein.cocktails.ui.asUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val searchResult: UiState<List<Cocktail>>,
    val searchQuery: String,
    val isActive: Boolean
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: CocktailsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState(UiState.Loading, "", false))
    val uiState: StateFlow<SearchUiState> = _uiState

    private fun searchCocktails(query: String) {
        viewModelScope.launch {
            repository.searchCocktails(query)
                .asUiState()
                .collect {
                    _uiState.value = _uiState.value.copy(searchResult = it)
                }
        }
    }

    private fun searchFavoriteCocktails(query: String) {
        viewModelScope.launch {
            repository.getFavoriteCocktails()
                .map {
                    it.filter { cocktail ->
                        cocktail.strDrink.contains(query, ignoreCase = true)
                    }
                }
                .asUiState()
                .collect {
                    _uiState.value = _uiState.value.copy(searchResult = it)
                }
        }
    }

    private fun updateFavoriteStatus(cocktail: Cocktail) {
        viewModelScope.launch {
            repository.updateFavoriteStatus(cocktail, !cocktail.isFavorite)
        }
    }

    fun processIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.SearchQueryChanged -> {
                _uiState.value = _uiState.value.copy(searchQuery = intent.query, isActive = true)
                if (intent.searchFavorites) {
                    searchFavoriteCocktails(intent.query)
                } else {
                    searchCocktails(intent.query)
                }
            }

            is SearchIntent.ToggleFavorite -> {
                updateFavoriteStatus(intent.cocktail)
            }

            SearchIntent.ClearSearch -> clearSearch()

            is SearchIntent.SetActive -> {
                _uiState.value = _uiState.value.copy(isActive = intent.isActive)
            }

            SearchIntent.Refresh -> {
                if (_uiState.value.searchQuery.isNotBlank()) {
                    searchCocktails(_uiState.value.searchQuery)
                }
            }
        }
    }

    private fun clearSearch() {
        _uiState.value = _uiState.value.copy(searchQuery = "", isActive = false)
    }
}

sealed class SearchIntent {
    data class SearchQueryChanged(
        val query: String,
        val searchFavorites: Boolean
    ) : SearchIntent()

    data class ToggleFavorite(val cocktail: Cocktail) : SearchIntent()
    data object ClearSearch : SearchIntent()

    data class SetActive(val isActive: Boolean) : SearchIntent()
    data object Refresh : SearchIntent()
}