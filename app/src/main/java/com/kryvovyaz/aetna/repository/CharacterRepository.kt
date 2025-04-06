package com.kryvovyaz.aetna.repository

import com.kryvovyaz.aetna.models.CharactersContainerResult

interface CharacterRepository {
    suspend fun fetchMemeContainer(name: String): CharactersContainerResult
}