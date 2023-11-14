package com.rubylichtenstein.cocktails.ui.cocktails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubylichtenstein.cocktails.ui.UiState
import com.rubylichtenstein.cocktails.data.CocktailRepository
import com.rubylichtenstein.cocktails.data.model.Cocktail
import com.rubylichtenstein.cocktails.data.model.CocktailDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CocktailViewModel @Inject constructor(
    private val repository: CocktailRepository
) : ViewModel() {

    private val _cocktailsByCategory = MutableStateFlow<UiState<List<Cocktail>>>(UiState.Loading)
    val cocktailsByCategory: StateFlow<UiState<List<Cocktail>>> = _cocktailsByCategory.asStateFlow()

    fun fetchCocktailsByCategory(category: String) {
        viewModelScope.launch {
            _cocktailsByCategory.value = UiState.Loading
            try {
                val cocktails = repository.getCocktailsByCategory(category)
                _cocktailsByCategory.value = UiState.Success(cocktails)
            } catch (e: Exception) {
                _cocktailsByCategory.value = UiState.Error(e.toString())
            }
        }
    }

    private val _cocktailDetails = MutableStateFlow<UiState<CocktailDetails>>(UiState.Loading)
    val cocktailDetails: StateFlow<UiState<CocktailDetails>> = _cocktailDetails.asStateFlow()

    fun fetchCocktailDetails(cocktailId: String) {
        viewModelScope.launch {
            _cocktailDetails.value = UiState.Loading
            try {
                val details = repository.getCocktailDetails(cocktailId)
                _cocktailDetails.value = UiState.Success(details.first())
            } catch (e: Exception) {
                _cocktailDetails.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}