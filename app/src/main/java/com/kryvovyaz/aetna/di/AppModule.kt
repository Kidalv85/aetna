package com.kryvovyaz.aetna.di

import com.kryvovyaz.aetna.api.CharacterService
import com.kryvovyaz.aetna.repository.CharacterRepositoryImpl
import com.kryvovyaz.aetna.util.BASE_URL
import com.kryvovyaz.aetna.viewmodel.CharacterViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideCharacterService(retrofit: Retrofit): CharacterService =
        retrofit.create(CharacterService::class.java)

    @Provides
    @Singleton
    fun provideCharactersRepository(memeService: CharacterService): CharacterRepositoryImpl {
        return CharacterRepositoryImpl(memeService)
    }

    @Provides
    @Singleton
    fun provideCharactersViewmodel(repository: CharacterRepositoryImpl): CharacterViewModel {
        return CharacterViewModel(repository)
    }
}