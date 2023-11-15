package com.rubylichtenstein.cocktails.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubylichtenstein.cocktails.data.api.CocktailsApi
import com.rubylichtenstein.cocktails.data.DrinkCategory
import com.rubylichtenstein.cocktails.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val repository: CocktailsApi
) :
    ViewModel() {
    private val _categories = MutableStateFlow<UiState<List<DrinkCategory>>>(UiState.Loading)
    val categories: StateFlow<UiState<List<DrinkCategory>>> = _categories.asStateFlow()

    init {
        fetchCocktailCategories()
    }

    fun fetchCocktailCategories() {
        viewModelScope.launch {
            _categories.value = UiState.Loading
            try {
                val categoriesFromApi = repository.getCocktailCategories()
                _categories.value = UiState.Success(categoriesFromApi)
            } catch (e: Exception) {
                _categories.value = UiState.Error(e.toString())
            }
        }
    }
}
