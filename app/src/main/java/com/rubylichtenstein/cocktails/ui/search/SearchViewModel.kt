package com.rubylichtenstein.cocktails.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubylichtenstein.cocktails.data.Cocktail
import com.rubylichtenstein.cocktails.data.CocktailRepository
import com.rubylichtenstein.cocktails.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: CocktailRepository
) : ViewModel() {
    private val _searchResults = MutableStateFlow<UiState<List<Cocktail>>>(UiState.Loading)
    val searchResults: StateFlow<UiState<List<Cocktail>>> = _searchResults

    fun searchCocktails(query: String) {
        viewModelScope.launch {
            try {
                val response = repository.searchCocktails(query)
                _searchResults.value = UiState.Success(response)
            } catch (e: Exception) {
                _searchResults.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}