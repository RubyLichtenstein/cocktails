package com.rubylichtenstein.cocktails.data.model

data class CategoriesResponse(
    val drinks: List<Category>
)

data class Category(
    val strCategory: String
)
