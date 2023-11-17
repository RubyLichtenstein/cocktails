package com.rubylichtenstein.cocktails.ui.search

import androidx.lifecycle.viewModelScope
import com.rubylichtenstein.cocktails.data.repository.CocktailsRepository
import com.rubylichtenstein.cocktails.ui.asUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchFavoriteCocktailsViewModel @Inject constructor(
    repository: CocktailsRepository
) : BaseSearchViewModel(repository) {

    override val debounceMillis = 0L

    override fun searchCocktails(query: String) {
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
}
