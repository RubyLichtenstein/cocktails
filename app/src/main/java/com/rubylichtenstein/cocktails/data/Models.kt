package com.rubylichtenstein.cocktails.data

data class DrinksResponse(
    val drinks: List<DrinkCategory>
)

data class DrinkCategory(
    val strCategory: String
)

data class Cocktail(
    val strDrink: String,
    val strDrinkThumb: String,
    val idDrink: String
)

data class CocktailResponse(
    val drinks: List<Cocktail>
)

data class CocktailDetailResponse(
    val drinks: List<CocktailDetail>
)

data class CocktailDetail(
    val idDrink: String,
    val strDrink: String,
    val strDrinkAlternate: String?,
    val strTags: String?,
    val strVideo: String?,
    val strCategory: String,
    val strIBA: String?,
    val strAlcoholic: String,
    val strGlass: String,
    val strInstructions: String,
    val strInstructionsES: String?,
    val strInstructionsDE: String?,
    val strInstructionsFR: String?,
    val strInstructionsIT: String?,
    val strInstructionsZH_HANS: String?,
    val strInstructionsZH_HANT: String?,
    val strDrinkThumb: String,
    val strIngredient1: String?,
    val strIngredient2: String?,
    val strIngredient3: String?,
    val strIngredient4: String?,
    // ... up to strIngredient15
    val strMeasure1: String?,
    val strMeasure2: String?,
    val strMeasure3: String?,
    val strMeasure4: String?,
    // ... up to strMeasure15
    val strImageSource: String?,
    val strImageAttribution: String?,
    val strCreativeCommonsConfirmed: String?,
    val dateModified: String?
)
