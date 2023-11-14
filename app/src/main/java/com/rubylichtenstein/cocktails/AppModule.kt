package com.rubylichtenstein.cocktails

import com.rubylichtenstein.cocktails.data.CocktailRepository
import com.rubylichtenstein.cocktails.data.RetrofitClient.theCocktailDbService
import com.rubylichtenstein.cocktails.data.TheCocktailDbService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTheCocktailDbService(): TheCocktailDbService = theCocktailDbService

    @Provides
    @Singleton
    fun provideCocktailRepository(theCocktailDbService: TheCocktailDbService): CocktailRepository =
        CocktailRepository(theCocktailDbService)
}