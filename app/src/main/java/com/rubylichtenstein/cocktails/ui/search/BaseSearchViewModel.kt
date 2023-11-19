@file:OptIn(FlowPreview::class)

package com.rubylichtenstein.cocktails.ui.search

import androidx.lifecycle.viewModelScope
import com.rubylichtenstein.cocktails.data.model.Cocktail
import com.rubylichtenstein.cocktails.data.repository.CocktailsRepository
import com.rubylichtenstein.cocktails.ui.UiState
import com.rubylichtenstein.cocktails.ui.base.CocktailsBaseViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

abstract class BaseSearchViewModel(
    protected val repository: CocktailsRepository
) : CocktailsBaseViewModel(repository) {
    protected val _uiState = MutableStateFlow(
        SearchUiState(
            searchResult = UiState.Initial,
            searchQuery = "",
            isActive = false
        )
    )
    val uiState: StateFlow<SearchUiState> = _uiState

    abstract fun debounceMillis(): Long

    init {
        viewModelScope.launch {
            uiState
                .map { it.searchQuery }
                .distinctUntilChanged()
                .debounce(debounceMillis())
                .collect { query ->
                    val isBlank = query.isBlank()

                    _uiState.value = _uiState.value.copy(
                        searchResult = if (isBlank) UiState.Initial else UiState.Loading
                    )

                    if (!isBlank)
                        searchCocktails(query)
                }
        }
    }

    abstract fun searchCocktails(query: String)

    private fun clearSearch() {
        _uiState.value = _uiState.value.copy(
            searchQuery = "",
            searchResult = UiState.Initial
        )
    }

    fun processIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.SearchQueryChanged -> {
                _uiState.value = _uiState.value.copy(
                    searchQuery = intent.query,
                )
            }

            is SearchIntent.ToggleFavorite -> {
                updateFavoriteStatus(intent.cocktail)
            }

            SearchIntent.ClearSearch -> clearSearch()

            is SearchIntent.SetActive -> {
                _uiState.value = _uiState.value.copy(isActive = intent.isActive)
            }

            SearchIntent.Refresh -> {
                processIntent(SearchIntent.SearchQueryChanged(_uiState.value.searchQuery))
            }
        }
    }
}

sealed class SearchIntent {
    data class SearchQueryChanged(
        val query: String,
    ) : SearchIntent()

    data class ToggleFavorite(val cocktail: Cocktail) : SearchIntent()
    data object ClearSearch : SearchIntent()

    data class SetActive(val isActive: Boolean) : SearchIntent()
    data object Refresh : SearchIntent()
}

data class SearchUiState(
    val searchResult: UiState<List<Cocktail>>,
    val searchQuery: String,
    val isActive: Boolean
)