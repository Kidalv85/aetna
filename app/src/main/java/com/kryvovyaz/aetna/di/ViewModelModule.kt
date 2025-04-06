package com.kryvovyaz.aetna.di

import com.kryvovyaz.aetna.repository.CharacterRepositoryImpl
import com.kryvovyaz.aetna.viewmodel.CharacterViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule {

    @Provides
    @Singleton
    fun provideMemeRepository(repository: CharacterRepositoryImpl): CharacterViewModel {
        return CharacterViewModel(repository)
    }
}