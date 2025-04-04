package com.kryvovyaz.aetna.di

import com.kryvovyaz.aetna.api.CharacterService
import com.kryvovyaz.aetna.repository.CharacterRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMemeRepository(memeService: CharacterService): CharacterRepositoryImpl {
        return CharacterRepositoryImpl(memeService)
    }
}