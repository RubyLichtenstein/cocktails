package com.rubylichtenstein.cocktails.ui.search

import androidx.lifecycle.viewModelScope
import com.rubylichtenstein.cocktails.data.repository.CocktailsRepository
import com.rubylichtenstein.cocktails.ui.asUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchApiCocktailsViewModel @Inject constructor(
    repository: CocktailsRepository
) : BaseSearchViewModel(repository) {

    override val debounceMillis = 500L

    override fun searchCocktails(query: String) {
        viewModelScope.launch {
            repository.searchCocktails(query)
                .asUiState()
                .collect {
                    _uiState.value = _uiState.value.copy(searchResult = it)
                }
        }
    }
}
