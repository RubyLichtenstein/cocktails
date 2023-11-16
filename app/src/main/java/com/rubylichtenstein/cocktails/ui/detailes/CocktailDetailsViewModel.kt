package com.rubylichtenstein.cocktails.ui.detailes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubylichtenstein.cocktails.data.model.Cocktail
import com.rubylichtenstein.cocktails.data.model.CocktailDetails
import com.rubylichtenstein.cocktails.data.repository.CocktailsRepository
import com.rubylichtenstein.cocktails.ui.UiState
import com.rubylichtenstein.cocktails.ui.asUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CocktailDetailsViewModel @Inject constructor(
    private val repository: CocktailsRepository
) : ViewModel() {

    private val _cocktailDetails = MutableStateFlow<UiState<CocktailDetails>>(UiState.Loading)
    val cocktailDetails: StateFlow<UiState<CocktailDetails>> = _cocktailDetails.asStateFlow()

    fun fetchCocktailDetails(cocktailId: String) {
        viewModelScope.launch {
            repository.getCocktailDetails(cocktailId)
                .asUiState()
                .collect {
                    _cocktailDetails.value = it
                }
        }
    }

    fun updateFavoriteStatus(cocktail: Cocktail) {
        viewModelScope.launch {
            repository.updateFavoriteStatus(cocktail, !cocktail.isFavorite)
        }
    }
}