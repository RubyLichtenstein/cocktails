package com.rubylichtenstein.cocktails.ui


sealed interface UiState<out T> {
    class Success<T>(val data: T) : UiState<T>
    data object Loading : UiState<Nothing>
    class Error(val message: String) : UiState<Nothing>
}