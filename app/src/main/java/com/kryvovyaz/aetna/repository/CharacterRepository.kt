package com.kryvovyaz.aetna.repository

import com.kryvovyaz.aetna.models.CharactersContainerResult

interface CharacterRepository {
    suspend fun fetchCharactersContainer(name: String): CharactersContainerResult
}