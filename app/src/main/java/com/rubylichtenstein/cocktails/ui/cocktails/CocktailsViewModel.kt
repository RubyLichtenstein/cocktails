package com.rubylichtenstein.cocktails.ui.cocktails

import androidx.lifecycle.viewModelScope
import com.rubylichtenstein.cocktails.data.model.Cocktail
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
class CocktailsViewModel @Inject constructor(
    private val repository: CocktailsRepository
) : CocktailsBaseViewModel(repository) {

    private val _cocktailsByCategory = MutableStateFlow<UiState<List<Cocktail>>>(UiState.Loading)
    val cocktailsByCategory: StateFlow<UiState<List<Cocktail>>> = _cocktailsByCategory.asStateFlow()

    private fun fetchCocktailsByCategory(category: String) {
        viewModelScope.launch {
            repository.getCocktailsByCategory(category).asUiState().collect {
                _cocktailsByCategory.value = it
            }
        }
    }

    fun processIntents(intents: Intents) {
        when (intents) {
            is Intents.FetchCocktailsByCategory -> fetchCocktailsByCategory(intents.category)
            is Intents.UpdateFavoriteStatus -> updateFavoriteStatus(intents.cocktail)
        }
    }

    sealed class Intents {
        data class FetchCocktailsByCategory(val category: String) : Intents()
        data class UpdateFavoriteStatus(val cocktail: Cocktail) : Intents()
    }
}