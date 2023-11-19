package com.rubylichtenstein.cocktails.ui.detailes

import androidx.lifecycle.viewModelScope
import com.rubylichtenstein.cocktails.data.model.Cocktail
import com.rubylichtenstein.cocktails.data.model.CocktailDetails
import com.rubylichtenstein.cocktails.data.repository.CocktailsRepository
import com.rubylichtenstein.cocktails.ui.UiState
import com.rubylichtenstein.cocktails.ui.asUiState
import com.rubylichtenstein.cocktails.ui.base.CocktailsBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CocktailDetailsViewModel @Inject constructor(
    private val repository: CocktailsRepository
) : CocktailsBaseViewModel(repository) {

    private val _cocktailDetails = MutableStateFlow<UiState<CocktailDetails>>(UiState.Loading)
    val cocktailDetails: StateFlow<UiState<CocktailDetails>> = _cocktailDetails.asStateFlow()

    private fun fetchCocktailDetails(cocktailId: String) {
        viewModelScope.launch {
            repository.getCocktailDetails(cocktailId)
                .asUiState()
                .collect {
                    _cocktailDetails.value = it
                }
        }
    }

    fun processIntents(intents: Intents) {
        when (intents) {
            is Intents.FetchCocktailDetails -> fetchCocktailDetails(intents.cocktailId)
            is Intents.UpdateFavoriteStatus -> updateFavoriteStatus(intents.cocktail)
        }
    }

    sealed class Intents {
        data class FetchCocktailDetails(val cocktailId: String) : Intents()
        data class UpdateFavoriteStatus(val cocktail: Cocktail) : Intents()
    }
}