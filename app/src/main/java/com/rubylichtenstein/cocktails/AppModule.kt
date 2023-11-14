package com.rubylichtenstein.cocktails

import android.content.Context
import com.rubylichtenstein.cocktails.data.CocktailRepository
import com.rubylichtenstein.cocktails.data.RetrofitClient.theCocktailDbService
import com.rubylichtenstein.cocktails.data.TheCocktailDbService
import com.rubylichtenstein.cocktails.data.room.AppDatabase
import com.rubylichtenstein.cocktails.data.room.FavoriteCocktailsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideFavoriteCocktailsDao(database: AppDatabase): FavoriteCocktailsDao {
        return database.favoriteCocktailsDao()
    }
}